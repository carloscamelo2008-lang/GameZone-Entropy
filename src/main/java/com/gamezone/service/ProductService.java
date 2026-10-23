package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.validation.ProductValidator;

import java.util.List;

/**
 * Provides business logic for managing products in the store, including
 * registration, inventory queries, and stock control. This is the only class
 * authorized to interact with {@link ProductRepository}.
 */
public class ProductService {

    private ProductRepository repository;
    private List<Product> products;

    /**
     * Creates a new product service backed by the given repository, loading any
     * previously stored products.
     *
     * @param repository the repository used for persistence
     * @throws com.gamezone.exceptions.PersistenceException if the products file
     * cannot be read
     */
    public ProductService(ProductRepository repository) {
        this.repository = repository;
        this.products = repository.loadAll();
    }

    /**
     * Returns the complete list of products currently managed.
     *
     * @return the list of all products
     */
    public List<Product> listAllProducts() {
        return products;
    }

    /**
     * Checks whether a product has enough stock to satisfy the requested
     * quantity.
     *
     * @param id the id of the product to check
     * @param quantity the quantity requested
     * @return true if the product exists and has sufficient stock
     */
    public boolean hasSufficientStock(String id, int quantity) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product.getStock() >= quantity;
            }
        }
        return false;
    }

    /**
     * Reduces the stock of a product by the given quantity.
     *
     * @param id the id of the product to update
     * @param quantity the quantity to subtract from stock
     * @throws com.gamezone.exceptions.ResourceNotFoundException if the product
     * does not exist
     * @throws com.gamezone.exceptions.PersistenceException if the products file
     * cannot be written
     */
    public void reduceStock(String id, int quantity) {
        Product product = findProductById(id);
        ProductValidator.validateExists(product, id);
        product.setStock(product.getStock() - quantity);
        repository.saveAll(products);
    }

    /**
     * Restores the stock of a product by the given quantity.
     *
     * @param id the id of the product to update
     * @param quantity the quantity to add to stock
     * @throws com.gamezone.exceptions.ResourceNotFoundException if the product
     * does not exist
     * @throws com.gamezone.exceptions.PersistenceException if the products file
     * cannot be written
     */
    public void restoreStock(String id, int quantity) {
        Product product = findProductById(id);
        ProductValidator.validateExists(product, id);
        product.setStock(product.getStock() + quantity);
        repository.saveAll(products);
    }

    /**
     * Registers a new video game, adding it to the managed list and persisting
     * the updated inventory.
     *
     * @param id unique identifier of the video game
     * @param title display name of the video game
     * @param price unit price
     * @param stock initial quantity in inventory
     * @param platform platform the video game is developed for
     * @param genre genre of the video game
     * @param ageRating recommended age rating
     * @return the newly created video game
     * @throws com.gamezone.exceptions.InvalidDataException if any field is
     * invalid
     * @throws com.gamezone.exceptions.PersistenceException if the products file
     * cannot be written
     */
    public VideoGame registerVideoGame(String id, String title, double price, int stock, String platform, String genre, String ageRating) {
        Product existing = findProductById(id);
        ProductValidator.validateIdAvailable(existing, id);
        ProductValidator.validateVideoGame(id, title, price, stock, platform, genre, ageRating);

        VideoGame videoGame = new VideoGame(id, title, price, stock, platform, genre, ageRating);

        products.add(videoGame);
        repository.saveAll(products);
        return videoGame;
    }

    /**
     * Registers a new console, adding it to the managed list and persisting the
     * updated inventory.
     *
     * @param id unique identifier of the console
     * @param title display name of the console
     * @param brand manufacturer brand of the console
     * @param model model name of the console
     * @param generation console generation
     * @param price unit price
     * @param stock initial quantity in inventory
     * @return the newly created console
     * @throws com.gamezone.exceptions.InvalidDataException if any field is
     * invalid
     * @throws com.gamezone.exceptions.PersistenceException if the products file
     * cannot be written
     */
    public Console registerConsole(String id, String title, String brand, String model, String generation, double price, int stock) {
        Product existing = findProductById(id);
        ProductValidator.validateIdAvailable(existing, id);
        ProductValidator.validateConsole(id, title, brand, model, generation, price, stock);

        Console console = new Console(id, title, price, stock, brand, model, generation);

        products.add(console);
        repository.saveAll(products);
        return console;
    }

    /**
     * Finds a product by its id.
     *
     * @param id the id of the product to find
     * @return the matching product, or null if it does not exist
     */
    private Product findProductById(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }
}
