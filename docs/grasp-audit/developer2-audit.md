# GRASP Audit Report - Developer 2 (Daniel)

## 1. Scope & Baseline
- **Baseline Commit:** `f6f35d676e8a29cdddabb28a093a1158175aa266`
- **Assigned Scope:**
  - **Person Module:** `Person`, `Customer`, `Seller`, `PersonRepository`, `PersonService`
  - **Accessory Management:** `AccessoryRepository`, `AccessoryService`
  - **Promotion Management:** `PromotionRepository`, `PromotionService`
  - **Return Management:** `ReturnRepository`, `ReturnService` (including monthly balance)
  - **Warranty Management:** `WarrantyRepository`, `WarrantyService`
  - **Cross-cutting Concerns:** Exception handling and validator usage across assigned services and repositories.

---

## 2. Evaluation of GRASP Patterns

### 2.1 Information Expert
- **Verdict:** `correct`
- **Evidence & Findings:** `ReturnService` holds direct access to the return collection (`ReturnRepository`) and obtains sales data through `SaleService` orchestration. Calculating monthly totals using the records it accesses does not violate Information Expert, as `ReturnService` is the primary orchestrator with access to both data sources required for monthly balance aggregation (baseline `ReturnService.java`, lines 20-22).

### 2.2 Creator
- **Verdict:** `correct`
- **Evidence & Findings:** In `PersonService.registerCustomer` (lines 35-49), `PersonService` acts as a legitimate Creator for `Customer` objects because it receives input primitives, instantiates the entity, and passes the updated collection to `personRepository.saveCustomers(customers)` (lines 42-49).

### 2.3 Controller
- **Verdict:** `not applicable`
- **Evidence & Findings:** None of the services or repositories under Developer 2's scope act as system controllers. System input events and workflow coordination from the console interface are handled exclusively by `com.gamezone.ui.UI` (outside this scope).

### 2.4 Low Coupling
- **Verdict:** `correct`
- **Evidence & Findings:** Repositories (`AccessoryRepository`, `ReturnRepository`, etc.) handle CSV file persistence directly. Because persistence is the core intended responsibility of these repository fabrications, direct CSV manipulation in these classes does not introduce ripple-effect changes to higher service layers or domain entities. Service classes depend only on repository abstractions/methods, maintaining low coupling.

### 2.5 High Cohesion
- **Verdict:** `violation`
- **Evidence & Findings:** See **[V-01]**. `ReturnService` mixes two distinct operational responsibilities: managing return transaction workflows (`registerReturn`, lines 68-138) and handling cross-module sales reporting/aggregation (`calculateMonthlySales`, lines 196-209, and `generateMonthlyBalance`, lines 244-246). Combining transactional processing with cross-domain reporting calculation dilutes class cohesion.

### 2.6 Polymorphism
- **Verdict:** `correct`
- **Evidence & Findings:** Polymorphic invocation is correctly applied within Developer 2's service operations. In `PromotionService.findBestPromotionFor` (lines 130-138), the method dispatches `promotion.calculateDiscount(sale)` polymorphically across concrete promotion implementations. Similarly, `WarrantyService.cancelWarranties` (lines 159-168) invokes `warranty.getAdditionalCost()` on abstract `Warranty` instances, delegating execution to the specific subtype logic without conditional branching.

### 2.7 Pure Fabrication
- **Verdict:** `correct`
- **Evidence & Findings:** `PersonRepository`, `ReturnRepository`, and `WarrantyRepository` are Pure Fabrications created to handle database/file I/O operations, keeping domain entities like `Person` and `Return` clean from infrastructure logic.

### 2.8 Indirection
- **Verdict:** `correct`
- **Evidence & Findings:** `ReturnService` acts as an intermediary layer between UI controllers and `ReturnRepository`, preventing the UI layer from directly coupling to data access and file storage mechanics.

### 2.9 Protected Variations
- **Verdict:** `violation`
- **Evidence & Findings:** See **[V-02]**. Unstable CSV parsing errors are handled inconsistently. In `AccessoryRepository.fromCsvLine` (lines 140-176), unguarded numeric conversions at lines 149-164 trigger unhandled `NumberFormatException` (a raw `RuntimeException`). In contrast, `ReturnRepository.fromCsvLine` (lines 134-163) explicitly handles `DateTimeParseException` for date parsing (lines 141-146) and safely skips invalid records. Callers are not protected against numeric parsing failures in `AccessoryRepository`.

---

## 3. Detailed Audit Findings & Violations

### [V-01] Unfocused Responsibility Boundary in ReturnService (Reporting vs Transactions)
- **GRASP Pattern Violated:** High Cohesion
- **Module / Component:** Return Management / `ReturnService`
- **Exact Location:** `com.gamezone.service.ReturnService` (`registerReturn` L68-138 vs `calculateMonthlySales` L196-209, `calculateMonthlyReturns` L220-232, `generateMonthlyBalance` L244-246)
- **Violation Reason:** While `ReturnService` is specified to handle monthly balance calculation (lines 20-22), bundling core return processing with cross-module sales querying/aggregation creates an unfocused responsibility boundary. Transactional lifecycle logic for returns and financial sales/balance reporting represent separate reasons to change.
- **Consequence:** Changes to sales calculation business rules or financial reporting criteria force modifications to `ReturnService`, increasing the risk of introducing regressions into return transaction processing.
- **Proposed Design Solution:** Extract sales aggregation and balance calculations into a dedicated reporting/calculation component (e.g., `ReturnReportingService` or `MonthlyBalanceCalculator`). `ReturnService` will maintain single focus on return creation and transaction validation.
- **Correction Owner:** Developer 2 (Daniel)

---

### [V-02] Inconsistent Exception Handling for Corrupted CSV Storage Lines
- **GRASP Pattern Violated:** Protected Variations
- **Module / Component:** Accessory Management / `AccessoryRepository` vs `ReturnRepository`
- **Exact Location:** `com.gamezone.persistence.AccessoryRepository.fromCsvLine` (L140-176, specifically L149-164) vs `com.gamezone.persistence.ReturnRepository.fromCsvLine` (L134-163, specifically L141-146)
- **Violation Reason:** `AccessoryRepository.fromCsvLine` uses `Integer.parseInt` without catching `NumberFormatException` or wrapping it in a domain exception. In contrast, `ReturnRepository.fromCsvLine` catches `DateTimeParseException` during date parsing and gracefully skips malformed records.
- **Consequence:** Corrupted numeric fields in the accessory CSV file cause unhandled `NumberFormatException` runtime crashes during loading, whereas malformed dates in returns are handled gracefully.
- **Proposed Design Solution:** Guard numeric conversions in `AccessoryRepository.fromCsvLine` against parsing failures and handle corrupt lines consistently across repositories.
- **Correction Owner:** Developer 2 (Daniel)

---

## 4. Summary of Identified Violations

| ID | Component | GRASP Pattern(s) | Severity | Description | Correction Owner |
|---|---|---|---|---|---|
| **V-01** | `ReturnService` | High Cohesion | Medium | Combines return lifecycle processing with cross-module sales reporting and monthly balance math. | Developer 2 (Daniel) |
| **V-02** | `AccessoryRepository` | Protected Variations | Low | Unguarded `NumberFormatException` in CSV numeric parsing exposes callers to unhandled runtime crashes. | Developer 2 (Daniel) |