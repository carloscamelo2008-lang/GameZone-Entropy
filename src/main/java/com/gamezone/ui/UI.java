package com.gamezone.ui;

import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import java.util.List;
import java.util.Scanner;
import com.gamezone.model.Accessory;
import com.gamezone.service.AccessoryService;

/**
 * Provides the console-based user interface for GameZone.
 */
public class UI {

    private final PersonService personService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final SaleService saleService;
    private final Scanner scanner;

    /**
     * Creates a new user interface.
     *
     * @param personService service used to manage customers and sellers
     * @param productService service used to manage products
     * @param saleService service used to manage sales
     * @param accessoryService service used to manage accessories
     */
    public UI(
            PersonService personService,
            ProductService productService,
            AccessoryService accessoryService,
            SaleService saleService) {

        this.personService = personService;
        this.productService = productService;
        this.accessoryService = accessoryService;
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
                case "8":
                    listAllSales();
                    break;
                case "9":
                    listCustomerSales();
                    break;
                case "10":
                    listSellerSales();
                    break;
                case "11":
                    manageAccessories();
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
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
        System.out.println("11. Gestionar accesorios");
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
    /**
     * Displays the complete sales history.
     */
    private void listAllSales() {
        System.out.println("\n===== HISTORIAL DE VENTAS =====");

        try {
            List<Sale> sales = saleService.listAllSales();

            if (sales.isEmpty()) {
                System.out.println("No hay ventas registradas.");
                return;
            }

            for (Sale sale : sales) {
                System.out.println(
                        "Fecha: " + sale.getDate()
                                + " | Cliente: " + sale.getCustomer().getName()
                                + " | Vendedor: " + sale.getSeller().getName()
                                + " | Total: $" + sale.calculateTotal()
                );

                System.out.println("Productos:");

                for (Product product : sale.getProducts()) {
                    System.out.println(
                            "  - " + product.getTitle()
                                    + " | Precio: $" + product.getPrice()
                    );
                }

                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(
                    "Error al listar el historial de ventas: " + e.getMessage()
            );
        }
    }
    /**
     * Displays the sales history of a specific customer.
     */
    private void listCustomerSales() {
        System.out.println("\n===== HISTORIAL DE COMPRAS DEL CLIENTE =====");

        System.out.print("ID del cliente: ");
        String customerId = scanner.nextLine();

        try {
            List<Sale> customerSales = saleService.listCustomerSales(customerId);

            if (customerSales.isEmpty()) {
                System.out.println("El cliente no tiene compras registradas.");
                return;
            }

            for (Sale sale : customerSales) {
                System.out.println(
                        "Fecha: " + sale.getDate()
                                + " | Cliente: " + sale.getCustomer().getName()
                                + " | Vendedor: " + sale.getSeller().getName()
                                + " | Total: $" + sale.calculateTotal()
                );

                System.out.println("Productos:");

                for (Product product : sale.getProducts()) {
                    System.out.println(
                            "  - " + product.getTitle()
                                    + " | Precio: $" + product.getPrice()
                    );
                }

                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(
                    "Error al consultar el historial del cliente: "
                            + e.getMessage()
            );
        }
    }
    /**
     * Displays the sales history of a specific seller.
     */
    private void listSellerSales() {
        System.out.println("\n===== HISTORIAL DE VENTAS DEL VENDEDOR =====");

        System.out.print("ID del vendedor: ");
        String sellerId = scanner.nextLine();

        try {
            List<Sale> sellerSales = saleService.listSellerSales(sellerId);

            if (sellerSales.isEmpty()) {
                System.out.println("El vendedor no tiene ventas registradas.");
                return;
            }

            for (Sale sale : sellerSales) {
                System.out.println(
                        "Fecha: " + sale.getDate()
                                + " | Cliente: " + sale.getCustomer().getName()
                                + " | Vendedor: " + sale.getSeller().getName()
                                + " | Total: $" + sale.calculateTotal()
                );

                System.out.println("Productos:");

                for (Product product : sale.getProducts()) {
                    System.out.println(
                            "  - " + product.getTitle()
                                    + " | Precio: $" + product.getPrice()
                    );
                }

                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(
                    "Error al consultar el historial del vendedor: "
                            + e.getMessage()
            );
        }
    }
    /**
     * Displays the accessory management submenu.
     */
    private void manageAccessories() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("===== GESTIONAR ACCESORIOS =====");
            System.out.println("1. Registrar controlador");
            System.out.println("2. Registrar cable");
            System.out.println("3. Registrar memoria");
            System.out.println("4. Listar accesorios");
            System.out.println("5. Listar accesorios por tipo");
            System.out.println("6. Buscar accesorios compatibles");
            System.out.println("7. Asignar consola compatible");
            System.out.println("0. Volver");

            System.out.print("Seleccione una opción: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    registerController();
                    break;
                case "2":
                    registerCable();
                    break;
                case "3":
                    registerMemory();
                    break;
                case "4":
                    listAccessories();
                    break;
                case "5":
                    listAccessoriesByType();
                    break;
                case "6":
                    findCompatibleAccessories();
                    break;
                case "7":
                    assignCompatibleConsole();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        }
    }
    /**
     * Registers a controller through the console.
     */
    private void registerController() {
        System.out.println("\n===== REGISTRAR CONTROLADOR =====");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Nombre: ");
        String title = scanner.nextLine();

        System.out.print("Precio: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());

        System.out.print("Tipo de conexión: ");
        String connectionType = scanner.nextLine();

        try {
            accessoryService.registerController(
                    id,
                    title,
                    price,
                    stock,
                    connectionType
            );

            System.out.println("Controlador registrado correctamente.");
        } catch (Exception e) {
            System.out.println(
                    "Error al registrar el controlador: " + e.getMessage()
            );
        }
    }
    /**
     * Registers a cable through the console.
     */
    private void registerCable() {
        System.out.println("\n===== REGISTRAR CABLE =====");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Nombre: ");
        String title = scanner.nextLine();

        System.out.print("Precio: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());

        System.out.print("Longitud en metros: ");
        double lengthInMeters = Double.parseDouble(scanner.nextLine());

        System.out.print("Tipo de conector: ");
        String connectorType = scanner.nextLine();

        try {
            accessoryService.registerCable(
                    id,
                    title,
                    price,
                    stock,
                    lengthInMeters,
                    connectorType
            );

            System.out.println("Cable registrado correctamente.");
        } catch (Exception e) {
            System.out.println(
                    "Error al registrar el cable: " + e.getMessage()
            );
        }
    }
    /**
     * Registers a memory accessory through the console.
     */
    private void registerMemory() {
        System.out.println("\n===== REGISTRAR MEMORIA =====");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Nombre: ");
        String title = scanner.nextLine();

        System.out.print("Precio: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());

        System.out.print("Capacidad en GB: ");
        int capacityInGb = Integer.parseInt(scanner.nextLine());

        System.out.print("Tipo de memoria: ");
        String memoryType = scanner.nextLine();

        try {
            accessoryService.registerMemory(
                    id,
                    title,
                    price,
                    stock,
                    capacityInGb,
                    memoryType
            );

            System.out.println("Memoria registrada correctamente.");
        } catch (Exception e) {
            System.out.println(
                    "Error al registrar la memoria: " + e.getMessage()
            );
        }
    }
    /**
     * Displays all registered accessories.
     */
    private void listAccessories() {
        System.out.println("\n===== ACCESORIOS =====");

        try {
            List<Accessory> accessories = accessoryService.listAllAccessories();

            if (accessories.isEmpty()) {
                System.out.println("No hay accesorios registrados.");
                return;
            }

            for (Accessory accessory : accessories) {
                System.out.println(
                        "ID: " + accessory.getId()
                                + " | Nombre: " + accessory.getTitle()
                                + " | Precio: $" + accessory.getPrice()
                                + " | Stock: " + accessory.getStock()
                );
            }
        } catch (Exception e) {
            System.out.println(
                    "Error al listar los accesorios: " + e.getMessage()
            );
        }
    }
    /**
     * Displays accessories filtered by their concrete type.
     */
    private void listAccessoriesByType() {
        System.out.println("\n===== ACCESORIOS POR TIPO =====");

        System.out.print("Tipo (Controller, Cable o Memory): ");
        String type = scanner.nextLine();

        try {
            List<Accessory> accessories =
                    accessoryService.listAccessoriesByType(type);

            if (accessories.isEmpty()) {
                System.out.println("No hay accesorios de ese tipo.");
                return;
            }

            for (Accessory accessory : accessories) {
                System.out.println(
                        "ID: " + accessory.getId()
                                + " | Nombre: " + accessory.getTitle()
                                + " | Precio: $" + accessory.getPrice()
                                + " | Stock: " + accessory.getStock()
                );
            }
        } catch (Exception e) {
            System.out.println(
                    "Error al filtrar los accesorios: " + e.getMessage()
            );
        }
    }
    /**
     * Displays accessories compatible with a specific console.
     */
    private void findCompatibleAccessories() {
        System.out.println("\n===== ACCESORIOS COMPATIBLES =====");

        System.out.print("ID de la consola: ");
        String consoleId = scanner.nextLine();

        try {
            List<Accessory> accessories =
                    accessoryService.findAccessoriesCompatibleWith(consoleId);

            if (accessories.isEmpty()) {
                System.out.println(
                        "No se encontraron accesorios compatibles."
                );
                return;
            }

            for (Accessory accessory : accessories) {
                System.out.println(
                        "ID: " + accessory.getId()
                                + " | Nombre: " + accessory.getTitle()
                                + " | Precio: $" + accessory.getPrice()
                                + " | Stock: " + accessory.getStock()
                );
            }
        } catch (Exception e) {
            System.out.println(
                    "Error al consultar compatibilidad: " + e.getMessage()
            );
        }
    }
    /**
     * Assigns a compatible console to an accessory.
     */
    private void assignCompatibleConsole() {
        System.out.println("\n===== ASIGNAR CONSOLA COMPATIBLE =====");

        System.out.print("ID del accesorio: ");
        String accessoryId = scanner.nextLine();

        System.out.print("ID de la consola: ");
        String consoleId = scanner.nextLine();

        try {
            accessoryService.addCompatibleConsole(
                    accessoryId,
                    consoleId
            );

            System.out.println(
                    "Consola compatible asignada correctamente."
            );
        } catch (Exception e) {
            System.out.println(
                    "Error al asignar la consola compatible: "
                            + e.getMessage()
            );
        }
    }
}