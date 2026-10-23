# Requirement 7 - GRASP Analysis (Phase 1)

This document records the team's answers to the five guiding questions for the GRASP audit. The audit refers to source-code baseline `f6f35d676e8a29cdddabb28a093a1158175aa266`. These are Phase 1 analysis decisions; implementation remains deferred until the consolidated audit is merged into `develop`.

## 1. How did the team decide whether a code fragment is a violation, correct, or not applicable? What minimum evidence must accompany a verdict?

The team uses the GRASP pattern's responsibility question as the test, rather than treating a long class, a dependency, or an `instanceof` expression as automatically incorrect. A **violation** requires a specific design decision in code, a clear link to the relevant GRASP principle, and a concrete current or foreseeable consequence. A **correct** verdict identifies the class and method where the responsibility is appropriately assigned. **Not applicable** is used only when the audited scope has no meaningful point of application for that pattern; it must include a scope-specific explanation.

For a violation to be accepted into the consolidated audit, the report must include:
- a stable finding ID and the pattern or patterns involved;
- the module/requirement and the exact class/method/line range, or a baseline permalink;
- an explanation of why the evidence violates the pattern;
- a concrete consequence, such as change propagation, duplicated policy, inappropriate responsibility, or an unhandled failure;
- a proposed design-level solution and the pattern it would apply;
- whether the correction touches another member's code and who owns each part.

All citations are pinned to the same `develop` source-code baseline. Multiple pattern labels for one underlying defect are cross-referenced under a single final finding ID instead of being counted as duplicate correction tasks.

## 2. What cross-boundary findings did the team identify, how are they recorded, and who owns the correction?

Two findings require explicit coordination across role boundaries:

- **V-02, monthly reporting versus return transaction workflow:** Developer 2 owns `ReturnService`, including the current monthly-balance feature, while the sales data is obtained from the Technical Leader's `SaleService`. The consolidated report records one High Cohesion finding. Developer 2 owns the reporting-component extraction; the Technical Leader coordinates any changes needed to the sales-query interface or object wiring.
- **V-04, warranty eligibility:** the product hierarchy (`Product`/`Console`) is Developer 1's responsibility, while the duplicate checks are in the Technical Leader's `UI.registerSale()` and `SaleService.registerSale()`. The consolidated report assigns the product-side eligibility capability to Developer 1 and the UI/service integration changes to the Technical Leader.

The remaining findings are contained within a single owner: V-01 is in the Developer 1 promotion model, and V-03 is in the Developer 2 accessory repository. The consolidated audit lists each final ID, its origin report, and its correction owner so responsibility is not ambiguous. Cross-owner work must be sequenced and reviewed by the Technical Leader; each contributor modifies only the files assigned to them.

## 3. Did any fragments violate more than one GRASP pattern? How were duplicates avoided, and how was the guiding pattern chosen?

Yes. The team retained three examples of one underlying issue affecting multiple patterns:

- **V-01, `CategoryDiscount.matchesTargetCategory()`:** the category fallback uses negative concrete-type checks. It primarily concerns Protected Variations because future product types can be misclassified; Low Coupling and Polymorphism describe related aspects of the same code.
- **V-04, warranty eligibility checks:** the UI and sale service repeat a `Console` type check. Polymorphism is the primary design direction, with Low Coupling, High Cohesion, and Protected Variations describing the consequences of spreading one eligibility rule across two layers.
- **V-02, monthly reporting in `ReturnService`:** the main issue is High Cohesion, because transaction lifecycle behavior and cross-module reporting create separate reasons to change. Information Expert is marked correct: aggregation in a service is not a violation by itself, and the service accesses the records required for the calculation.

The team assigned one final ID per underlying issue, and the same ID is referenced under each relevant pattern. The primary pattern is selected according to the design change that best addresses the root cause: explicit category variation for V-01, responsibility separation for V-02, deliberate data-error handling for V-03, and a polymorphic warranty-eligibility capability for V-04. This avoids duplicate refactor tasks while retaining the full pattern analysis.


## 4. How will the team preserve external behavior and prove that the refactor did not break any menu operation?

No refactoring has been performed in Phase 1. Before the first Phase 2 correction, the team will establish a reference result on the then-current `develop` baseline. The reference will combine compilation, any automated tests that exist, and a manual menu-operation matrix.

The baseline verification should:
1. Run `mvn clean compile` and `mvn test`; record the command output and whether automated tests were actually discovered and executed.
2. Exercise the main menu and every submenu operation using fixed, documented input data. At minimum, the matrix must cover product registration/listing; customer and seller operations; accessory management and compatibility/stock; sale registration with and without a promotion and warranty; all sales-history filters; returns including the monthly balance; invalid menu/numeric input; and the typed exception paths that can be reproduced safely.
3. Record, for each scenario, the input, expected user-visible result/message, relevant inventory or balance outcome, and any affected data-file contents. Preserve a clean starting data set so comparisons are repeatable.
4. Before each Phase 2 PR, rerun the scenarios affected by that PR and compare the outputs and persistent data to the baseline. At the end of Phase 2, repeat the full menu matrix and the compilation/test commands.
5. Store the test strategy and initial/final evidence in `docs/grasp-refactor-log.md`, including command output or reproducible transcripts. If there is no automated test coverage for an operation, the manual evidence must be explicit rather than treating a successful build as proof of no regression.

The refactor must preserve normal observable behavior: identical valid inputs should continue to produce the same user-facing results/messages and the same data-file contents. For corrupted persisted data (V-03), the team must first agree on the intended error policy and document the expected behavior; it must not silently change ordinary successful workflows while improving parse-failure handling.

## 5. In what order will violations be corrected, and how will shared work avoid conflicts?

The consolidated order is V-03, V-02, V-01, then V-04:

1. **V-03 (Developer 2):** make the accessory CSV parsing policy explicit and add regression coverage for malformed numeric records. This is contained in the repository scope and can be reviewed independently.
2. **V-02 (Developer 2, coordinated with the Technical Leader):** extract monthly reporting/aggregation from `ReturnService`. Agree on the sales-data interface with the Technical Leader before changing wiring, while keeping the final output and balance behavior unchanged.
3. **V-01 (Developer 1):** introduce the explicit product-category contract and use it in `CategoryDiscount`. Keep the change limited to promotion category determination.
4. **V-04 (Developer 1 + Technical Leader):** add the product-side warranty capability first, then update `UI.registerSale()` and `SaleService.registerSale()` to use that stable contract instead of duplicate concrete-type checks.

This order starts with contained changes, separates monthly reporting from return transactions, and then handles the product model decisions in an order that reduces repeated edits to `Product` and its subclasses. V-01 (promotion category) and V-04 (warranty eligibility) are different policies, so they must remain separate IDs and correction activities even though both involve the product model.

To prevent branch conflicts, all contributors will work from the shared Phase 2 branch after the consolidated Phase 1 PR is merged. The Technical Leader will sequence cross-owner changes; Developer 1 will make product-model changes, Developer 2 will make return/reporting and repository changes, and the Technical Leader will integrate UI/sales-flow changes. Each correction is committed atomically, pushed promptly, and reviewed in its own PR or clearly bounded commit group. No contributor changes another owner's files without coordination: the assigned owner makes that portion of the correction.
