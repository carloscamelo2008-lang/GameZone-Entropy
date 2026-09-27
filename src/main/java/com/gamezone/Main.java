package com.gamezone;

import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.ui.UI;
import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.service.AccessoryService;
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
        AccessoryRepository accessoryRepository =
                new AccessoryRepository(
                        "data/accessories.csv"
                );

        AccessoryService accessoryService =
                new AccessoryService(accessoryRepository);


        SaleService saleService =
                new SaleService(
                        saleRepository,
                        productService,
                        accessoryService,
                        personService
                );
        UI ui = new UI(personService, productService, saleService);
        ui.start();
    }
}
