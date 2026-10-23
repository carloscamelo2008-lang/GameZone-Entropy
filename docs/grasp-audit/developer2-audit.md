# GRASP Audit Report - Developer 2 (Daniel)

## 1. Scope
This audit covers the following modules and components assigned to Developer 2:
- **Person Module:** `Person`, `Customer`, `Seller`, `PersonRepository`, `PersonService`
- **Accessory Management:** `AccessoryRepository`, `AccessoryService`
- **Promotion Management:** `PromotionRepository`, `PromotionService`
- **Return Management:** `ReturnRepository`, `ReturnService` (including monthly balance)
- **Warranty Management:** `WarrantyRepository`, `WarrantyService`
- **Cross-cutting Concerns:** Exception handling and validator usage across repositories and services

---

## 2. Evaluation of GRASP Patterns

### 2.1 Information Expert
- **Verdict:** violation
- **Evidence & Findings:** `ReturnService` calculates totals and formats monthly balances directly instead of delegating domain logic to entities or specialized data structures.

### 2.2 Creator
- **Verdict:** correct
- **Evidence & Findings:** Services act as creators for domain objects when receiving DTO/primitive inputs from upper layers. Repositories handle persistence objects cleanly.

### 2.3 Controller
- **Verdict:** not applicable
- **Evidence & Findings:** Controller logic is handled at the UI layer (`UI.java`). Services and Repositories focus strictly on domain logic and data access.

### 2.4 Low Coupling
- **Verdict:** violation
- **Evidence & Findings:** Repositories directly handle concrete CSV file I/O formatting without an abstraction layer, coupling persistence details with domain-adjacent data structures.

### 2.5 High Cohesion
- **Verdict:** violation
- **Evidence & Findings:** `ReturnService` combines return registration, period validation, monthly sales aggregation, and balance generation, reducing cohesion.

### 2.6 Polymorphism
- **Verdict:** not applicable
- **Evidence & Findings:** Services and Repositories under audit do not currently rely on polymorphic method dispatch for business rules.

### 2.7 Pure Fabrication
- **Verdict:** correct
- **Evidence & Findings:** Repositories (`PersonRepository`, `ReturnRepository`, etc.) and Services exist as pure fabrications to maintain clean separation of concerns and avoid bloat in domain models.

### 2.8 Indirection
- **Verdict:** correct
- **Evidence & Findings:** Services mediate between UI calls and persistence repositories, avoiding direct coupling between UI components and raw file operations.

### 2.9 Protected Variations
- **Verdict:** violation
- **Evidence & Findings:** Validation logic and custom exceptions are thrown inconsistently across repositories and services, exposing callers to raw file/parse errors in edge cases.

---

## 3. Summary of Identified Violations

| Component | Pattern | Severity | Description |
|-----------|---------|----------|-------------|
| `ReturnService` | High Cohesion | Medium | Combines sales aggregation, return logic, and balance calculations in a single class. |
| `ReturnService` | Information Expert | Medium | Directly calculates monthly balance metrics instead of delegating to domain models or specialized fabrications. |
| Repositories (`*Repository`) | Low Coupling | Low | Concrete repositories manage direct CSV parsing logic rather than relying on dedicated file-mapping utilities. |
| Services / Repositories | Protected Variations | Low | Inconsistent validation checks prior to file persistence operations. |