package com.gamezone.ui;

import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.model.Product;
import com.gamezone.model.Console;
import com.gamezone.model.Sale;
import java.util.List;
import java.util.Scanner;
import com.gamezone.model.Accessory;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.ReturnService;
import com.gamezone.exceptions.GameZoneException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.PersistenceException;

/**
 * Provides the console-based user interface for GameZone.
 */
public class UI {

    private final PersonService personService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final SaleService saleService;
    private final Scanner scanner;
    private final ReturnService returnService;

    /**
     * Creates a new user interface.
     *
     * @param personService service used to manage customers and sellers
     * @param productService service used to manage products
     * @param saleService service used to manage sales
     * @param accessoryService service used to manage accessories
     * @param returnService service used to calculate returns and monthly balance
     */
    public UI(
            PersonService personService,
            ProductService productService,
            AccessoryService accessoryService,
            SaleService saleService,
            ReturnService returnService) {

        this.personService = personService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.saleService = saleService;
        this.scanner = new Scanner(System.in);
        this.returnService = returnService;
    }

    /**
     * Starts the console user interface.
     */
    public void start() {
        boolean running = true;

        while (running) {
            showMenu();

            String option = scanner.nextLine();

            try {
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

                    case "12":
                        showMonthlyBalance();
                        break;

                    default:
                        System.out.println(
                                "Opción no válida. Intente nuevamente."
                        );
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println(
                        "Entrada numérica inválida. "
                                + "Ingrese un número válido e inténtelo de nuevo."
                );
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
        System.out.println("12. Balance mensual");
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
        } catch (ResourceNotFoundException e) {
            printError("registrar el videojuego", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("registrar el videojuego", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("registrar el videojuego", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("registrar el videojuego", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("registrar el videojuego", "Error general de la aplicación", e);
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
        } catch (ResourceNotFoundException e) {
            printError("registrar la consola", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("registrar la consola", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("registrar la consola", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("registrar la consola", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("registrar la consola", "Error general de la aplicación", e);
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
        } catch (ResourceNotFoundException e) {
            printError("listar los productos", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("listar los productos", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("listar los productos", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("listar los productos", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("listar los productos", "Error general de la aplicación", e);
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
        } catch (ResourceNotFoundException e) {
            printError("registrar el cliente", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("registrar el cliente", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("registrar el cliente", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("registrar el cliente", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("registrar el cliente", "Error general de la aplicación", e);
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
     * Registers a new sale using customer, seller, selected products,
     * and optional extended warranties for consoles.
     */
    private void registerSale() {
        System.out.println("\n===== REGISTRAR VENTA =====");

        System.out.print("ID del cliente: ");
        String customerId = scanner.nextLine();

        System.out.print("ID del vendedor: ");
        String sellerId = scanner.nextLine();

        List<String> productIds = new java.util.ArrayList<>();
        List<String> productIdsWithExtendedWarranty = new java.util.ArrayList<>();

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

        for (String productId : productIds) {

            Product product = null;

            for (Product availableProduct : productService.listAllProducts()) {
                if (availableProduct.getId().equals(productId)) {
                    product = availableProduct;
                    break;
                }
            }

            if (product instanceof Console) {

                System.out.print(
                        "¿Desea garantía extendida para la consola "
                                + product.getTitle() + "? (s/n): "
                );

                String answer = scanner.nextLine();

                if (answer.equalsIgnoreCase("s")) {
                    productIdsWithExtendedWarranty.add(productId);
                }
            }
        }

        try {
            Sale sale = saleService.registerSale(
                    customerId,
                    sellerId,
                    productIds,
                    productIdsWithExtendedWarranty
            );

            System.out.println("Venta registrada correctamente.");
            System.out.println("Fecha: " + sale.getDate());
            System.out.println("Cliente: " + sale.getCustomer().getName());
            System.out.println("Vendedor: " + sale.getSeller().getName());

            System.out.println();
            System.out.println(sale.generateReceipt());

        } catch (ResourceNotFoundException e) {
            printError("registrar la venta", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("registrar la venta", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("registrar la venta", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("registrar la venta", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("registrar la venta", "Error general de la aplicación", e);
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
        } catch (ResourceNotFoundException e) {
            printError("listar el historial de ventas", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("listar el historial de ventas", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("listar el historial de ventas", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("listar el historial de ventas", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("listar el historial de ventas", "Error general de la aplicación", e);
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
        } catch (ResourceNotFoundException e) {
            printError("consultar el historial del cliente", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("consultar el historial del cliente", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("consultar el historial del cliente", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("consultar el historial del cliente", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("consultar el historial del cliente", "Error general de la aplicación", e);
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
        } catch (ResourceNotFoundException e) {
            printError("consultar el historial del vendedor", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("consultar el historial del vendedor", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("consultar el historial del vendedor", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("consultar el historial del vendedor", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("consultar el historial del vendedor", "Error general de la aplicación", e);
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
        String priceInput = scanner.nextLine();

        System.out.print("Stock: ");
        String stockInput = scanner.nextLine();

        System.out.print("Tipo de conexión: ");
        String connectionType = scanner.nextLine();

        try {
            double price = Double.parseDouble(priceInput);
            int stock = Integer.parseInt(stockInput);

            accessoryService.registerController(
                    id,
                    title,
                    price,
                    stock,
                    connectionType
            );

            System.out.println("Controlador registrado correctamente.");
        } catch (ResourceNotFoundException e) {
            printError("registrar el controlador", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("registrar el controlador", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("registrar el controlador", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("registrar el controlador", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("registrar el controlador", "Error general de la aplicación", e);
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
        String priceInput = scanner.nextLine();

        System.out.print("Stock: ");
        String stockInput = scanner.nextLine();

        System.out.print("Longitud en metros: ");
        String lengthInput = scanner.nextLine();

        System.out.print("Tipo de conector: ");
        String connectorType = scanner.nextLine();

        try {
            double price = Double.parseDouble(priceInput);
            int stock = Integer.parseInt(stockInput);
            double lengthInMeters = Double.parseDouble(lengthInput);

            accessoryService.registerCable(
                    id,
                    title,
                    price,
                    stock,
                    lengthInMeters,
                    connectorType
            );

            System.out.println("Cable registrado correctamente.");
        } catch (ResourceNotFoundException e) {
            printError("registrar el cable", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("registrar el cable", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("registrar el cable", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("registrar el cable", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("registrar el cable", "Error general de la aplicación", e);
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
        String priceInput = scanner.nextLine();

        System.out.print("Stock: ");
        String stockInput = scanner.nextLine();

        System.out.print("Capacidad en GB: ");
        String capacityInput = scanner.nextLine();

        System.out.print("Tipo de memoria: ");
        String memoryType = scanner.nextLine();

        try {
            double price = Double.parseDouble(priceInput);
            int stock = Integer.parseInt(stockInput);
            int capacityInGb = Integer.parseInt(capacityInput);

            accessoryService.registerMemory(
                    id,
                    title,
                    price,
                    stock,
                    capacityInGb,
                    memoryType
            );

            System.out.println("Memoria registrada correctamente.");
        } catch (ResourceNotFoundException e) {
            printError("registrar la memoria", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("registrar la memoria", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("registrar la memoria", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("registrar la memoria", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("registrar la memoria", "Error general de la aplicación", e);
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
                System.out.println(accessory.getDescription());
            }
        } catch (ResourceNotFoundException e) {
            printError("listar los accesorios", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("listar los accesorios", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("listar los accesorios", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("listar los accesorios", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("listar los accesorios", "Error general de la aplicación", e);
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
                System.out.println(accessory.getDescription());
            }
        } catch (ResourceNotFoundException e) {
            printError("filtrar los accesorios", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("filtrar los accesorios", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("filtrar los accesorios", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("filtrar los accesorios", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("filtrar los accesorios", "Error general de la aplicación", e);
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
                System.out.println(accessory.getDescription());
            }
        } catch (ResourceNotFoundException e) {
            printError("consultar compatibilidad", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("consultar compatibilidad", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("consultar compatibilidad", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("consultar compatibilidad", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("consultar compatibilidad", "Error general de la aplicación", e);
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
        } catch (ResourceNotFoundException e) {
            printError("asignar la consola compatible", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("asignar la consola compatible", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("asignar la consola compatible", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("asignar la consola compatible", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("asignar la consola compatible", "Error general de la aplicación", e);
        }
    }
    /**
     * Displays the monthly sales, returns, and net balance.
     */
    private void showMonthlyBalance() {
        System.out.println("\n===== BALANCE MENSUAL =====");

        System.out.print("Mes (1-12): ");
        int month = Integer.parseInt(scanner.nextLine());

        System.out.print("Año: ");
        int year = Integer.parseInt(scanner.nextLine());

        try {
            double monthlySales = returnService.calculateMonthlySales(month, year);
            double monthlyReturns = returnService.calculateMonthlyReturns(month, year);
            double monthlyBalance = returnService.generateMonthlyBalance(month, year);

            System.out.println("Total de ventas: $" + monthlySales);
            System.out.println("Total de devoluciones: $" + monthlyReturns);
            System.out.println("Balance neto: $" + monthlyBalance);

        } catch (ResourceNotFoundException e) {
            printError("generar el balance mensual", "Recurso no encontrado", e);
        } catch (InvalidDataException e) {
            printError("generar el balance mensual", "Datos inválidos", e);
        } catch (BusinessRuleException e) {
            printError("generar el balance mensual", "Regla del negocio incumplida", e);
        } catch (PersistenceException e) {
            printError("generar el balance mensual", "Error de persistencia", e);
        } catch (GameZoneException e) {
            printError("generar el balance mensual", "Error general de la aplicación", e);
        }
    }
    /**
     * Prints a Spanish error message according to the exception category.
     *
     * @param operation operation being performed
     * @param category category of the application exception
     * @param exception exception raised by the application
     */
    private void printError(
            String operation,
            String category,
            GameZoneException exception) {
        System.out.println(
                "Error al " + operation + ". "
                        + category + ": " + exception.getMessage()
        );
    }

}
