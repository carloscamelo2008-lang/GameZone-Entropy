# Requirement 7 - GRASP Audit: Technical Leader

## Audit baseline and scope

- **Repository:** `carloscamelo2008-lang/GameZone-Entropy`
- **Branch:** `feature/grasp-audit`
- **Baseline:** `develop` at `f6f35d676e8a29cdddabb28a093a1158175aa266`
- **Phase:** Requirement 7, Phase 1 (audit only). No application code has been changed as part of this audit.

### Classes and methods in scope

The scope is organized by the module/requirement that introduced or extended the responsibility. The actual repository class is `UI.java`; this is the console-menu implementation called `ConsoleMenu` in the requirement.

- **Workshop 1 - Sales and application startup**
  - `Sale`: state, promotion fields as currently implemented, subtotal/final-total calculations, and receipt generation.
  - `SaleRepository`: `saveAll()` and `loadAll()`.
  - `SaleService`: constructor, `registerSale()`, item lookup, and sale-query methods.
  - `Main.main()`: application wiring and startup.
- **Requirement 1 - Accessories**
  - `SaleService.registerSale()`: accessory lookup and accessory stock updates.
  - `UI.manageAccessories()` and related menu handlers: console integration with accessory operations.
- **Requirement 2 - Promotions**
  - `Sale`: promotion name and discount fields, total calculations, and receipt breakdown.
  - `SaleService.registerSale()`: promotion selection and applied discount.
  - `UI.registerSale()` and sales-history handlers: sale presentation.
- **Requirement 3 - Returns**
  - `ProductService.restoreStock()`: product stock restoration integration.
  - `UI.showMonthlyBalance()`: monthly balance menu integration.
  - **`Sale.canBeReturned()` is excluded** because the responsibility map assigns that method to Developer 1.
- **Requirement 4 - Warranties**
  - `SaleService.registerSale()`: basic/extended warranty assignment and extended-warranty cost.
  - `UI.registerSale()`: console prompt for an extended warranty.
  - `Main.main()`: wiring of warranty-related dependencies.
- **Requirement 6 - Exceptions and validation**
  - `UI.start()` and operation handlers: menu routing and differentiated handling of typed exceptions.
  - `Main.main()`: handling a typed application exception during startup.

The audit covers the nine GRASP patterns named in the requirement. Each verdict is based on the scoped source code at the stated `develop` baseline. Potential functional defects are recorded separately because Requirement 7 says not to change behavior during the audit phase.

## 1. Information Expert

**Verdict: `correct`**

- `Sale.calculateTotal()` sums the prices of the products held by the sale, and `Sale.calculateFinalTotal()` combines that subtotal with the sale's discount and extended-warranty cost. Keeping these calculations in `Sale` is appropriate because the sale holds the corresponding data.
- `Sale.generateReceipt()` formats the values associated with the sale without reaching into repositories.
- `ProductService.restoreStock()` updates the stock of a product managed by the service and persists the updated product collection through `ProductRepository`. The service owns that collection/persistence coordination; the product model does not perform file access.
- `SaleService` delegates promotion selection and warranty creation to `PromotionService` and `WarrantyService`, rather than implementing their internal rules itself.

