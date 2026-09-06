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
                case "4":
                    registerCustomer();
                    break;
                case "5":
                    listCustomers();
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
        System.out.println("1. Registrar videojuego");
        System.out.println("2. Registrar consola");
        System.out.println("3. Listar productos");
        System.out.println("4. Registrar cliente");
        System.out.println("5. Listar clientes");
        System.out.println("6. Listar vendedores");
        System.out.println("7. Registrar venta");
        System.out.println("8. Listar historial de ventas");
        System.out.println("9. Historial de compras de cliente");
        System.out.println("10. Historial de ventas de vendedor");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Displays all registered sellers.
     */
    private void listSellers() {
        System.out.println("\n===== VENDEDORES =====");

        for (var seller : personService.listSellers()) {
            System.out.println(
                    "ID: " + seller.getId()
                            + " | Nombre: " + seller.getName()
                            + " | Teléfono: " + seller.getPhone()
                            + " | Código de empleado: " + seller.getEmployeeCode()
                            + " | Turno: " + seller.getShift()
            );
        }
    }
    /**
     * Registers a new customer using data entered through the console.
     */
    private void registerCustomer() {
        System.out.println("\n===== REGISTRAR CLIENTE =====");

        System.out.print("Identificación: ");
        String id = scanner.nextLine();

        System.out.print("Nombre: ");
        String name = scanner.nextLine();

        System.out.print("Teléfono: ");
        String phone = scanner.nextLine();

        System.out.print("Correo electrónico: ");
        String email = scanner.nextLine();

        try {
            personService.registerCustomer(id, name, phone, email);
            System.out.println("Cliente registrado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar el cliente: " + e.getMessage());
        }
    }
    /**
     * Displays all registered customers.
     */
    private void listCustomers() {
        System.out.println("\n===== CLIENTES =====");

        for (var customer : personService.listCustomers()) {
            System.out.println(
                    "ID: " + customer.getId()
                            + " | Nombre: " + customer.getName()
                            + " | Teléfono: " + customer.getPhone()
                            + " | Correo: " + customer.getEmail()
            );
        }
    }
}