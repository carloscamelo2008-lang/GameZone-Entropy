package com.gamezone.persistence;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.service.ProductService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of warranties to and from a CSV file. This class
 * is responsible only for reading and writing data; it contains no
 * business logic beyond resolving the Sale and Product references
 * needed to rebuild each warranty on load.
 *
 */
public class WarrantyRepository {

    private final String filePath;
    private final SaleRepository saleRepository;
    private final ProductService productService;

    /**
     * Creates a warranty repository backed by the given CSV file,
     * resolving Sale and Product references through the given services.
     *
     * @param filePath       path to the CSV file used for persistence
     * @param saleRepository    repository used to resolve sale references on load
     * @param productService service used to resolve product references on load
     */
    public WarrantyRepository(String filePath, SaleRepository saleRepository, ProductService productService) {
        this.filePath = filePath;
        this.saleRepository = saleRepository;
        this.productService = productService;
    }

    /**
     * Saves the complete list of warranties to the file, overwriting
     * any previously stored data.
     *
     * @param warranties the list of warranties to persist
     */
    public void saveAll(List<Warranty> warranties) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Warranty warranty : warranties) {
                writer.println(toCsvLine(warranty));
            }
        } catch (IOException e) {
            System.out.println("Error saving warranties: " + e.getMessage());
        }
    }

    /**
     * Loads the complete list of warranties from the file. If the file
     * does not exist yet (e.g. on first run), an empty list is returned.
     * Lines whose sale or product cannot be resolved are skipped.
     *
     * @return the list of warranties previously saved, or an empty list
     */
    public List<Warranty> loadAll() {
        List<Warranty> warranties = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return warranties;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                Warranty warranty = fromCsvLine(line);
                if (warranty != null) {
                    warranties.add(warranty);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading warranties: " + e.getMessage());
        }

        return warranties;
    }

    /**
     * Builds the pseudo-identifier used to persist and later resolve a
     * sale, composed of its date and its customer id, since {@link Sale}
     * does not currently expose a unique identifier of its own.
     *
     * @param sale the sale to build the identifier for
     * @return the composite sale identifier
     */
    private String buildSaleReference(Sale sale) {
        return sale.getDate() + "|" + sale.getCustomer().getId();
    }

    /**
     * Converts a warranty into a single CSV line, using a discriminator
     * column to distinguish between the two concrete warranty types.
     *
     * @param warranty the warranty to serialize
     * @return the CSV representation of the warranty
     */
    private String toCsvLine(Warranty warranty) {
        String type = (warranty instanceof BasicWarranty) ? "BASIC" : "EXTENDED";

        return warranty.getId() + ","
                + type + ","
                + warranty.getProduct().getId() + ","
                + buildSaleReference(warranty.getSale()) + ","
                + warranty.getStartDate();
    }

    /**
     * Rebuilds a warranty from a single CSV line, resolving its product
     * and sale references. Returns null when the referenced product or
     * sale can no longer be found.
     *
     * @param line the CSV line to parse
     * @return the rebuilt warranty, or null if it could not be resolved
     */
    private Warranty fromCsvLine(String line) {
        String[] fields = line.split(",", -1);

        String id = fields[0];
        String type = fields[1];
        String productId = fields[2];
        LocalDateTime saleDate = LocalDateTime.parse(fields[3]);
        String customerId = fields[4];
        LocalDate startDate = LocalDate.parse(fields[5]);

        Product product = findProductById(productId);
        Sale sale = findSale(saleDate, customerId);

        if (product == null || sale == null) {
            return null;
        }

        if (type.equals("BASIC")) {
            return new BasicWarranty(id, product, sale, startDate);
        }

        return new ExtendedWarranty(id, product, sale, startDate);
    }

    /**
     * Finds a product by its id among the products currently managed
     * by {@link ProductService}.
     *
     * @param productId the id of the product to find
     * @return the matching product, or null if not found
     */
    private Product findProductById(String productId) {
        for (Product product : productService.listAllProducts()) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Finds a sale by its date and its customer id among the sales
     * currently managed by {@link SaleRepository}.
     *
     * @param date       the date and time of the sale
     * @param customerId the id of the customer who made the sale
     * @return the matching sale, or null if not found
     */
    private Sale findSale(LocalDateTime date, String customerId) {
        for (Sale sale : saleRepository.loadAll()) {
            if (sale.getDate().equals(date) && sale.getCustomer().getId().equals(customerId)) {
                return sale;
            }
        }
        return null;
    }
}

