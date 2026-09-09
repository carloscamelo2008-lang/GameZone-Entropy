package com.gamezone.service;

import com.gamezone.model.Sale;
import com.gamezone.persistence.SaleRepository;

import java.util.List;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Seller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Contains the business rules related to sales.
 */
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;
    private final PersonService personService;
    private final List<Sale> sales;
    /**
     * Creates a sale service and loads previously stored sales.
     *
     * @param saleRepository repository used to persist sales
     * @param productService service used to access and update products
     * @param personService service used to find customers and sellers
     */
    public SaleService(
            SaleRepository saleRepository,
            ProductService productService,
            PersonService personService) {

        this.saleRepository = saleRepository;
        this.productService = productService;
        this.personService = personService;

        this.sales = saleRepository.loadAll();
    }


    /**
     * Registers a new sale after validating the customer, seller,
     * products, and available stock.
     *
     * @param customerId the id of the customer making the purchase
     * @param sellerId the id of the seller handling the sale
     * @param productIds the ids of the products included in the sale
     * @return the newly created sale
     * @throws IllegalArgumentException if the customer, seller, or products
     *                                  are invalid or stock is insufficient
     */
    public Sale registerSale(String customerId, String sellerId, List<String> productIds) {

        Customer customer = personService.findCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found.");
        }

        Seller seller = personService.findSellerById(sellerId);
        if (seller == null) {
            throw new IllegalArgumentException("Seller not found.");
        }

        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "A sale must contain at least one product."
            );
        }

        Map<String, Integer> quantities = new HashMap<>();

        for (String productId : productIds) {
            quantities.put(
                    productId,
                    quantities.getOrDefault(productId, 0) + 1
            );
        }

        List<Product> products = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {

            String productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = findProductById(productId);

            if (product == null) {
                throw new IllegalArgumentException(
                        "Product not found: " + productId
                );
            }

            if (!productService.hasSufficientStock(productId, quantity)) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product: " + productId
                );
            }

            for (int i = 0; i < quantity; i++) {
                products.add(product);
            }
        }

        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            productService.reduceStock(
                    entry.getKey(),
                    entry.getValue()
            );
        }

        Sale sale = new Sale(
                LocalDateTime.now(),
                customer,
                seller,
                products
        );

        sales.add(sale);
        saleRepository.saveAll(sales);

        return sale;
    }
    /**
     * Finds a product by its id.
     *
     * @param id the id of the product to find
     * @return the matching product, or null if not found
     */
    private Product findProductById(String id) {
        for (Product product : productService.listAllProducts()) {
            if (product.getId().equals(id)) {
                return product;
            }
        }

        return null;
    }
    /**
     * Returns all registered sales.
     *
     * @return the complete list of sales
     */
    public List<Sale> listAllSales() {
        return sales;
    }
    /**
     * Returns all sales made by a specific customer.
     *
     * @param customerId the id of the customer
     * @return the sales associated with the customer
     */
    public List<Sale> listCustomerSales(String customerId) {
        List<Sale> customerSales = new ArrayList<>();

        for (Sale sale : sales) {
            if (sale.getCustomer().getId().equals(customerId)) {
                customerSales.add(sale);
            }
        }

        return customerSales;
    }
    /**
     * Returns all sales handled by a specific seller.
     *
     * @param sellerId the id of the seller
     * @return the sales associated with the seller
     */
    public List<Sale> listSellerSales(String sellerId) {
        List<Sale> sellerSales = new ArrayList<>();

        for (Sale sale : sales) {
            if (sale.getSeller().getId().equals(sellerId)) {
                sellerSales.add(sale);
            }
        }

        return sellerSales;
    }
}