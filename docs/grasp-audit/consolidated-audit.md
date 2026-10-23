# Requirement 7 - Consolidated GRASP Audit

## 1. Audit baseline and status

- **Repository:** `carloscamelo2008-lang/GameZone-Entropy`
- **Source-code baseline audited:** `develop` at `f6f35d676e8a29cdddabb28a093a1158175aa266`
- **Current documentation baseline:** `develop` after the reviewed Developer 2 and Developer 1 audit PRs were merged (head `5a90ea7c3e3c316e197b76c3dbe2d45b1d3e2350`).
- **Phase:** Requirement 7, Phase 1 (audit only).
- **Scope constraint:** No application source code was changed during the audit. Findings outside GRASP scope are recorded separately and are not fixed in this requirement.

All code evidence in the individual reports points to the same original source-code baseline. The two subsequent merges added audit documentation only, so the source-code baseline remains applicable.

## 2. Coverage matrix

The following table consolidates the role allocation in the requirement and the three individual reports. A method assigned explicitly to a different contributor is covered by that contributor even when its class appears in another module.

| Module / responsibility | Coverage | Audited classes and methods |
|---|---|---|
| Product management | Developer 1, with one explicit leader-owned method | `Product`, `VideoGame`, `Console`, `ProductRepository`, `ProductService`; `ProductService.restoreStock()` is audited by the Technical Leader |
| People | Developer 2 | `Person`, `Customer`, `Seller`, `PersonRepository`, `PersonService` |
| Sales and startup | Technical Leader | `Sale` except `Sale.canBeReturned()); `SaleRepository`, `SaleService`, `UI` (console menu), `Main` |
| Accessory model hierarchy | Developer 1 | `Accessory`, `Controller`, `Cable`, `Memory` |
| Accessory service and persistence | Developer 2 | `AccessoryService`, `AccessoryRepository` |
| Promotion model hierarchy | Developer 1 | `Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount` |
| Promotion service and persistence | Developer 2 | `PromotionService`, `PromotionRepository` |
| Returns and monthly balance | Split by responsibility | Developer 1: `Return` and `Sale.canBeReturned()`; Developer 2: `ReturnService`, `ReturnRepository`; monthly reporting in `ReturnService` is included in the Developer 2 scope |
| Warranty model hierarchy | Developer 1 | `Warranty`, `BasicWarranty`, `ExtendedWarranty` |
| Warranty service and persistence | Developer 2 | `WarrantyService`, `WarrantyRepository` |
| Exceptions and validation | Split by use and ownership | Developer 1 audits the exception and validation classes; Developer 2 audits their use in services/repositories; the Technical Leader audits UI exception routing and startup handling |

### Individual reports consolidated

- `docs/grasp-audit/leader-audit.md`
- `docs/grasp-audit/developer1-audit.md`
- `docs/grasp-audit/developer2-audit.md`

The reports cover all nine required GRASP patterns. Where an individual report uses a local finding ID such as `V-01`, this consolidated report assigns one final team ID per distinct underlying issue. Local IDs are retained in the finding origins below for traceability.

## 3. Team verdict matrix

| GRASP pattern | Team verdict | Supporting reports / findings |
|---|---|---|
| Information Expert | `correct` | All three reports. Monthly total aggregation is retained as an appropriate service responsibility; the separate reporting concern is assessed under High Cohesion instead. |
| Creator | `correct` | All three reports identify appropriate construction responsibilities or find no conflicting creator assignment in scope. |
| Controller | `correct` | The leader's `UI` routes menu commands and delegates work. Developer reports mark this pattern not applicable within their narrower scopes. |
| Low Coupling | `violation` | Developer 1: category inference in `CategoryDiscount` (local `V-01`); Leader: duplicated concrete warranty eligibility checks (local `V-01`). |
| High Cohesion | `violation` | Developer 2: return lifecycle combined with cross-module monthly reporting (local `V-01`); Leader: UI mixes presentation with warranty eligibility policy (local `V-01`). |
| Polymorphism | `violation` | Developer 1: `CategoryDiscount` uses concrete-type checks (local `V-01`); Leader: warranty eligibility varies by `Console` but is checked explicitly in two locations (local `V-01`). Correct polymorphic dispatch in Developer 2's services does not cancel violations elsewhere. |
| Pure Fabrication | `correct` | All three reports identify repositories, validators, services, or typed exceptions as appropriate supporting components. |
| Indirection | `correct` | All three reports identify service/repository/validator layers that mediate between callers and technical concerns. |
| Protected Variations | `violation` | Developer 1: category classification inferred by exclusion (local `V-01`); Developer 2: unguarded numeric parsing in `AccessoryRepository` (local `V-02`); Leader: duplicated concrete warranty eligibility checks (local `V-01`). |

## 4. Final consolidated violations

The next section assigns the definitive team IDs, consolidates duplicate pattern references into one work item per underlying issue, and assigns correction responsibility.


### V-01 - Promotion category inferred from negative concrete-type checks

- **Origin:** `developer1-audit.md`, local finding `V-01`.
- **Primary pattern:** Protected Variations.
- **Related patterns:** Low Coupling and Polymorphism.
- **Module / requirement:** Promotion model hierarchy / Requirement 2, interacting with the product hierarchy.
- **Evidence:** `CategoryDiscount.matchesTargetCategory()` checks `VideoGame` and `Console` explicitly, then treats any product that is neither as an accessory. See [CategoryDiscount.java, lines 104-115](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/CategoryDiscount.java#L104-L115) and [Accessory.java, lines 13-29](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Accessory.java#L13-L29).
- **Explanation:** The `ACCESSORY` category is inferred by exclusion rather than determined through an explicit category contract. A future, unrelated `Product` subtype could therefore be classified as an accessory by default.
- **Consequence:** Extending the product hierarchy can silently change promotion eligibility and force changes to a promotion model that should depend on a stable category contract.
- **Agreed design direction:** Add an explicit category contract to `Product` (for example, a `ProductCategory` enum/accessor implemented by the concrete product types) and let `CategoryDiscount` compare the declared category. An explicit `Accessory` check with a safe default for unknown product types is the smaller alternative.
- **Correction owner:** Developer 1.
- **Severity:** Medium.

### V-02 - Return transaction workflow and monthly financial reporting share one service

- **Origin:** `developer2-audit.md`, local finding `V-01`.
- **Primary pattern:** High Cohesion.
- **Related pattern:** Information Expert was reviewed and is **not** considered violated by the consolidated team.
- **Module / requirement:** Return management / Requirement 3, including monthly balance reporting.
- **Evidence:** `ReturnService.registerReturn()` manages the return transaction at lines 68-138; `calculateMonthlySales()` aggregates sales from `SaleService` at lines 196-209; `calculateMonthlyReturns()` aggregates return refunds at lines 220-232; and `generateMonthlyBalance()` combines those totals at lines 244-246. See [ReturnService.java, lines 68-138](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/ReturnService.java#L68-L138), [lines 196-232](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/ReturnService.java#L196-L232), and [lines 244-246](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/ReturnService.java#L244-L246).
- **Explanation:** The class owns core transaction validation, stock restoration, warranty cancellation, persistence, and also a financial reporting calculation spanning sales and returns. These responsibilities create separate reasons to change. The fact that monthly balance is explicitly included in the current service's documentation is acknowledged; the finding concerns the separation of responsibilities, not a claim that the service lacks access to the required records.
- **Consequence:** Changes to reporting criteria or monthly aggregation can force edits to the same class that coordinates return registration and its side effects, increasing regression risk in the transaction workflow.
- **Agreed design direction:** Extract monthly aggregation/balance calculation into a dedicated component such as `MonthlyBalanceCalculator` or `ReturnReportingService`. Keep return creation, validation, stock restoration, warranty cancellation, and persistence coordination in `ReturnService`.
- **Correction owner:** Developer 2. Because the calculation obtains sales through the leader-owned `SaleService`, the Technical Leader coordinates any required API/wiring change. The extraction should not duplicate sale state or alter existing balance outputs.
- **Severity:** Medium.

### V-03 - Accessory CSV numeric parsing exposes callers to raw parsing exceptions

- **Origin:** `developer2-audit.md`, local finding `V-02`.
- **Primary pattern:** Protected Variations.
- **Module / requirement:** Accessory persistence / Requirement 1.
- **Evidence:** `AccessoryRepository.fromCsvLine()` parses price and stock at lines 149-150 and parses the cable length or memory capacity at lines 161-164 without handling `NumberFormatException`. See [AccessoryRepository.java, lines 140-176](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/persistence/AccessoryRepository.java#L140-L176). For contrast, `ReturnRepository.fromCsvLine()` specifically catches `DateTimeParseException` for the date and skips that invalid record at lines 141-146; this is not a general handler for every possible parse failure. See [ReturnRepository.java, lines 134-163](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/persistence/ReturnRepository.java#L134-L163).
- **Explanation:** A malformed numeric field can escape the repository as an unhandled runtime parse error rather than following a deliberate, documented invalid-record policy.
- **Consequence:** A corrupted accessory record can abort accessory loading and propagate an implementation-specific exception to a caller.
- **Agreed design direction:** Establish one explicit policy for malformed persisted records and apply it to numeric parsing. Handle `NumberFormatException` deliberately, with record/file context; either skip a malformed record consistently or translate the failure to the application's persistence exception contract, based on the behavior agreed by the team. Add a regression case for malformed numeric content.
- **Correction owner:** Developer 2.
- **Severity:** Medium.

### V-04 - Warranty eligibility is checked through duplicate concrete-type conditionals

- **Origin:** `leader-audit.md`, local finding `V-01`.
- **Primary pattern:** Polymorphism.
- **Related patterns:** Low Coupling, High Cohesion, and Protected Variations.
- **Module / requirement:** Sales and console UI integration / Requirements 1, 2, and 4.
- **Evidence:** `UI.registerSale()` checks `product instanceof Console` before presenting the extended-warranty prompt at [UI.java, lines 400-424](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L400-L424). `SaleService.registerSale()` repeats a `Console` type check before assigning warranties at [SaleService.java, lines 147-182](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L147-L182).
- **Explanation:** One business eligibility decision is repeated in the presentation layer and the sale orchestration service. The UI depends on a concrete subtype to decide which interaction to show, while the service separately decides which products receive warranties.
- **Consequence:** A change to warranty eligibility requires synchronized edits in two layers; the UI prompt and actual warranty assignment can drift.
- **Agreed design direction:** Define a stable product capability such as `supportsWarranty()` on the product model, implemented by the responsible product subclasses, and use it from the UI and `SaleService`. The UI should remain responsible for prompting/display; the service remains responsible for assigning warranties.
- **Correction owners:** Developer 1 owns the product-model capability; the Technical Leader owns the UI and `SaleService` integration and coordinates the order. This is one cross-owner finding and one team correction, not separate issues for each of the four patterns.
- **Severity:** Medium.

## 5. Boundary findings and coordination

### Cross-owner finding: V-02 monthly reporting boundary

`ReturnService` (Developer 2) obtains the sale collection through leader-owned `SaleService` to calculate monthly sales. This is not, by itself, a violation of Low Coupling; it is a coordination boundary for the High Cohesion correction. Developer 2 should own the reporting extraction. The Technical Leader reviews the existing sales-query interface and coordinates any required wiring changes. Changes to `SaleService` itself remain the Technical Leader's responsibility.

### Cross-owner finding: V-04 warranty eligibility contract

V-04 requires the product capability to be implemented in Developer 1's product hierarchy and consumed by the Technical Leader's UI/sale workflow. Developer 1 adds and tests the product-side capability; the Technical Leader replaces duplicated type checks in UI and `SaleService`. The leader coordinates the sequence so both sides are integrated in the shared refactor branch without parallel edits to the same files.

The other findings are contained within their assigned scope: V-01 is in the Developer 1 promotion model; V-03 is in the Developer 2 accessory repository. They do not require a second owner based on the current evidence.

## 6. Findings outside GRASP correction scope

The individual reports identified the following functional/text observations. They are not GRASP refactoring tasks in Requirement 7 and must remain unchanged in this requirement. They should be retained as evidence for a later requirement.

- **LEAD-OUT-01 - Sales history may display the subtotal instead of the final total.** The sales-history methods in `UI.java` call `sale.calculateTotal()`, while `Sale` also exposes `calculateFinalTotal()`. Evidence: [UI.java, lines 457-473](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L457-L473), [UI.java, lines 502-521](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L502-L521), [UI.java, lines 550-569](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/ui/UI.java#L550-L569), and [Sale.java, lines 136-154](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Sale.java#L136-L154).
- **LEAD-OUT-02 - Sale registration has a partial-persistence risk.** Warranty and stock side effects can be persisted before the sale record is saved. This is a failure-path consistency risk; it is not corrected as part of the GRASP audit. Evidence: [SaleService.java, lines 147-202](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/SaleService.java#L147-L202).
- **DEV1-OUT-01 - Return receipt label may contain corrupted text.** `Return.generateReturnReceipt()` contains `Reembolso por garant?as canceladas`. This is a text/encoding issue, not a GRASP violation. Evidence: [Return.java, lines 168-177](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Return.java#L168-L177).

## 7. Final correction ownership and recommended order

| Order | Final ID | Primary pattern | Owner | Coordination / dependency |
|---|---|---|---|---|
| 1 | V-03 | Protected Variations | Developer 2 | Contained repository correction; establish malformed-record policy and test it |
| 2 | V-02 | High Cohesion | Developer 2 | Coordinate sales collection access with the Technical Leader; extract balance calculation without changing outputs |
| 3 | V-01 | Protected Variations | Developer 1 | Explicit product-category contract used by `CategoryDiscount` |
| 4 | V-04 | Polymorphism | Developer 1 + Technical Leader | Developer 1 adds product capability; leader updates UI/sale integration afterward |

The order prioritizes contained persistence and reporting changes, then the model-policy changes. V-01 and V-04 both touch product-related design but represent different policies (promotion category versus warranty eligibility); they must remain separate correction IDs. The Technical Leader coordinates commits when an item crosses ownership boundaries.

## 8. Phase 1 completion notes

- Three individual reports have been reviewed and consolidated; local IDs have been mapped to four final team IDs.
- Related GRASP patterns are cross-referenced under one underlying finding rather than counted as separate fixes.
- Coverage and ownership have been checked against the role allocation described in the requirement.
- All defects outside the GRASP correction scope are recorded separately.
- No application-code changes, class-diagram before/after edits, or Phase 2 refactor-log entries are included in this Phase 1 consolidation.


## 9. Phase 1 gate checklist

- [x] Developer 2's individual audit has been approved and merged (PR #38).
- [x] Developer 1's individual audit has been approved and merged (PR #39).
- [x] The Technical Leader's individual audit is included for review.
- [x] The coverage matrix assigns the documented code responsibilities to the three roles.
- [x] Related pattern labels share one final finding ID per underlying issue.
- [x] Cross-owner corrections identify both the model/service-side owner and the coordination responsibility.
- [x] Functional observations outside the refactor scope are preserved separately.
- [x] The five guiding questions are answered in `docs/grasp-analysis.md`.
- [ ] Technical Leader's consolidated PR is cross-reviewed and approved.
- [ ] The consolidated Phase 1 audit is merged into `develop`.

Phase 2 remains blocked until the last two review/merge gates are complete. After that merge, the team can create `feature/grasp-refactor`, establish the before-diagram and baseline verification record, and begin the assigned corrections.
