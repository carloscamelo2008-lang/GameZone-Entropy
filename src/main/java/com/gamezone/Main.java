package com.gamezone;

import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

/**
 * Application entry point for the GameZone system.
 */
public class Main {

    /**
     * Starts the GameZone application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        PersonRepository personRepository =
                new PersonRepository(
                        "data/customers.csv",
                        "data/sellers.csv"
                );

        ProductRepository productRepository =
                new ProductRepository(
                        "data/products.dat"
                );

        SaleRepository saleRepository =
                new SaleRepository(
                        "data/sales.dat"
                );

        PersonService personService =
                new PersonService(personRepository);

        ProductService productService =
                new ProductService(productRepository);

        SaleService saleService =
                new SaleService(
                        saleRepository,
                        productService,
                        personService
                );

        System.out.println("GameZone started successfully.");
    }
}
