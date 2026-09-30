# GameZone-Entropy

Integrated administration system for GameZone Unicesar, developed in Java with a layered architecture and a modular design for products, accessories, promotions, warranties, sales, and returns.

## Team

| Integrant | Role |
| --- | --- |
| Carlos Eduardo Camelo Montaño | Technical Leader |
| Jesús Daniel Díaz Álvarez | Developer 1 |
| Daniel Josué Arrieta Fontalvo | Developer 2 |

## System Overview

GameZone is an administration system for a video game store. The integrated version combines the functionality developed in the previous requirements into a single system.

The integrated domain includes:

- Video games
- Consoles
- Accessories
- Customers
- Sellers
- Promotions
- Basic warranties
- Extended warranties
- Sales
- Returns
- Monthly balance reporting

A sale can contain video games, consoles, and accessories in the same transaction.

The integrated services coordinate promotions, warranties, inventory, and returns while preserving the separation between the model, service, persistence, and user interface layers.

## Integrated Functionalities

### Product Management

The system supports:

1. Registering video games.
2. Registering consoles.
3. Listing products.
4. Managing product stock.

### Accessory Management

The system supports:

1. Registering controllers.
2. Registering cables.
3. Registering memory cards.
4. Listing accessories by type.
5. Finding accessories compatible with a console.
6. Updating accessory stock.
7. Restoring accessory stock after a return.

All accessories extend the common `Product` hierarchy.

### People Management

The system supports:

1. Registering customers.
2. Listing customers.
3. Listing sellers.
4. Finding customers by id.
5. Finding sellers by id.

### Promotions

The system supports three promotion types:

- Percentage discounts.
- Category discounts.
- Bulk-purchase discounts.

Category discounts support:

- `VIDEOGAME`
- `CONSOLE`
- `ACCESSORY`

`PromotionService.findBestPromotionFor(...)` selects the active promotion that provides the largest monetary discount for a sale.

### Sales

The integrated sale flow supports:

1. Validating the customer and seller.
2. Validating that the sale contains at least one item.
3. Resolving video games, consoles, and accessories.
4. Validating stock according to the item type.
5. Calculating the sale subtotal.
6. Selecting the best applicable promotion.
7. Applying the promotion discount to the sale subtotal.
8. Assigning an automatic basic warranty to each console.
9. Assigning optional extended warranties to consoles.
10. Calculating the final sale total.
11. Updating the corresponding inventory.
12. Persisting the sale.

The final sale total is calculated as:

`subtotal - discount + extended warranty cost`

The sale receipt includes:

- Subtotal.
- Applied promotion.
- Discount amount.
- Extended warranty cost.
- Final total.

### Warranties

The warranty module supports:

- Automatic basic warranties for consoles.
- Optional extended warranties.
- Warranty validity queries.
- Upcoming warranty expiration queries.
- Warranty lookup by product and sale.
- Cancellation of warranties when a console is returned.

The extended warranty cost is calculated as 10% of the covered product price.

The warranty repository stores raw warranty identifiers. `WarrantyService` resolves the corresponding sales and products using `SaleRepository` and `ProductService`. This prevents the circular dependency addressed during integration.
### Returns

The integrated return module supports:

1. Registering partial returns.
2. Validating that the original sale exists.
3. Validating the 30-day return period.
4. Validating that returned items belong to the original sale.
5. Preventing the same unit from being returned more than once.
6. Restoring product stock.
7. Restoring accessory stock.
8. Calculating proportional refunds for discounted sales.
9. Cancelling warranties associated with returned consoles.
10. Including refundable extended warranty costs in the return.
11. Generating a detailed return receipt.

The proportional refund for a returned item is calculated as:

`price × (1 - discount / subtotal)`

The return receipt displays the original price, proportional discount, refunded amount, and warranty refund when applicable.

### Monthly Balance

The return service provides:

- `calculateMonthlySales(int month, int year)`
- `calculateMonthlyReturns(int month, int year)`
- `generateMonthlyBalance(int month, int year)`

Monthly sales use each sale's final total, including promotions and extended warranties.

The monthly net balance is calculated as:

`monthly sales - monthly returns`

## Architecture

The application is organized into four layers:

1. **Model**: domain entities and business concepts.
2. **Persistence**: repositories responsible for reading and writing data.
3. **Service**: business logic and coordination between modules.
4. **UI**: console-based user interface.

The main dependency direction is:

```text
UI → Service → Persistence
       ↓
      Model
```

Services may coordinate other services when cross-module business rules require it.

## Main Integrated Services

### ProductService

Manages:

- Products.
- Stock validation.
- Stock reduction.
- Stock restoration.
- Product registration.

### AccessoryService

Manages:

- Accessory registration.
- Accessory queries.
- Compatibility queries.
- Accessory stock updates.
- Accessory stock restoration.

### PromotionService

Manages:

- Promotion registration.
- Active promotions.
- Best-promotion selection.

### WarrantyService

Manages:

- Basic warranties.
- Extended warranties.
- Warranty queries.
- Warranty cancellation during returns.

### SaleService

Coordinates:

- People.
- Products.
- Accessories.
- Promotions.
- Warranties.
- Sales persistence.

### ReturnService

Coordinates:

- Return validation.
- Product stock restoration.
- Accessory stock restoration.
- Proportional refunds.
- Warranty cancellation.
- Monthly reporting.

## Persistence

The system uses file-based persistence.

Examples include:

- `data/products.dat`
- `data/sales.dat`
- `data/customers.csv`
- `data/sellers.csv`
- `data/accessories.csv`
- `data/promotions.csv`
- `data/warranties.csv`
- `data/returns.csv`

Repositories are responsible for file access and serialization.

## Project Structure

```text
src/main/java/com/gamezone/
├── model/
├── persistence/
├── service/
├── ui/
└── Main.java
docs/
├── analysis.md
├── class-diagram.md
├── hierarchy-diagram.md
├── layers-diagram.md
├── accessory-analysis.md
├── accessory-class-diagram.md
├── promotion-analysis.md
├── promotion-class-diagram.md
├── warranty-analysis.md
├── warranty-class-diagram.md
├── return-analysis.md
├── return-class-diagram.md
├── integration-analysis.md
├── integrated-class-diagram.md
└── ai-usage/
```

## Documentation

The project includes:

- Module analysis documents.
- Module class diagrams.
- Integrated analysis.
- Integrated class diagram.
- Integrated layers diagram.
- AI usage logs.

The AI usage logs are stored in:

```text
docs/ai-usage/
```

Each team member maintains a personal log documenting the use of AI tools according to the integration requirement.

## Execution

The project uses Java 17 and Maven.

Compile the project with:

```bash
mvn clean compile
```

The application entry point is:

```text
com.gamezone.Main
```

## Version Control

The project follows a Git Flow-based process using:

```text
main
  ↑
develop
  ↑
feature/*
fix/*
refactor/*
docs/*
```

All integration work is performed through Pull Requests targeting `develop`.

Commits follow the Conventional Commits format and are written in English.

## Integrated Documentation

For a detailed explanation of the integration adjustments A1-A7, see:

- `docs/integration-analysis.md`
- `docs/integrated-class-diagram.md`
- `docs/layers-diagram.md`

The final integrated version must be verified through a complete scenario including a mixed sale, promotion application, extended warranty, partial return, stock restoration, warranty cancellation, refund calculation, and monthly balance.