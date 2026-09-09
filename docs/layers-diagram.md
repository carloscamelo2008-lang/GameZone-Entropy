# Layers Diagram

The following diagram represents the four architectural layers of the GameZone Unicesar system, the classes belonging to each layer, the application entry point, and the allowed dependencies between them.

```mermaid
flowchart TD

    %% ==========================================
    %% APPLICATION ENTRY POINT
    %% ==========================================

    Main["Main<br/>Application Entry Point"]

    %% ==========================================
    %% USER INTERFACE LAYER
    %% ==========================================

    subgraph UI["User Interface Layer"]
        UIClass["UI"]
    end

    %% ==========================================
    %% SERVICE LAYER
    %% ==========================================

    subgraph SERVICE["Service Layer"]
        ProductService["ProductService"]
        PersonService["PersonService"]
        SaleService["SaleService"]
    end

    %% ==========================================
    %% PERSISTENCE LAYER
    %% ==========================================

    subgraph PERSISTENCE["Persistence Layer"]
        ProductRepository["ProductRepository"]
        PersonRepository["PersonRepository"]
        SaleRepository["SaleRepository"]
    end

    %% ==========================================
    %% MODEL LAYER
    %% ==========================================

    subgraph MODEL["Model Layer"]
        Product["Product"]
        VideoGame["VideoGame"]
        Console["Console"]
        Person["Person"]
        Customer["Customer"]
        Seller["Seller"]
        Sale["Sale"]
    end

    %% ==========================================
    %% APPLICATION STARTUP
    %% ==========================================

    Main --> UIClass

    %% ==========================================
    %% ALLOWED DEPENDENCIES
    %% ==========================================

    UIClass --> ProductService
    UIClass --> PersonService
    UIClass --> SaleService

    ProductService --> ProductRepository
    ProductService --> Product

    PersonService --> PersonRepository
    PersonService --> Customer
    PersonService --> Seller

    SaleService --> SaleRepository
    SaleService --> ProductService
    SaleService --> PersonService
    SaleService --> Sale

    ProductRepository --> Product
    PersonRepository --> Customer
    PersonRepository --> Seller
    SaleRepository --> Sale
```
