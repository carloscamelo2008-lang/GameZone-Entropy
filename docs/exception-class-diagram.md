
# Exception Handling — Class Diagram

The diagram shows the custom exception hierarchy, the validator classes, and their main relationships with the services, repositories, and console user interface.

```mermaid
classDiagram
    class RuntimeException

    class GameZoneException {
        <<exception>>
        +GameZoneException(String message)
        +GameZoneException(String message, Throwable cause)
    }

    class InvalidDataException {
        <<exception>>
    }

    class BusinessRuleException {
        <<exception>>
    }

    class ResourceNotFoundException {
        <<exception>>
    }

    class PersistenceException {
        <<exception>>
    }

    class ProductValidator {
        <<utility>>
        +validateText(String value, String fieldName)
        +validateId(String id)
        +validateTitle(String title)
        +validatePrice(double price)
        +validateStock(int stock)
        +validateIdAvailable(Product existing, String id)
        +validateExists(Product product, String id)
        +validateSufficientStock(Product product, int quantity)
    }

    class PersonValidator {
        <<utility>>
        +validateId(String id)
        +validateName(String name)
        +validatePhone(String phone)
        +validateEmail(String email)
        +validateCustomer(String id, String name, String phone, String email)
        +validateSeller(String id, String name, String phone, String employeeCode, String shift)
    }

    class SaleValidator {
        <<utility>>
        +validateProductIds(List~String~ productIds)
        +validateCustomerExists(Customer customer, String id)
        +validateSellerExists(Seller seller, String id)
        +validateProductExists(Product product, String id)
        +validateStock(Product product, int quantity)
        +validateExtendedWarranties(List~String~ productIds, List~String~ warrantyProductIds)
    }

    class ProductService
    class PersonService
    class SaleService
    class PersonRepository
    class ProductRepository
    class SaleRepository
    class UI

    RuntimeException <|-- GameZoneException
    GameZoneException <|-- InvalidDataException
    GameZoneException <|-- BusinessRuleException
    GameZoneException <|-- ResourceNotFoundException
    GameZoneException <|-- PersistenceException

    ProductService ..> ProductValidator : validates products
    PersonService ..> PersonValidator : validates people
    SaleService ..> SaleValidator : validates sales

    SaleValidator ..> ProductValidator : delegates product checks
    SaleValidator ..> PersonValidator : delegates person checks

    ProductValidator ..> InvalidDataException : throws
    ProductValidator ..> BusinessRuleException : throws
    ProductValidator ..> ResourceNotFoundException : throws

    PersonValidator ..> InvalidDataException : throws
    PersonValidator ..> BusinessRuleException : throws
    PersonValidator ..> ResourceNotFoundException : throws

    SaleValidator ..> InvalidDataException : throws
    SaleValidator ..> BusinessRuleException : throws
    SaleValidator ..> ResourceNotFoundException : throws

    PersonRepository ..> PersistenceException : reports persistence failures
    ProductRepository ..> PersistenceException : reports persistence failures
    SaleRepository ..> PersistenceException : reports persistence failures

    UI ..> GameZoneException : catches application errors
```

## Key Design Notes

- `GameZoneException` provides a shared base type for application-specific errors.
- The specialized exceptions distinguish invalid input, business rule violations, missing resources, and persistence failures.
- The validator classes centralize checks and are used by the corresponding services.
- `SaleValidator` delegates shared product and person validations instead of duplicating them.
- The repositories report relevant data access failures through `PersistenceException`.
- The console UI catches `GameZoneException` and displays Spanish error messages.