**Evidence:** [Sale.java, lines 136-171](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Sale.java#L136-L171); [ProductService.java, restoreStock](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/ProductService.java#L74-L84); [SaleService.java, lines 124-130 and 147-202](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L124-L202).

## 2. Creator

**Verdict: `correct`**

- `SaleService.registerSale()` creates the `Sale` after resolving its customer, seller, and products. It has the associated information needed to assemble the sale.
- `Main.main()` creates and wires repositories and services in one application-startup location. This is an appropriate place to construct the application object graph, rather than distributing setup across domain classes.
- `SaleRepository` persists sales; it does not take over the business decision to initiate a sale.

**Evidence:** [SaleService.java, lines 86-122](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L86-L122); [Main.java, lines 30-116](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/Main.java#L30-L116).

## 3. Controller

**Verdict: `correct`**

- `UI.start()` receives the user's menu option and routes it to the corresponding interaction method.
- The sale interaction delegates registration to `SaleService.registerSale()`; the service coordinates the business workflow, while the UI gathers input and displays the result.
- `Main` is limited to application setup and starting the UI; it does not process individual sale rules.

**Evidence:** [UI.java, lines 59-125](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L59-L125); [UI.java, lines 364-450](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L364-L450); [SaleService.java, lines 78-204](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L78-L204).


## 4. Low Coupling

**Verdict: `violation`**

**Finding: `V-01` (shared with Patterns 5, 6, and 9).**

The avoidable coupling is concentrated in the console sale interaction. `UI.registerSale()` queries `ProductService.listAllProducts()`, inspects each concrete product as a `Console`, and uses that type check to decide whether to prompt for an extended warranty. `SaleService.registerSale()` independently performs another `instanceof Console` check before assigning warranties. This makes the UI depend on a concrete domain subtype and duplicates a rule that affects the sale workflow.

The existence of multiple collaborators in `SaleService` is not treated as a violation by itself: the requirement explicitly assigns the service responsibility for coordinating products, accessories, promotions, and warranties. The supported coupling finding is the duplicated concrete-type decision across the UI and service.

**Evidence:** [UI.java, lines 400-424](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L400-L424); [SaleService.java, lines 147-182](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L147-L182).

## 5. High Cohesion

**Verdict: `violation`**

**Finding: `V-01`.**

The console UI's main role is to gather input, route commands, and display results. However, `UI.registerSale()` also retrieves inventory records and decides warranty eligibility by checking the product's concrete type. That policy-level decision overlaps with the same decision inside `SaleService.registerSale()`. The issue is not that one console menu exposes several application operations; it is that this sale handler mixes presentation with domain-type policy and duplicates the service's decision.

**Evidence:** [UI.java, lines 364-450](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L364-L450); [SaleService.java, lines 147-182](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L147-L182).

## 6. Polymorphism

**Verdict: `violation`**

**Finding: `V-01`.**

The workflow selects warranty behavior with explicit type checks:

- `UI.registerSale()` checks `product instanceof Console` before asking whether an extended warranty is wanted.
- `SaleService.registerSale()` checks `product instanceof Console` before assigning basic and optional extended warranties.

This is behavior that varies by product subtype, but the variation is encoded as conditionals in two classes rather than exposed through one polymorphic eligibility contract or a single warranty-policy abstraction. If another product subtype becomes eligible for warranties, both locations would need coordinated edits.

**Evidence:** [UI.java, lines 400-424](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L400-L424); [SaleService.java, lines 147-182](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L147-L182).


## 7. Pure Fabrication

**Verdict: `correct`**

- `SaleRepository` is a non-domain class that isolates file serialization and reading/writing responsibilities from `Sale` and the sale workflow.
- `SaleService` is a service-level fabrication that coordinates validation and the collaborating product, people, accessory, promotion, warranty, and persistence modules. It avoids placing file access or the entire transaction workflow inside the `Sale` entity.
- `ProductService.restoreStock()` keeps stock collection management and persistence coordination in the service layer instead of adding repository operations to the product entity.

**Evidence:** [SaleRepository.java, lines 12-55](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/persistence/SaleRepository.java#L12-L55); [SaleService.java, lines 20-59 and 78-204](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L20-L204); [ProductService.java, restoreStock](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/ProductService.java#L74-L84).

## 8. Indirection

**Verdict: `correct`**

- The UI requests operations through services rather than accessing repositories directly.
- `SaleService` collaborates with `ProductService`, `AccessoryService`, `PersonService`, `PromotionService`, and `WarrantyService` for each module's operations, while sale persistence is delegated to `SaleRepository`.
- `Main` centralizes object construction and dependency wiring; the UI does not construct repositories or open data files itself.

This provides service and repository intermediaries between the console and persistence. The concrete subtype check in `V-01` is captured under Low Coupling, High Cohesion, Polymorphism, and Protected Variations, not treated as a separate failure of Indirection.

**Evidence:** [UI.java, constructor and fields](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L25-L54); [SaleService.java, dependencies and integration](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L23-L59); [Main.java, lines 30-116](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/Main.java#L30-L116).

## 9. Protected Variations

**Verdict: `violation`**

**Finding: `V-01`.**

The warranty-eligibility rule is not protected behind a stable capability or policy contract. Instead, both the UI and sale service directly name the concrete `Console` subtype. A predictable change to which product kinds may receive a warranty would require changes in both layers, increasing the chance that the prompt and actual assignment logic diverge.

This pattern assessment refers to the same underlying finding as Pattern 6. It does not create a second correction task.

**Evidence:** [UI.java, lines 400-424](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L400-L424); [SaleService.java, lines 147-182](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L147-L182).

## Findings summary

### Detailed finding record

#### `V-01` - Duplicated concrete-type decision for extended-warranty eligibility

- **Pattern(s):** Low Coupling; High Cohesion; Polymorphism; Protected Variations.
- **Module / requirement:** Sales and UI integration, spanning Workshop 1 and the accessory/promotion/warranty integrations (Requirements 1, 2, and 4).
- **Evidence:** `UI.registerSale()` checks `product instanceof Console` before prompting for an extended warranty ([UI.java, lines 400-424](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L400-L424)). `SaleService.registerSale()` separately checks `product instanceof Console` before assigning basic and optional extended warranties ([SaleService.java, lines 147-182](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L147-L182)).
- **Explanation:** The UI repeats a concrete product-type decision that also exists in the service. This introduces avoidable dependency on the concrete subtype and spreads one warranty-eligibility rule across two layers.
- **Consequence:** If warranty eligibility changes for another product type, or the rule changes, both locations would require synchronized edits. The console prompt and the business assignment logic could drift apart.
- **Proposed design solution:** Centralize the warranty-eligibility policy behind one stable operation or abstraction and have the UI ask the service/policy whether the prompt applies, instead of checking `Console` directly. Keep warranty creation and business validation in the service layer. The concrete refactoring choice must be confirmed in Phase 2 after consolidating the team's reports.
- **Code owned by another member:** No for the two cited locations. Both are in the Technical Leader's assigned scope for this report.
- **Correction owner:** Technical Leader, subject to final confirmation in the consolidated audit.

The four pattern references describe one underlying design issue, so they should be consolidated as one correction rather than four duplicate tasks. No application-code correction is made during Phase 1.


## Findings outside the GRASP refactor scope

Requirement 7 says to record functional defects separately and leave them unchanged during this audit/refactor requirement. The following observations are therefore not counted as GRASP violations and were not fixed.

### `LEAD-OUT-01` - Sales history displays the subtotal as the total

The three sales-history operations in `UI.java` display `sale.calculateTotal()`: `listAllSales()`, `listCustomerSales()`, and `listSellerSales()`. However, the `Sale` API distinguishes the subtotal from `calculateFinalTotal()`, which subtracts the discount and adds extended-warranty cost. The displayed amount may therefore differ from the final sale amount for discounted or warranty-bearing sales.

**Evidence:** [UI.java, lines 457-473](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L457-L473); [UI.java, lines 502-521](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L502-L521); [UI.java, lines 550-569](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L550-L569); [Sale.java, lines 136-154](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Sale.java#L136-L154).

### `LEAD-OUT-02` - Sale registration has a partial-persistence risk

Within `SaleService.registerSale()`, warranty records are persisted through `WarrantyService`, stock updates are persisted through the product/accessory services, and only afterward is the sale saved through `SaleRepository.saveAll(sales)`. If a later persistence operation fails, earlier side effects may already have been stored. This is recorded as a failure-path consistency risk to verify in a later requirement, not as a GRASP correction in this phase.

**Evidence:** [SaleService.java, lines 147-202](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L147-L202); [WarrantyService.java, warranty persistence operations](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/WarrantyService.java#L45-L93); [SaleRepository.java, saveAll](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/persistence/SaleRepository.java#L20-L39).

## Audit completion and limitations

- All nine required GRASP patterns have a verdict for the scoped leader code.
- One underlying design finding, `V-01`, is cross-referenced under four patterns so it can be consolidated and corrected only once.
- The two observations listed outside scope remain unchanged.
- This file is the **individual leader audit** and preserves its findings and evidence. The Developer 1 and Developer 2 reports have since been approved and merged into `develop`; final team verdicts, unified finding IDs, cross-boundary assignments, and correction ownership are recorded in [`consolidated-audit.md`](consolidated-audit.md). Where this individual report's local finding IDs differ from the final team IDs, the consolidated report is authoritative.
- This phase changes documentation only. Runtime behavior and application code have not been refactored.


## Verdict matrix for consolidation

| GRASP pattern | Verdict | Finding reference |
|---|---|---|
| Information Expert | `correct` | No separate violation identified in this scope. |
| Creator | `correct` | No separate violation identified in this scope. |
| Controller | `correct` | No separate violation identified in this scope. |
| Low Coupling | `violation` | `V-01` |
| High Cohesion | `violation` | `V-01` |
| Polymorphism | `violation` | `V-01` |
| Pure Fabrication | `correct` | No separate violation identified in this scope. |
| Indirection | `correct` | No separate violation identified in this scope. |
| Protected Variations | `violation` | `V-01` |

