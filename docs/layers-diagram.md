# Integrated Layers Diagram

The following diagram represents the four architectural layers of the integrated GameZone Unicesar system and the dependencies between them.

```mermaid
flowchart TD

    Main["Main<br/>Application Entry Point"]

    subgraph UI["User Interface Layer"]
        UIClass["UI"]
    end

    subgraph SERVICE["Service Layer"]
        ProductService["ProductService"]
        PersonService["PersonService"]
        AccessoryService["AccessoryService"]
        PromotionService["PromotionService"]
        WarrantyService["WarrantyService"]
        SaleService["SaleService"]
        ReturnService["ReturnService"]
    end

    subgraph PERSISTENCE["Persistence Layer"]
        ProductRepository["ProductRepository"]
        PersonRepository["PersonRepository"]
        AccessoryRepository["AccessoryRepository"]
        PromotionRepository["PromotionRepository"]
        SaleRepository["SaleRepository"]
        WarrantyRepository["WarrantyRepository"]
        ReturnRepository["ReturnRepository"]
    end

    subgraph MODEL["Model Layer"]
        Product["Product"]
        VideoGame["VideoGame"]
        Console["Console"]
        Accessory["Accessory"]
        Controller["Controller"]
        Cable["Cable"]
        Memory["Memory"]

        Person["Person"]
        Customer["Customer"]
        Seller["Seller"]

        Promotion["Promotion"]
        PercentageDiscount["PercentageDiscount"]
        CategoryDiscount["CategoryDiscount"]
        BulkPurchaseDiscount["BulkPurchaseDiscount"]

        Warranty["Warranty"]
        BasicWarranty["BasicWarranty"]
        ExtendedWarranty["ExtendedWarranty"]

        Sale["Sale"]
        Return["Return"]
    end

    Main --> UIClass

    UIClass --> ProductService
    UIClass --> PersonService
    UIClass --> AccessoryService
    UIClass --> SaleService

    ProductService --> ProductRepository
    ProductService --> Product

    PersonService --> PersonRepository
    PersonService --> Customer
    PersonService --> Seller

    AccessoryService --> AccessoryRepository
    AccessoryService --> Accessory

    PromotionService --> PromotionRepository
    PromotionService --> Promotion

    WarrantyService --> WarrantyRepository
    WarrantyService --> SaleRepository
    WarrantyService --> ProductService
    WarrantyService --> Warranty

    SaleService --> SaleRepository
    SaleService --> ProductService
    SaleService --> PersonService
    SaleService --> AccessoryService
    SaleService --> PromotionService
    SaleService --> WarrantyService
    SaleService --> Sale

    ReturnService --> ReturnRepository
    ReturnService --> SaleService
    ReturnService --> ProductService
    ReturnService --> AccessoryService
    ReturnService --> WarrantyService
    ReturnService --> Return

    ProductRepository --> Product
    PersonRepository --> Customer
    PersonRepository --> Seller
    AccessoryRepository --> Accessory
    PromotionRepository --> Promotion
    SaleRepository --> Sale
    WarrantyRepository --> Warranty
    ReturnRepository --> Return

    Product --> VideoGame
    Product --> Console
    Product --> Accessory

    Accessory --> Controller
    Accessory --> Cable
    Accessory --> Memory

    Person --> Customer
    Person --> Seller

    Promotion --> PercentageDiscount
    Promotion --> CategoryDiscount
    Promotion --> BulkPurchaseDiscount

    Warranty --> BasicWarranty
    Warranty --> ExtendedWarranty
```

## Dependency rules

The project preserves the following architectural direction:

```text
UI → Service → Persistence
       ↓
      Model
```

The service layer may also coordinate other services when business rules require collaboration between modules.

The integrated system uses this coordination in particular for:

- `SaleService`, which coordinates products, accessories, people, promotions, and warranties.
- `WarrantyService`, which resolves sales through `SaleRepository` and products through `ProductService`.
- `ReturnService`, which coordinates returns, inventory restoration, and warranty cancellation.
- `ReturnRepository`, which resolves returned items through both `ProductService` and `AccessoryService`.

The model layer does not contain file access logic, and persistence responsibilities remain isolated in the repository layer.