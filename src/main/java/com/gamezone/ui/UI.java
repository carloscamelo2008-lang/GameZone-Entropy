package com.gamezone.ui;

import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.util.Scanner;

/**
 * Provides the console-based user interface for GameZone.
 */
public class UI {

    private final PersonService personService;
    private final ProductService productService;
    private final SaleService saleService;
    private final Scanner scanner;

    /**
     * Creates a new user interface.
     *
     * @param personService service used to manage customers and sellers
     * @param productService service used to manage products
     * @param saleService service used to manage sales
     */
    public UI(
            PersonService personService,
            ProductService productService,
            SaleService saleService) {

        this.personService = personService;
        this.productService = productService;
        this.saleService = saleService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the console user interface.
     */
    public void start() {
        boolean running = true;

        while (running) {
            showMenu();

            String option = scanner.nextLine();

            switch (option) {
                case "0":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                case "6":
                    listSellers();
                    break;
            }
        }
    }

    /**
     * Displays the main menu options.
     */
    private void showMenu() {
        System.out.println();
        System.out.println("===== GAMEZONE =====");
        System.out.println("1. Register video game");
        System.out.println("2. Register console");
        System.out.println("3. List products");
        System.out.println("4. Register customer");
        System.out.println("5. List customers");
        System.out.println("6. List sellers");
        System.out.println("7. Register sale");
        System.out.println("8. List all sales");
        System.out.println("9. List customer sales");
        System.out.println("10. List seller sales");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
    }
    /**
     * Displays all registered sellers.
     */
    private void listSellers() {
        System.out.println("\n===== SELLERS =====");

        for (var seller : personService.listSellers()) {
            System.out.println(
                    "ID: " + seller.getId()
                            + " | Name: " + seller.getName()
                            + " | Phone: " + seller.getPhone()
                            + " | Employee Code: " + seller.getEmployeeCode()
                            + " | Shift: " + seller.getShift()
            );
        }
    }
}