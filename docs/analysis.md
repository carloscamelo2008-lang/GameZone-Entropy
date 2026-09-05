# Analysis

## Project

GameZone Unicesar is a video game and console store located in the university sector of Valledupar. The business sells video games and consoles to the general public and requires an information system to replace the manual processes currently used.

The system must manage information about people, products, and sales while preserving the information between executions.

## Team

The project is developed by the Entropy team.

- Carlos Eduardo Camelo Montaño: Technical Leader
- Jesús Daniel Díaz Álvarez: Developer 1
- Daniel Josué Arrieta Fontalvo: Developer 2

## Objective

The objective of the system is to organize and manage the main operations of GameZone through an object-oriented Java application organized into four architectural layers.

The design will apply encapsulation, inheritance, polymorphism, abstract classes, and separation of responsibilities between the model, persistence, service, and user interface layers.

# Analysis Questions

## 1. Common and specific attributes of people

All people interacting with the store share basic information such as name, identification, and contact phone.

These common attributes should belong to a common abstract `Person` class because they represent information shared by different types of people.

Specific information should belong to specialized classes:

- `Customer` represents customers and their purchase-related information.
- `Seller` represents sellers and includes an employee code and a work shift.

This distinction can be represented through inheritance, with `Customer` and `Seller` extending `Person`.

This design avoids duplicating common attributes and allows each specialized class to contain the information specific to its role.

## 2. Generic person class

A generic `Person` class should exist as an abstract class because the system needs concrete roles that interact with the store, such as customers and sellers.

A generic person should not be instantiated directly because a person without a defined role does not represent a specific business entity in the context of GameZone.

Declaring `Person` as abstract allows it to define common attributes and behavior while requiring concrete subclasses to represent the actual types of people used by the application.

## 3. Common and specific product characteristics

All products sold by the store share common information:

- Identifier
- Title
- Price
- Available quantity in inventory

These characteristics should belong to an abstract `Product` class.

Specialized products contain additional attributes.

A `VideoGame` has:

- Platform
- Genre
- Recommended age rating

A `Console` has:

- Brand
- Model
- Generation

This structure allows common product information to be reused while preserving the characteristics specific to each product type.

## 4. Product description behavior

Every product type must be able to present a complete description that includes its particular characteristics.

The `Product` class should therefore declare an abstract behavior such as `getDescription()`.

Each concrete product subclass must implement this behavior according to its own characteristics.

For example, a video game description should include its platform, genre, and recommended age rating, while a console description should include its brand, model, and generation.

Declaring this behavior as abstract guarantees that every concrete product provides its own implementation and allows the system to apply polymorphism.

## 5. Relationships between a sale and other classes

A `Sale` is associated with a `Customer`, a `Seller`, and one or more `Product` objects.

These relationships are associations rather than inheritance relationships because a sale is not a type of customer, seller, or product.

The relationships represent the participants in a business transaction:

- Each sale is associated with one customer.
- Each sale is associated with one seller.
- Each sale contains one or more products.

The minimum of one product is a business rule required for a sale to be valid.

## 6. Responsibility for calculating the sale total

The `Sale` should be responsible for calculating its own total because the total is derived from the products included in that sale.

Keeping this behavior inside `Sale` follows the principle of assigning behavior to the object that owns the relevant information.

The service layer should coordinate the sale process and validate business rules, while the domain object should encapsulate the calculation related to its own data.

Therefore, the design can include a behavior such as `calculateTotal()` in `Sale`.

## 7. Guaranteeing that a sale contains at least one product

A sale must not be registered without at least one product.

This rule should be validated in the service layer before the sale is persisted because the service layer is responsible for business rules.

The `Sale` object must represent the collection of products involved in the transaction.

When the collection is empty, the service must reject the operation and prevent the sale from being stored.

This guarantees that invalid sales do not reach the persistence layer.

## 8. Automatic inventory update after a sale

When a sale is registered, the available quantity of each sold product must be reduced automatically.

The sale process should be coordinated by the service layer because the operation involves validations and several domain objects.

The `SaleService` should:

1. Validate that the sale contains at least one product.
2. Verify that each product has enough available inventory.
3. Calculate or obtain the sale total.
4. Update the inventory quantities.
5. Persist the updated products.
6. Persist the sale.

The `Product` objects contain the inventory information, while persistence is responsible for storing the updated information in files.

This separation prevents the user interface from directly manipulating persistence or business data.

## 9. Classes in each architectural layer

The system is divided into four required layers.

