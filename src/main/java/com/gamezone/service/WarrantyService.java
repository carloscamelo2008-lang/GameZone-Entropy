package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;
import com.gamezone.persistence.WarrantyRepository.WarrantyRecord;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Contains the business rules related to product warranties, including
 * assignment of basic and extended warranties, and queries about their
 * validity. This service resolves the Sale and Product references for
 * each warranty using {@link SaleRepository} and {@link ProductService}
 * directly, which avoids the circular dependency that would arise if
 * {@link WarrantyRepository} depended on SaleService instead.
 */
public class WarrantyService {

    private final WarrantyRepository warrantyRepository;
    private final SaleRepository saleRepository;
    private final ProductService productService;
    private final List<Warranty> warranties;

    /**
     * Creates a warranty service, loading previously stored warranties
     * and resolving their Sale and Product references.
     *
     * @param warrantyRepository repository used to persist raw warranty records
     * @param saleRepository     repository used to resolve sale references
     * @param productService     service used to resolve product references
     */
    public WarrantyService(WarrantyRepository warrantyRepository, SaleRepository saleRepository, ProductService productService) {
        this.warrantyRepository = warrantyRepository;
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.warranties = resolveWarranties(warrantyRepository.loadAll());
    }

    /**
     * Creates and persists an automatic basic warranty for the given
     * product, associated to the given sale.
     *
     * @param product   the product covered by the warranty
     * @param sale      the sale in which the product was purchased
     * @param startDate the date the warranty coverage begins
     * @return the newly created basic warranty
     */
    public BasicWarranty assignBasicWarranty(Product product, Sale sale, LocalDate startDate) {
        BasicWarranty warranty = new BasicWarranty(generateId(), product, sale, startDate);
        warranties.add(warranty);
        warrantyRepository.saveAll(warranties);
        return warranty;
    }

    /**
     * Creates and persists an extended warranty for the given product,
     * associated to the given sale.
     *
     * @param product   the product covered by the warranty
     * @param sale      the sale in which the product was purchased
     * @param startDate the date the warranty coverage begins
     * @return the newly created extended warranty
     */
    public ExtendedWarranty assignExtendedWarranty(Product product, Sale sale, LocalDate startDate) {
        ExtendedWarranty warranty = new ExtendedWarranty(generateId(), product, sale, startDate);
        warranties.add(warranty);
        warrantyRepository.saveAll(warranties);
        return warranty;
    }

    /**
     * Finds the warranty associated to a specific product within a
     * specific sale.
     *
     * @param productId the id of the covered product
     * @param saleId    the composite identifier of the sale, built with
     *                  {@code sale.getDate() + "|" + sale.getCustomer().getId()}
     * @return the matching warranty, or null if not found
     */
    public Warranty findWarrantyByProduct(String productId, String saleId) {
        for (Warranty warranty : warranties) {
            if (warranty.getProduct().getId().equals(productId) && buildSaleReference(warranty.getSale()).equals(saleId)) {
                return warranty;
            }
        }
        return null;
    }

    /**
     * Returns all registered warranties.
     *
     * @return the complete list of warranties
     */
    public List<Warranty> listAllWarranties() {
        return warranties;
    }

    /**
     * Returns all warranties currently active on today's date.
     *
     * @return the list of active warranties
     */
    public List<Warranty> listActiveWarranties() {
        List<Warranty> activeWarranties = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Warranty warranty : warranties) {
            if (warranty.isActive(today)) {
                activeWarranties.add(warranty);
            }
        }

        return activeWarranties;
    }

    /**
     * Returns all warranties whose coverage ends within the given
     * number of days from today, and that have not expired yet.
     *
     * @param daysAhead how many days ahead to look for expirations
     * @return the list of warranties expiring soon
     */
    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {
        List<Warranty> expiringSoon = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(daysAhead);

        for (Warranty warranty : warranties) {
            LocalDate endDate = warranty.getEndDate();
            if (!endDate.isBefore(today) && !endDate.isAfter(limit)) {
                expiringSoon.add(warranty);
            }
        }

        return expiringSoon;
    }

    /**
     * Resolves a list of raw warranty records into real warranties,
     * looking up each referenced product and sale. Records whose
     * product or sale can no longer be found are skipped.
     *
     * @param records the raw records loaded from the repository
     * @return the list of resolved warranties
     */
    private List<Warranty> resolveWarranties(List<WarrantyRecord> records) {
        List<Warranty> resolved = new ArrayList<>();

        for (WarrantyRecord record : records) {
            Product product = findProductById(record.getProductId());
            Sale sale = findSaleByReference(record.getSaleReference());

            if (product == null || sale == null) {
                continue;
            }

            Warranty warranty = record.getType().equals("BASIC")
                    ? new BasicWarranty(record.getId(), product, sale, record.getStartDate())
                    : new ExtendedWarranty(record.getId(), product, sale, record.getStartDate());

            resolved.add(warranty);
        }

        return resolved;
    }

    /**
     * Builds the pseudo-identifier used to identify a sale, composed of
     * its date and its customer id, since {@link Sale} does not
     * currently expose a unique identifier of its own.
     *
     * @param sale the sale to build the reference for
     * @return the composite sale reference
     */
    private String buildSaleReference(Sale sale) {
        return sale.getDate() + "|" + sale.getCustomer().getId();
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
     * Finds a sale whose composite reference matches the given value,
     * among the sales currently managed by {@link SaleRepository}.
     *
     * @param saleReference the composite reference to match
     * @return the matching sale, or null if not found
     */
    private Sale findSaleByReference(String saleReference) {
        for (Sale sale : saleRepository.loadAll()) {
            if (buildSaleReference(sale).equals(saleReference)) {
                return sale;
            }
        }
        return null;
    }

    /**
     * Generates a unique identifier for a new warranty.
     *
     * @return a newly generated warranty id
     */
    private String generateId() {
        return "WAR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
