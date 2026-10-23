
# Exception Handling — Analysis

## 1. Purpose

The exception handling mechanism provides a consistent way to report invalid input, business rule violations, missing resources, and persistence failures across GameZone Unicesar.

It separates error detection from error reporting. Validators identify invalid data or operations, services coordinate business logic, repositories report file access failures, and the console UI displays understandable error messages.

## 2. Exception Hierarchy

`GameZoneException` is the common base class for application-specific exceptions. It extends Java's `RuntimeException` and supports constructors with a message and with a message plus the original cause.

The hierarchy contains four specialized exceptions:

- `InvalidDataException`: Used when supplied data is empty, malformed, or outside the permitted range. Examples include an invalid e-mail address, a non-positive product price, or negative stock.
- `BusinessRuleException`: Used when an operation violates a business rule. Examples include a duplicate product or customer identifier, insufficient stock, or requesting an extended warranty for an item that is not part of the sale.
- `ResourceNotFoundException`: Used when a requested product, customer, or seller cannot be found.
- `PersistenceException`: Used when reading or writing application data fails. It can preserve the original exception as its cause to support troubleshooting.

## 3. Validation Responsibilities

### ProductValidator

`ProductValidator` validates product identifiers, titles, prices, stock levels, and product-specific fields.

It also checks identifier availability, product existence, and sufficient stock. Invalid fields raise `InvalidDataException`, duplicate identifiers raise `BusinessRuleException`, and missing products raise `ResourceNotFoundException`.

### PersonValidator

`PersonValidator` validates customer and seller information.

Its rules include identification, name format, phone number format, e-mail format for customers, and required employee code and shift for sellers. It also checks whether a customer exists or whether a customer identifier is already registered.

### SaleValidator

`SaleValidator` validates the list of product identifiers in a sale, confirms that the customer, seller, and requested items exist, checks stock availability, and verifies that extended warranties are requested only for items included in the sale.

It delegates shared product and person checks to `ProductValidator` and `PersonValidator`, avoiding unnecessary duplication of validation rules.

## 4. Integration with the Service Layer

The service layer invokes the appropriate validators before completing operations:

- `ProductService` uses `ProductValidator` when registering products and checking product existence.
- `PersonService` uses `PersonValidator` when registering customers and sellers.
- `SaleService` uses `SaleValidator` to validate sale items, resource existence, stock, and extended warranty assignments.

This arrangement keeps the relevant business checks in reusable validator classes rather than duplicating them throughout the services.

## 5. Persistence Failures

`PersonRepository`, `ProductRepository`, `SaleRepository`, `AccessoryRepository`, `PromotionRepository`, `ReturnRepository`, and `WarrantyRepository` translate relevant file access failures into `PersistenceException`.

The repositories remain responsible for reading and writing their data, while services allow application-specific exceptions to propagate to the caller. Missing data files may be treated as an empty initial data source when supported by the repository implementation.

An unreadable or corrupted existing file may cause application initialization to fail when a service loads its data. Such startup failures need to be distinguished from errors raised during an operation initiated from the menu.

## 6. Console UI Error Reporting

The console `UI` catches `GameZoneException` around operations that can raise application-specific exceptions and displays Spanish error messages.

The main menu also handles `NumberFormatException` so that malformed numeric input can be reported without unnecessarily terminating the menu.

These handlers cover exceptions that reach the menu's execution flow. Exceptions raised while services are being constructed in `Main` occur before `UI.start()` begins and require separate consideration if startup error reporting is part of the required behavior.

## 7. Design Benefits

The exception handling design provides:

- Consistent classification of input, business, resource, and persistence failures.
- Reusable validation rules shared across service operations.
- Preservation of underlying causes for persistence errors.
- Clearer Spanish feedback from the console UI.
- Separation of concerns between validation, business services, persistence, and user interaction.
