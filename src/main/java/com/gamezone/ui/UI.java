package com.gamezone.ui;

import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import java.util.List;
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
                case "1":
                    registerVideoGame();
                    break;

                case "2":
                    registerConsole();
                    break;

                case "3":
                    listProducts();
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
                case "7":
                    registerSale();
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
     * Displays a form to register a new video game.
     */
    private void registerVideoGame() {
        System.out.println("\n===== REGISTRAR VIDEOJUEGO =====");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Nombre: ");
        String title = scanner.nextLine();

        System.out.print("Precio: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());

        System.out.print("Plataforma: ");
        String platform = scanner.nextLine();

        System.out.print("Género: ");
        String genre = scanner.nextLine();

        System.out.print("Clasificación por edad: ");
        String ageRating = scanner.nextLine();

        try {
            productService.registerVideoGame(
                    id,
                    title,
                    price,
                    stock,
                    platform,
                    genre,
                    ageRating
            );

            System.out.println("Videojuego registrado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar el videojuego: " + e.getMessage());
        }
    }
    /**
     * Displays a form to register a new console.
     */
    private void registerConsole() {
        System.out.println("\n===== REGISTRAR CONSOLA =====");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Nombre: ");
        String title = scanner.nextLine();

        System.out.print("Marca: ");
        String brand = scanner.nextLine();

        System.out.print("Modelo: ");
        String model = scanner.nextLine();

        System.out.print("Generación: ");
        String generation = scanner.nextLine();

        System.out.print("Precio: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());

        try {
            productService.registerConsole(
                    id,
                    title,
                    brand,
                    model,
                    generation,
                    price,
                    stock
            );

            System.out.println("Consola registrada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar la consola: " + e.getMessage());
        }
    }
    /**
     * Displays all registered products.
     */
    private void listProducts() {
        System.out.println("\n===== PRODUCTOS =====");

        try {
            List<Product> products = productService.listAllProducts();

            if (products.isEmpty()) {
                System.out.println("No hay productos registrados.");
                return;
            }

            for (Product product : products) {
                System.out.println(
                        "ID: " + product.getId()
                                + " | Nombre: " + product.getTitle()
                                + " | Precio: $" + product.getPrice()
                                + " | Stock: " + product.getStock()
                );
            }
        } catch (Exception e) {
            System.out.println("Error al listar los productos: " + e.getMessage());
        }
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
    /**
     * Registers a new sale using customer, seller, and selected products.
     */
    private void registerSale() {
        System.out.println("\n===== REGISTRAR VENTA =====");

        System.out.print("ID del cliente: ");
        String customerId = scanner.nextLine();

        System.out.print("ID del vendedor: ");
        String sellerId = scanner.nextLine();

        List<String> productIds = new java.util.ArrayList<>();

        System.out.println("\nIngrese los productos de la venta.");
        System.out.println("Escriba FIN cuando haya terminado.");

        while (true) {
            System.out.print("ID del producto: ");
            String productId = scanner.nextLine();

            if (productId.equalsIgnoreCase("FIN")) {
                break;
            }

            if (productId.isBlank()) {
                System.out.println("El ID del producto no puede estar vacío.");
                continue;
            }

            productIds.add(productId);
        }

        if (productIds.isEmpty()) {
            System.out.println("La venta debe contener al menos un producto.");
            return;
        }

        try {
            Sale sale = saleService.registerSale(
                    customerId,
                    sellerId,
                    productIds
            );

            System.out.println("Venta registrada correctamente.");
            System.out.println("Fecha: " + sale.getDate());
            System.out.println("Cliente: " + sale.getCustomer().getName());
            System.out.println("Vendedor: " + sale.getSeller().getName());
            System.out.println("Total: $" + sale.calculateTotal());

        } catch (Exception e) {
            System.out.println("Error al registrar la venta: " + e.getMessage());
        }
    }
}