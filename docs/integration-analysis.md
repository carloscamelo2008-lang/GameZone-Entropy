# Integration Analysis

## A1 — Accessory Category Discount

### Cause

The promotion module originally limited `CategoryDiscount` to the `VIDEOGAME` and `CONSOLE` categories. After integrating the accessory module, accessories also became part of the store catalog and sales flow, so the promotion system needed to support them as a valid category.

### Solution

`CategoryDiscount` was extended to recognize `ACCESSORY` as a valid target category and to identify instances of `Accessory` when calculating discounts.

`PromotionService.registerCategoryDiscount` was updated to accept only the three supported categories:

- `VIDEOGAME`
- `CONSOLE`
- `ACCESSORY`

`ConsoleMenu` was also updated to allow the user to select accessories when registering a category-based promotion.

The promotions data was extended with an accessory category promotion valid during the integration period.

---

## A2 — Warranty Circular Dependency

### Cause

The integration of the warranty module created a circular dependency between the sales and warranty components:

`SaleService → WarrantyService → WarrantyRepository → SaleService`

This prevented the application from constructing the services cleanly through constructor injection.

### Solution

`WarrantyRepository` was redesigned to persist and load only the identifiers required to reconstruct warranty references.

Instead of depending on `SaleService`, `WarrantyRepository` resolves sales through `SaleRepository`.

`WarrantyService` receives:

- `WarrantyRepository`
- `SaleRepository`
- `ProductService`

and resolves `Sale` and `Product` references from their identifiers.

`Main` was adjusted to construct the objects without the circular dependency.

The warranty class diagram was also updated to reflect the new dependency structure.

---

## A3 — Unified Sale Registration Flow

### Cause

The requirements for sales, promotions, accessories and warranties modified `SaleService.registerSale` independently. Once these modules were integrated, the order of operations became important because discounts must be calculated over the sale subtotal, while extended warranties must be added afterward.

### Solution

`SaleService.registerSale` was reorganized into a unified flow:

1. Validate that the sale contains at least one item.
2. Resolve each item as a product or accessory and validate its stock.
3. Create the sale and calculate the subtotal.
4. Find the best applicable promotion and calculate the discount only over the subtotal.
5. Generate the automatic basic warranty for each console and any requested extended warranties.
6. Calculate the final total as:

`subtotal - discount + extended warranty cost`

7. Update the inventory through `ProductService` or `AccessoryService`, depending on the item type.
8. Persist the sale and warranties.

`Sale.generateReceipt` was also updated to display:

- subtotal;
- discount and promotion name;
- extended warranty cost;
- final total.

The sales user interface was integrated so that products and accessories can be selected and an extended warranty can be requested for each console.

---

## A4 — Return Accessory Stock

### Cause

The return module originally restored stock only through `ProductService.restoreStock`. Since accessories are managed separately by `AccessoryService`, returning an accessory would not restore its inventory correctly.

### Solution

`AccessoryService` was extended with `restoreStock(String accessoryId, int quantity)`.

`ReturnService` was integrated with `AccessoryService` and now restores stock according to the runtime type of the returned item:

- regular products use `ProductService.restoreStock`;
- accessories use `AccessoryService.restoreStock`.

`ReturnRepository` was also updated to resolve returned item references from both `ProductService` and `AccessoryService`.

This allows returns containing products, accessories, or both to restore the corresponding inventory correctly.

---

## A5 — Discounted Return Refund

### Cause

The original return calculation refunded the complete list price of each returned product. When the original sale included a promotion, this could result in a refund greater than the amount actually paid for the returned item.

### Solution

`Return.calculateRefundAmount` now calculates the refund using the same proportional discount applied to the original sale.

The refund for each returned product is calculated as:

`price × (1 - discount / subtotal)`

The implementation also handles a zero subtotal safely by using a zero discount rate.

`Return.generateReturnReceipt` was updated to show, for each returned product:

- original list price;
- proportional discount;
- refunded amount.

The receipt also shows the promotion applied to the original sale and the total refund.

---

## A6 — Monthly Balance Report

### Cause

The original `generateMonthlyBalance` method only returned the net balance. The integrated system required the application to expose the total sales, total returns and net balance separately.

In addition, the total sales must represent the final amount of each sale, including promotions and extended warranties.

### Solution

`ReturnService` was extended with:

`calculateMonthlySales(int month, int year)`

`calculateMonthlyReturns(int month, int year)`

`calculateMonthlySales` uses `Sale.calculateFinalTotal()` so that the monthly sales total includes discounts and extended warranty costs.

`generateMonthlyBalance` keeps its original method signature and calculates:

`monthly sales - monthly returns`

The Technical Leader is responsible for integrating these three values into `ConsoleMenu`.

---

## A7 — Warranty Cancellation on Console Return

### Cause

The previous requirements did not define what should happen to warranties when a console was returned. In the integrated system, a returned console must not keep an active warranty associated with the original sale.

### Solution

`WarrantyService` was extended with:

`cancelWarranties(String productId, String saleId)`

This method removes the warranties associated with the specified product and sale and returns the refundable warranty cost.

The refundable amount is determined through the warranty polymorphism:

- `BasicWarranty` returns an additional cost of `0`;
- `ExtendedWarranty` returns its additional cost.

`ReturnService.registerReturn` now invokes the warranty cancellation logic for each returned console and adds the refundable warranty amount to the return.

`Return` was extended with a separate `warrantyRefundAmount` value. This amount is added to the total refund and displayed in the return receipt when applicable.

The Technical Leader is responsible for connecting `ReturnService` to the application startup and user interface.