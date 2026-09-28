package com.gamezone;

import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.PromotionRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
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

        AccessoryRepository accessoryRepository =
                new AccessoryRepository(
                        "data/accessories.csv"
                );

        AccessoryService accessoryService =
                new AccessoryService(accessoryRepository);

        PromotionRepository promotionRepository =
                new PromotionRepository("data/promotions.csv");

        PromotionService promotionService =
                new PromotionService(promotionRepository);

        WarrantyRepository warrantyRepository =
                new WarrantyRepository("data/warranties.csv");

        WarrantyService warrantyService =
                new WarrantyService(
                        warrantyRepository,
                        saleRepository,
                        productService
                );

        SaleService saleService =
                new SaleService(
                        saleRepository,
                        productService,
                        accessoryService,
                        personService,
                        promotionService,
                        warrantyService
                );

        // TODO: wire warrantyService into UI/ConsoleMenu once the warranty
        // management submenu is added (Líder Técnico, per Requirement 4).
        UI ui = new UI(
                personService,
                productService,
                accessoryService,
                saleService
        );

        ui.start();
    }
}