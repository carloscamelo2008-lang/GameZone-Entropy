# Requirement 7 - GRASP Audit: Technical Leader

## Audit baseline and scope

- **Repository:** `carloscamelo2008-lang/GameZone-Entropy`
- **Branch:** `feature/grasp-audit`
- **Baseline:** `develop` at `f6f35d676e8a29cdddabb28a093a1158175aa266`
- **Phase:** Requirement 7, Phase 1 (audit only). No application code has been changed as part of this audit.

### Classes and methods in scope

1. `Sale`: sale state, promotion fields, subtotal/final-total calculations, and receipt generation. **`canBeReturned()` is excluded** because the Requirement 7 responsibility map assigns that method to Developer 1.
2. `SaleRepository`: `saveAll()` and `loadAll()`.
3. `SaleService`: `registerSale()`, item lookup, sale queries, and integration with products, accessories, promotions, warranties, and persistence.
4. `UI.java` (the console-menu implementation named `ConsoleMenu` in the requirement): menu routing, sale flow, module interactions, and typed exception handling.
5. `Main`: application wiring and startup.
6. `ProductService.restoreStock()`.

The audit covers the nine GRASP patterns named in the requirement. Each verdict is based on the scoped source code. Potential functional defects are recorded separately because Requirement 7 says not to change behavior during the audit phase.

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

**Finding: `LEAD-GRASP-01` (shared with Patterns 5, 6, and 9).**

The avoidable coupling is concentrated in the console sale interaction. `UI.registerSale()` queries `ProductService.listAllProducts()`, inspects each concrete product as a `Console`, and uses that type check to decide whether to prompt for an extended warranty. `SaleService.registerSale()` independently performs another `instanceof Console` check before assigning warranties. This makes the UI depend on a concrete domain subtype and duplicates a rule that affects the sale workflow.

The existence of multiple collaborators in `SaleService` is not treated as a violation by itself: the requirement explicitly assigns the service responsibility for coordinating products, accessories, promotions, and warranties. The supported coupling finding is the duplicated concrete-type decision across the UI and service.

**Evidence:** [UI.java, lines 400-424](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L400-L424); [SaleService.java, lines 147-182](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L147-L182).

## 5. High Cohesion

**Verdict: `violation`**

**Finding: `LEAD-GRASP-01`.**

The console UI's main role is to gather input, route commands, and display results. However, `UI.registerSale()` also retrieves inventory records and decides warranty eligibility by checking the product's concrete type. That policy-level decision overlaps with the same decision inside `SaleService.registerSale()`. The issue is not that one console menu exposes several application operations; it is that this sale handler mixes presentation with domain-type policy and duplicates the service's decision.

**Evidence:** [UI.java, lines 364-450](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L364-L450); [SaleService.java, lines 147-182](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L147-L182).

## 6. Polymorphism

**Verdict: `violation`**

**Finding: `LEAD-GRASP-01`.**

The workflow selects warranty behavior with explicit type checks:

- `UI.registerSale()` checks `product instanceof Console` before asking whether an extended warranty is wanted.
- `SaleService.registerSale()` checks `product instanceof Console` before assigning basic and optional extended warranties.

This is behavior that varies by product subtype, but the variation is encoded as conditionals in two classes rather than exposed through one polymorphic eligibility contract or a single warranty-policy abstraction. If another product subtype becomes eligible for warranties, both locations would need coordinated edits.

**Evidence:** [UI.java, lines 400-424](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L400-L424); [SaleService.java, lines 147-182](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L147-L182).
