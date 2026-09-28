package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.ProductService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of returns to and from a CSV file. Each line stores
 * the return id, its date, a composite reference to the original sale, the
 * ids of the returned products and the reason (kept last so that it may
 * safely contain commas). Sale and Product references are resolved on load
 * through {@link SaleRepository}, {@link ProductService} and
 * {@link AccessoryService} (accessories are kept apart from products).
 */
public class ReturnRepository {

    private static final String PRODUCT_SEPARATOR = ";";

    private final String filePath;
    private final SaleRepository saleRepository;
    private final ProductService productService;
    private final AccessoryService accessoryService;

    /**
     * Creates a return repository backed by the given CSV file.
     *
     * @param filePath        path to the CSV file used for persistence
     * @param saleRepository  repository used to resolve sale references on load
     * @param productService  service used to resolve product references on load
     * @param accessoryService service used to resolve accessory references on load
     */
    public ReturnRepository(String filePath, SaleRepository saleRepository, ProductService productService,
                            AccessoryService accessoryService) {
        this.filePath = filePath;
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.accessoryService = accessoryService;
    }

    /**
     * Saves the complete list of returns to the file, overwriting any
     * previously stored data.
     *
     * @param returns the list of returns to persist
     */
    public void saveAll(List<Return> returns) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Return aReturn : returns) {
                writer.println(toCsvLine(aReturn));
            }
        } catch (IOException e) {
            System.out.println("Error saving returns: " + e.getMessage());
        }
    }

    /**
     * Loads the complete list of returns from the file. If the file does not
     * exist yet (e.g. on first run), an empty list is returned. Lines whose
     * sale or products can no longer be resolved are skipped.
     *
     * @return the list of returns previously saved, or an empty list
     */
    public List<Return> loadAll() {
        List<Return> returns = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return returns;
        }

        List<Sale> sales = saleRepository.loadAll();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                Return loaded = fromCsvLine(line, sales);
                if (loaded != null) {
                    returns.add(loaded);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading returns: " + e.getMessage());
        }

        return returns;
    }

    /**
     * Converts a return into a single CSV line.
     *
     * @param aReturn the return to serialize
     * @return the CSV representation of the return
     */
    private String toCsvLine(Return aReturn) {
        List<String> productIds = new ArrayList<>();
        for (Product product : aReturn.getReturnedProducts()) {
            productIds.add(product.getId());
        }

        String reason = aReturn.getReason().replace("\r", " ").replace("\n", " ");

        return aReturn.getId() + ","
                + aReturn.getDate() + ","
                + buildSaleReference(aReturn.getSale()) + ","
                + String.join(PRODUCT_SEPARATOR, productIds) + ","
                + reason;
    }

    /**
     * Rebuilds a return from a single CSV line, resolving its sale and
     * product references. Returns null when the line is malformed or a
     * reference can no longer be found.
     *
     * @param line  the CSV line to parse
     * @param sales the sales used to resolve the sale reference
     * @return the rebuilt return, or null if it could not be resolved
     */
    private Return fromCsvLine(String line, List<Sale> sales) {
        String[] fields = line.split(",", 5);

        if (fields.length < 5) {
            return null;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(fields[1]);
        } catch (DateTimeParseException e) {
            return null;
        }

        Sale sale = findSaleByReference(sales, fields[2]);
        if (sale == null) {
            return null;
        }

        List<Product> returnedProducts = new ArrayList<>();
        for (String productId : fields[3].split(PRODUCT_SEPARATOR)) {
            Product product = findProductById(productId);
            if (product == null) {
                return null;
            }
            returnedProducts.add(product);
        }

        return new Return(fields[0], date, sale, returnedProducts, fields[4]);
    }

    /**
     * Builds the composite reference used to identify a sale, made of its
     * date and its customer id, since {@link Sale} has no unique id of its own.
     *
     * @param sale the sale to build the reference for
     * @return the composite sale reference
     */
    private String buildSaleReference(Sale sale) {
        return sale.getDate() + "|" + sale.getCustomer().getId();
    }

    /**
     * Finds a sale by its composite reference.
     *
     * @param sales         the sales to search
     * @param saleReference the composite reference to match
     * @return the matching sale, or null if not found
     */
    private Sale findSaleByReference(List<Sale> sales, String saleReference) {
        for (Sale sale : sales) {
            if (buildSaleReference(sale).equals(saleReference)) {
                return sale;
            }
        }
        return null;
    }

    /**
     * Finds a product by its id among the products managed by
     * {@link ProductService} and, when it is not there, among the accessories
     * managed by {@link AccessoryService}.
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
        return accessoryService.findById(productId);
    }
}
