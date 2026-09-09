# Analysis

## Project

GameZone Unicesar is a video game and console store located in the university sector of Valledupar. The business sells video games and consoles to the general public and requires an information system to replace the manual processes currently used.

The system manages information about people, products, and sales while preserving the information between executions.

## Team

The project is developed by the Entropy team.

- Carlos Eduardo Camelo Montaño: Technical Leader
- Jesús Daniel Díaz Álvarez: Developer 1
- Daniel Josué Arrieta Fontalvo: Developer 2

## Objective

The objective of the system is to organize and manage the main operations of GameZone through an object-oriented Java application organized into four architectural layers.

The implementation applies encapsulation, inheritance, polymorphism, abstract classes, and separation of responsibilities between the model, persistence, service, and user interface layers.

# Analysis Questions

## 1. Common and specific attributes of people

All people interacting with the store share basic information such as name, identification, and contact phone.

These common attributes belong to the abstract `Person` class because they represent information shared by different types of people.

Specific information belongs to specialized classes:

- `Customer` represents customers and contains their identification and contact information.
- `Seller` represents sellers and includes an employee code and a work shift.

This distinction is represented through inheritance, with `Customer` and `Seller` extending `Person`.

This design avoids duplicating common attributes and allows each specialized class to contain the information specific to its role.

## 2. Generic person class

A generic `Person` class exists as an abstract class because the system works with concrete roles that interact with the store, such as customers and sellers.

A generic person is not instantiated directly because a person without a defined role does not represent a specific business entity in the context of GameZone.

Declaring `Person` as abstract allows it to define common attributes and behavior while requiring concrete subclasses to represent the actual types of people used by the application.

## 3. Common and specific product characteristics

All products sold by the store share common information:

- Identifier
- Title
- Price
- Available quantity in inventory

These characteristics belong to the abstract `Product` class.

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

The `Product` class therefore declares the abstract behavior `getDescription()`.

Each concrete product subclass implements this behavior according to its own characteristics.

For example, a video game description includes its platform, genre, and recommended age rating, while a console description includes its brand, model, and generation.

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

The `Sale` is responsible for calculating its own total because the total is derived from the products included in that sale.

Keeping this behavior inside `Sale` follows the principle of assigning behavior to the object that owns the relevant information.

The service layer coordinates the sale process and validates business rules, while the domain object encapsulates the calculation related to its own data.

Therefore, `Sale` provides behavior for calculating the total of the transaction.

## 7. Guaranteeing that a sale contains at least one product

A sale must not be registered without at least one product.

This rule is validated in the service layer before the sale is persisted because the service layer is responsible for business rules.

The `Sale` object represents the collection of products involved in the transaction.

When the collection is empty, the service rejects the operation and prevents the sale from being stored.

This guarantees that invalid sales do not reach the persistence layer.

## 8. Automatic inventory update after a sale

When a sale is registered, the available quantity of each sold product is reduced automatically.

The sale process is coordinated by the service layer because the operation involves validations and several domain objects.

The `SaleService` performs the following process:

1. Validates that the sale contains at least one product.
2. Verifies that each product has enough available inventory.
3. Builds the collection of products included in the sale.
4. Reduces the inventory quantities after all stock validations succeed.
5. Creates the sale and calculates its total.
6. Persists the sale.

The `Product` objects contain the inventory information, while the persistence layer stores the updated product information in files.

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

The current persistence classes are:

- `PersonRepository`
- `ProductRepository`
- `SaleRepository`

These classes focus on storage and retrieval and do not contain business rules that belong to the service layer.

### Services

The service layer contains the business rules and coordinates operations between the user interface, domain objects, and persistence classes.

The current service classes are:

- `PersonService`
- `ProductService`
- `SaleService`

The services are responsible for validations and for coordinating operations such as product management, person management, and sales.

### User Interface

The user interface contains the console menu through which users execute the system operations.

The UI uses service classes and does not access persistence directly.

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

The application follows these dependency relationships:

```text
UI → Service
Service → Persistence
Service → Model
Persistence → Model
```

The user interface depends on services because it requests business operations from them.

The service layer depends on persistence because it must save and retrieve information through persistence classes, and it depends on the model because it operates on domain objects.

The persistence layer depends on the model because it stores and reconstructs domain objects.

The model does not depend on the other application layers.

The following dependencies are forbidden:

- UI → Persistence
- UI → Model
- Model → Persistence
- Model → Service
- Model → UI

These restrictions prevent responsibilities from different layers from becoming mixed.

# Functional Operations

The console menu supports the following operations.

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

The application also loads previously stored information when it starts, and changes made through the system are persisted by the corresponding persistence classes.

# Business Rules

The implemented system enforces the following business rules:

- A sale must contain at least one product.
- A product cannot be sold when the available inventory is insufficient.
- The inventory is reduced when a sale is registered.
- The total value of a sale is calculated from the products included in the transaction.
- Previously stored information remains available between executions.
- The application loads stored information when it starts.
- The system starts with at least three sellers preloaded in the corresponding data file because sellers are not registered through the user interface.
- A customer cannot be registered when another customer already uses the same identification.

# Persistence

The system preserves the information it manages between executions by using files.

The current implementation uses:

- `data/customers.csv` for customer information.
- `data/sellers.csv` for the preloaded seller information.
- `data/products.dat` for product information.
- `data/sales.dat` for sales information.

The application loads previously stored information when it starts.

The file format is selected according to the type of information being stored. CSV is used for person information, while Java serialization is used for products and sales.

The `data/sellers.csv` file contains the sellers required for the initial execution.

# Architectural Principles

The implementation follows these principles:

- All domain attributes are private.
- General categories that should not be instantiated directly are abstract.
- Specialized behavior is represented using abstract methods where appropriate.
- Concrete subclasses implement inherited abstract behavior.
- The model remains independent from persistence and user interface concerns.
- The user interface accesses the system through services.
- File access remains inside the persistence layer.
- All class names, attributes, and methods are written in English.
- Classes use PascalCase.
- Attributes and methods use camelCase.
- Classes, attributes, and methods are introduced according to the context and responsibilities of the system.

# Design Decisions

The domain contains two inheritance hierarchies:

1. `Person` → `Customer`, `Seller`
2. `Product` → `VideoGame`, `Console`

`Person` and `Product` are abstract classes, while their specialized subclasses are concrete.

The final class structure, attributes, methods, relationships, multiplicities, and layer assignments are represented in the corresponding Mermaid diagrams:

- `docs/hierarchy-diagram.md`
- `docs/class-diagram.md`
- `docs/layers-diagram.md`

These diagrams document the implemented design.

# Implementation Structure

The application is organized using the following package structure:

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

The `Main` class starts the application and initializes the repositories, services, and user interface.
