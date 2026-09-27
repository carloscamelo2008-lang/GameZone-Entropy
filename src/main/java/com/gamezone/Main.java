package com.gamezone;

import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;
import com.gamezone.ui.UI;
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

        // WarrantyRepository resolves Sale references through SaleRepository
        // (not SaleService), so it can be constructed here without creating
        // a circular dependency with SaleService.
        WarrantyRepository warrantyRepository =
                new WarrantyRepository(
                        "data/warranties.csv",
                        saleRepository,
                        productService
                );

        WarrantyService warrantyService =
                new WarrantyService(warrantyRepository);

        // TODO: wire warrantyService into UI/ConsoleMenu once the warranty
        // management submenu is added (Líder Técnico, per Requirement 4).
        UI ui = new UI(personService, productService, saleService);
        ui.start();
    }
}