### Model

The model contains the domain classes representing the business entities and their behavior.

Examples include:

- `Person`
- `Customer`
- `Seller`
- `Product`
- `VideoGame`
- `Console`
- `Sale`

### Persistence

The persistence layer contains the classes responsible for saving and recovering information from files.

These classes must focus on storage and retrieval and must not contain business rules that belong to the service layer.

### Services

The service layer contains the business rules and coordinates operations between the user interface, domain objects, and persistence classes.

The services are responsible for validations and for coordinating operations such as product management, person management, and sales.

### User Interface

The user interface contains the console menu through which users execute the system operations.

The UI must use service classes and must not access persistence directly.

The criterion for assigning a class to a layer is its responsibility:

- Domain concepts belong to the model.
- File access belongs to persistence.
- Business rules belong to services.
- User interaction belongs to the user interface.

## 10. Why file access must not be inside domain classes

Domain classes must not contain logic for reading or writing files because their responsibility is to represent business entities and their behavior.

Mixing file access with domain logic would create strong coupling between the model and the storage mechanism.

This would make the system harder to maintain, test, and modify because changing the persistence format would require changes to domain classes.

Keeping persistence in a separate layer preserves separation of responsibilities and follows the architecture required by the workshop.

## 11. Allowed and forbidden dependencies between layers

The allowed dependency direction is:

```text
UI → Service → Persistence → Model
```

The service layer may also depend directly on the model.

The user interface depends on services because it requests business operations from them.

The service layer depends on persistence because it must save and retrieve data through persistence classes, and it depends on the model because it operates on domain objects.

The persistence layer depends on the model because it stores and reconstructs domain objects.

The model does not depend on any other layer.

The following dependencies are forbidden:

- UI → Persistence
- Model → Persistence
- Model → Service
- Model → UI

These restrictions prevent responsibilities from different layers from becoming mixed.

# Functional Operations

The console menu must support the following operations.

## Product Management

1. Register a new video game.
2. Register a new console.
3. List all products available in the inventory.

## Person Management

4. Register a new customer.
5. List all registered customers.
6. List all registered sellers.

## Sales Management

7. Register a new sale by selecting a customer, a seller, and one or more products.
8. Consult the complete sales history.
9. Consult the purchase history of a specific customer.
10. Consult the sales handled by a specific seller.

The application must also load stored information automatically when it starts and save changes automatically after each operation.

# Business Rules

The design must enforce the following business rules:

- A sale must contain at least one product.
- A product cannot be sold when the available inventory is insufficient.
- The inventory must be reduced when a sale is registered.
- The total value of a sale must be calculated from the products included in the transaction.
- Previously stored information must remain available between executions.
- The application must load stored information when it starts.
- The system must start with at least three sellers preloaded in the corresponding data file because sellers are not registered through the user interface.

# Persistence

All information managed by the system must be preserved between executions.

The application must store products, people, and sales in files managed by the application.

When the application starts, previously stored information must be loaded automatically.

The file format will be selected by the team according to the workshop requirements, provided that the information is preserved correctly between executions.

# Architectural Principles

The implementation must follow these principles:

- All domain attributes must be private.
- General categories that should not be instantiated directly must be abstract.
- Specialized behavior must be represented using abstract methods where appropriate.
- Concrete subclasses must implement inherited abstract behavior.
- The model must remain independent from persistence and user interface concerns.
- The user interface must access the system through services.
- File access must remain inside the persistence layer.
- All class names, attributes, and methods must be written in English.
- Classes must use PascalCase.
- Attributes and methods must use camelCase.
- The design must not introduce classes, attributes, or methods that are not justified by the context of the system.

# Initial Design Decisions

The domain will contain at least two inheritance hierarchies:

1. `Person` → `Customer`, `Seller`
2. `Product` → `VideoGame`, `Console`

The final class structure, attributes, methods, relationships, multiplicities, and layer assignments will be represented in the corresponding Mermaid diagrams:

- `docs/hierarchy-diagram.md`
- `docs/class-diagram.md`
- `docs/layers-diagram.md`

These diagrams will serve as the reference for the implementation.

# Implementation Structure

The application will use the following package structure:

```text
com.gamezone
├── model
├── persistence
├── service
├── ui
└── Main.java
```

The `model` package contains the domain classes.

The `persistence` package contains classes responsible for reading and writing files.

The `service` package contains business logic and rules.

The `ui` package contains the console menu.

The `Main` class starts the application and coordinates the application startup.
