package com.gamezone.service;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.persistence.PromotionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides business operations for managing promotions: registration,
 * queries by validity, and selection of the best applicable promotion
 * for a given sale. Delegates persistence to PromotionRepository.
 */
public class PromotionService {

    private PromotionRepository promotionRepository;
    private List<Promotion> promotions;

    /**
     * Creates the service and loads the currently persisted promotions.
     *
     * @param promotionRepository the repository used for persistence
     */
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
        this.promotions = promotionRepository.loadAll();
    }

    /**
     * Registers a new percentage discount promotion and persists the
     * updated list.
     *
     * @param id         the promotion id
     * @param name       the promotion name
     * @param startDate  the date the promotion becomes active
     * @param endDate    the date the promotion stops being active
     * @param percentage the discount percentage (between 0 and 100)
     * @return the newly registered promotion
     */
    public PercentageDiscount registerPercentageDiscount(String id, String name, LocalDate startDate,
                                                          LocalDate endDate, double percentage) {
        PercentageDiscount promotion = new PercentageDiscount(id, name, startDate, endDate, percentage);
        promotions.add(promotion);
        promotionRepository.saveAll(promotions);
        return promotion;
    }

    /**
     * Registers a new category discount promotion and persists the
     * updated list.
     *
     * @param id             the promotion id
     * @param name           the promotion name
     * @param startDate      the date the promotion becomes active
     * @param endDate        the date the promotion stops being active
     * @param percentage     the discount percentage (between 0 and 100)
     * @param targetCategory the target category ("VIDEOGAME" or "CONSOLE")
     * @return the newly registered promotion
     */
    public CategoryDiscount registerCategoryDiscount(String id, String name, LocalDate startDate,
                                                      LocalDate endDate, double percentage, String targetCategory) {
        if (!"VIDEOGAME".equalsIgnoreCase(targetCategory) && !"CONSOLE".equalsIgnoreCase(targetCategory)
                && !"ACCESSORY".equalsIgnoreCase(targetCategory)) {
            throw new IllegalArgumentException("La categoría objetivo debe ser VIDEOGAME, CONSOLE o ACCESSORY.");
        }
        CategoryDiscount promotion = new CategoryDiscount(id, name, startDate, endDate, percentage, targetCategory);
        promotions.add(promotion);
        promotionRepository.saveAll(promotions);
        return promotion;
    }

    /**
     * Registers a new bulk purchase discount promotion and persists
     * the updated list.
     *
     * @param id              the promotion id
     * @param name            the promotion name
     * @param startDate       the date the promotion becomes active
     * @param endDate         the date the promotion stops being active
     * @param minimumQuantity the minimum number of products required
     * @param percentage      the discount percentage (between 0 and 100)
     * @return the newly registered promotion
     */
    public BulkPurchaseDiscount registerBulkPurchaseDiscount(String id, String name, LocalDate startDate,
                                                              LocalDate endDate, int minimumQuantity, double percentage) {
        BulkPurchaseDiscount promotion = new BulkPurchaseDiscount(id, name, startDate, endDate, minimumQuantity, percentage);
        promotions.add(promotion);
        promotionRepository.saveAll(promotions);
        return promotion;
    }

    /**
     * Returns every promotion currently registered.
     *
     * @return the list of all promotions
     */
    public List<Promotion> listAllPromotions() {
        return promotions;
    }

    /**
     * Returns the promotions that are active on the current date.
     *
     * @return the list of currently active promotions
     */
    public List<Promotion> listActivePromotions() {
        List<Promotion> active = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Promotion promotion : promotions) {
            if (promotion.isActive(today)) {
                active.add(promotion);
            }
        }
        return active;
    }

    /**
     * Among the promotions active on the sale's date, finds the one
     * that grants the largest monetary discount to the given sale.
     *
     * @param sale the sale to evaluate
     * @return the best applicable promotion, or null if none applies
     *         or the maximum discount would be zero
     */
    public Promotion findBestPromotionFor(Sale sale) {
        LocalDate saleDate = sale.getDate().toLocalDate();
        Promotion best = null;
        double bestDiscount = 0.0;

        for (Promotion promotion : promotions) {
            if (promotion.isActive(saleDate)) {
                double discount = promotion.calculateDiscount(sale);
                if (discount > bestDiscount) {
                    bestDiscount = discount;
                    best = promotion;
                }
            }
        }

        return best;
    }

    /**
     * Finds a promotion by its id.
     *
     * @param id the promotion id
     * @return the matching promotion, or null if none is found
     */
    public Promotion findById(String id) {
        for (Promotion promotion : promotions) {
            if (promotion.getId().equals(id)) {
                return promotion;
            }
        }
        return null;
    }
}
