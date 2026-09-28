package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.persistence.ReturnRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contains the business rules related to product returns: registration
 * with deadline and ownership validation, stock restoration, queries,
 * and the monthly balance between sales and returns.
 */
public class ReturnService {

    private final ReturnRepository returnRepository;
    private final SaleService saleService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final List<Return> returns;

    /**
     * Creates a return service and loads previously stored returns.
     *
     * @param returnRepository repository used to persist returns
     * @param saleService      service used to find the original sales
     * @param productService   service used to restore product stock
     * @param accessoryService service used to restore accessory stock
     */
    public ReturnService(ReturnRepository returnRepository, SaleService saleService, ProductService productService,
            AccessoryService accessoryService) {
        this.returnRepository = returnRepository;
        this.saleService = saleService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.returns = returnRepository.loadAll();
    }

    /**
     * Registers a return after validating that the original sale exists,
     * that it is still within the 30-day return period, and that the given
     * products belong to it. On success the stock of the returned products
     * is restored and the return is persisted.
     *
     * @param saleId     the composite reference of the original sale, built
     *                   as {@code sale.getDate() + "|" + sale.getCustomer().getId()}
     * @param productIds the ids of the products being returned (an id may
     *                   repeat when several units are returned)
     * @param reason     the reason for the return
     * @return the newly registered return
     * @throws IllegalArgumentException if any validation fails
     */
    public Return registerReturn(String saleId, List<String> productIds, String reason) {
        Sale sale = findSaleByReference(saleId);
        if (sale == null) {
            throw new IllegalArgumentException("La venta indicada no existe.");
        }

        if (!sale.canBeReturned()) {
            throw new IllegalArgumentException(
                    "No se puede registrar la devolución: han pasado más de 30 días desde la venta.");
        }

        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Debe indicar al menos un producto a devolver.");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el motivo de la devolución.");
        }

        Map<String, Integer> requested = countById(productIds);
        List<Product> returnedProducts = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : requested.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = findProductInSale(sale, productId);
            if (product == null) {
                throw new IllegalArgumentException(
                        "El producto " + productId + " no pertenece a la venta indicada.");
            }

            int available = countInSale(sale, productId) - countAlreadyReturned(sale, productId);
            if (quantity > available) {
                throw new IllegalArgumentException(
                        "No se pueden devolver " + quantity + " unidad(es) del producto " + productId
                        + ": solo quedan " + available + " por devolver en esta venta.");
            }

            for (int i = 0; i < quantity; i++) {
                returnedProducts.add(product);
            }
        }

        Return newReturn = new Return(
                "RET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                LocalDate.now(),
                sale,
                returnedProducts,
                reason
        );

        for (Map.Entry<String, Integer> entry : requested.entrySet()) {
            restoreItemStock(findProductInSale(sale, entry.getKey()), entry.getValue());
        }

        returns.add(newReturn);
        returnRepository.saveAll(returns);

        return newReturn;
    }

    /**
     * Returns all registered returns.
     *
     * @return the complete list of returns
     */
    public List<Return> viewAllReturns() {
        return returns;
    }

    /**
     * Returns the returns whose original sale belongs to the given customer.
     *
     * @param customerId the id of the customer
     * @return the returns associated with the customer
     */
    public List<Return> viewReturnsByCustomer(String customerId) {
        List<Return> result = new ArrayList<>();

        for (Return aReturn : returns) {
            if (aReturn.getSale().getCustomer().getId().equals(customerId)) {
                result.add(aReturn);
            }
        }

        return result;
    }

    /**
     * Returns the returns associated with a specific sale.
     *
     * @param saleId the composite reference of the sale
     * @return the returns associated with the sale
     */
    public List<Return> viewReturnsBySale(String saleId) {
        List<Return> result = new ArrayList<>();

        for (Return aReturn : returns) {
            if (buildSaleReference(aReturn.getSale()).equals(saleId)) {
                result.add(aReturn);
            }
        }

        return result;
    }

    /**
     * Calculates the net balance of a month: the total of the sales made in
     * that month minus the total refunded by the returns registered in it.
     *
     * @param month the month (1-12)
     * @param year  the year
     * @return the net balance (sales minus returns)
     * @throws IllegalArgumentException if the month is not between 1 and 12
     */
    public double generateMonthlyBalance(int month, int year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }

        double totalSales = 0;
        for (Sale sale : saleService.listAllSales()) {
            if (sale.getDate().getMonthValue() == month && sale.getDate().getYear() == year) {
                totalSales += sale.calculateTotal();
            }
        }

        double totalReturns = 0;
        for (Return aReturn : returns) {
            if (aReturn.getDate().getMonthValue() == month && aReturn.getDate().getYear() == year) {
                totalReturns += aReturn.getRefundAmount();
            }
        }

        return totalSales - totalReturns;
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
     * @param saleId the composite reference to match
     * @return the matching sale, or null if not found
     */
    private Sale findSaleByReference(String saleId) {
        for (Sale sale : saleService.listAllSales()) {
            if (buildSaleReference(sale).equals(saleId)) {
                return sale;
            }
        }
        return null;
    }

    /**
     * Finds a product inside the products of a sale.
     *
     * @param sale      the sale to search
     * @param productId the id of the product to find
     * @return the matching product, or null if the sale does not include it
     */
    private Product findProductInSale(Sale sale, String productId) {
        for (Product product : sale.getProducts()) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Counts how many units of a product were included in a sale.
     *
     * @param sale      the sale to inspect
     * @param productId the id of the product
     * @return the number of units of the product in the sale
     */
    private int countInSale(Sale sale, String productId) {
        int count = 0;
        for (Product product : sale.getProducts()) {
            if (product.getId().equals(productId)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts how many units of a product have already been returned for a sale.
     *
     * @param sale      the original sale
     * @param productId the id of the product
     * @return the number of units already returned
     */
    private int countAlreadyReturned(Sale sale, String productId) {
        String saleReference = buildSaleReference(sale);
        int count = 0;

        for (Return aReturn : returns) {
            if (buildSaleReference(aReturn.getSale()).equals(saleReference)) {
                for (Product product : aReturn.getReturnedProducts()) {
                    if (product.getId().equals(productId)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    /**
     * Counts how many times each id appears in the given list, keeping the
     * order in which the ids first appear.
     *
     * @param ids the list of ids
     * @return a map from each id to its number of occurrences
     */
    private Map<String, Integer> countById(List<String> ids) {
        Map<String, Integer> counts = new LinkedHashMap<>();

        for (String id : ids) {
            counts.put(id, counts.getOrDefault(id, 0) + 1);
        }

        return counts;
    }

    /**
     * Restores the stock of a returned item: accessories through
     * {@link AccessoryService}, any other product through {@link ProductService}.
     *
     * @param item     the returned item
     * @param quantity the number of units to put back in stock
     */
    private void restoreItemStock(Product item, int quantity) {
        if (item instanceof Accessory) {
            accessoryService.updateStock(item.getId(), quantity);
        } else {
            productService.restoreStock(item.getId(), quantity);
        }
    }
}
