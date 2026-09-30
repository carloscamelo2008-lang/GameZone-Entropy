# Promotion Analysis

## Purpose

The promotion module defines the different discount strategies available in GameZone Unicesar and centralizes the selection of the best applicable promotion for a sale.

## Promotion

`Promotion` is an abstract class that represents a generic promotional campaign.

It contains:

- `id`
- `name`
- `startDate`
- `endDate`

Its main responsibilities are:

- determining whether a promotion is active on a given date;
- defining the polymorphic `calculateDiscount(Sale sale)` operation.

## Promotion Types

### PercentageDiscount

Applies a percentage discount to the total value of the sale.

Formula:

`total × percentage / 100`

### CategoryDiscount

Applies a percentage discount only to products belonging to a target category.

Supported categories:

- `VIDEOGAME`
- `CONSOLE`
- `ACCESSORY`

Video games and consoles are identified by their concrete types.

For `ACCESSORY`, the current implementation uses the fallback:

`not VideoGame and not Console`

This is a known limitation because any future product type different from `VideoGame` and `Console` would also be treated as an accessory. The fallback should later be replaced by an explicit `instanceof Accessory` check.

### BulkPurchaseDiscount

Applies a percentage discount when the sale contains at least the configured minimum quantity of products.

## PromotionService

`PromotionService` coordinates the promotion module.

Its responsibilities include:

- registering percentage promotions;
- registering category promotions;
- registering bulk-purchase promotions;
- listing all promotions;
- listing active promotions;
- finding promotions by identifier;
- selecting the best applicable promotion for a sale.

`findBestPromotionFor(Sale sale)` evaluates active promotions using the sale date and returns the promotion with the highest monetary discount.

## Integration Considerations

The promotion module is integrated with `SaleService`.

During sale registration, the subtotal is calculated first. The selected promotion is then applied to that subtotal before extended warranty costs are added to obtain the final sale total.