package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductRepository;

import java.util.List;

/**
 * Provides business logic for managing products in the store,
 * including registration, inventory queries, and stock control.
 * This is the only class authorized to interact with
 * {@link ProductRepository}.
 */
public class ProductService {
    private ProductRepository repository;
    private List<Product> products;

    /**
     * Creates a new product service backed by the given repository,
     * loading any previously stored products.
     *
     * @param repository the repository used for persistence
     */
    public ProductService(ProductRepository repository){
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
     * Checks whether a product has enough stock to satisfy the
     * requested quantity.
     *
     * @param id       the id of the product to check
     * @param quantity the quantity requested
     * @return true if the product exists and has sufficient stock
     */
    public boolean hasSufficientStock(String id,int quantity){
        for (Product product : products){
            if (product.getId().equals(id)){
                return product.getStock() >= quantity;
            }
        }
        return false;
    }

    /**
     * Reduces the stock of a product by the given quantity.
     * If the product does not exist, no changes are made.
     *
     * @param id       the id of the product to update
     * @param quantity the quantity to subtract from stock
     */
    public void reduceStock(String id,int quantity){
        for (Product product : products) {
            if (product.getId().equals(id)) {
                product.setStock(product.getStock() - quantity);
                repository.saveAll(products);
                return;
            }
        }
    }

    /**
     * Registers a new video game, adding it to the managed list and
     * persisting the updated inventory.
     *
     * @param id        unique identifier of the video game
     * @param title     display name of the video game
     * @param price     unit price
     * @param stock     initial quantity in inventory
     * @param platform  platform the video game is developed for
     * @param genre     genre of the video game
     * @param ageRating recommended age rating
     * @return the newly created video game
     */
    public VideoGame registerVideoGame(String id,String title,double price,int stock,String platform,String genre, String ageRating){
        VideoGame videoGame = new VideoGame(id,title,price,stock,platform,genre, ageRating);
        products.add(videoGame);
        repository.saveAll(products);
        return videoGame;
    }


    /**
     * Registers a new console, adding it to the managed list and
     * persisting the updated inventory.
     *
     * @param id         unique identifier of the console
     * @param title      display name of the console
     * @param brand      manufacturer brand of the console
     * @param model      model name of the console
     * @param generation console generation
     * @param price      unit price
     * @param stock      initial quantity in inventory
     * @return the newly created console
     */
    public Console registerConsole(String id,String title,String brand, String model, String generation,double price,int stock){
        Console console = new Console(id, title, price, stock, brand, model, generation);
        products.add(console);
        repository.saveAll(products);
        return console;
    }
}
