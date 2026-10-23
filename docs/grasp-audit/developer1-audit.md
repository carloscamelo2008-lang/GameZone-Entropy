# Requirement 7 - GRASP Audit: Developer 1 Scope

## Audit baseline and scope

- **Repository:** `carloscamelo2008-lang/GameZone-Entropy`
- **Working branch:** `feature/grasp-audit`
- **Baseline audited:** `develop` at commit `f6f35d676e8a29cdddabb28a093a1158175aa266`
- **Phase:** Requirement 7, Phase 1 (audit only).
- **Role coverage:** Developer 1's assigned scope. This report is being completed as a team contingency by the technical leader; the Git commit remains attributed to the account that publishes it.
- **Audit rule:** findings refer to the baseline above, not to future refactoring ideas. No application code is modified by this report.

### Classes and methods in scope

The audit is organized by the module/requirement that introduced or extended the responsibility.

- **Product management:** `Product`, `VideoGame`, `Console`, `ProductRepository`, and `ProductService` for product registration, lookup, stock checks, and stock reduction. `ProductService.restoreStock()` is excluded because the responsibility map assigns it to the technical leader.
- **Accessory model hierarchy:** `Accessory`, `Controller`, `Cable`, and `Memory`. Accessory service and repository classes are outside this role's assigned scope.
- **Promotion models:** `Promotion`, `PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount`. Promotion service and persistence classes are outside this role's assigned scope.
- **Returns:** `Return` and only `Sale.canBeReturned()`. Other sale responsibilities, `ReturnService`, and `ReturnRepository` are excluded.
- **Warranty models:** `Warranty`, `BasicWarranty`, and `ExtendedWarranty`. Warranty service and persistence classes are outside this role's assigned scope.
- **Validation and exceptions:** `ProductValidator`, `PersonValidator`, `SaleValidator`, `GameZoneException`, `InvalidDataException`, `BusinessRuleException`, `ResourceNotFoundException`, and `PersistenceException`.

## 1. Information Expert

**Verdict: `correct`**

- `Product` owns the common product state, while `VideoGame.getDescription()` and `Console.getDescription()` use subtype-specific attributes to build the corresponding descriptions. This puts product-description behavior with the data it needs.
- `Return.calculateRefundAmount()` calculates the refund from the returned products and the original sale's discount information, both of which are available from the `Return` object.
- `Sale.canBeReturned()` evaluates the return-period rule using the sale's own date. That rule belongs with the sale information rather than in a caller that would need to inspect the date independently.
- `ProductService` and `ProductValidator` coordinate inventory operations and validate requested values; their use of a service/validator does not, by itself, establish an Information Expert violation.

**Evidence:** [Product.java, lines 13-18 and 105-116](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Product.java#L13-L18); [VideoGame.java, lines 86-98](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/VideoGame.java#L86-L98); [Console.java, lines 83-99](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Console.java#L83-L99); [Return.java, lines 123-147](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Return.java#L123-L147); [Sale.java, lines 175-185](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Sale.java#L175-L185).

## 2. Creator

**Verdict: `correct`**

- `ProductService.registerVideoGame()` creates a `VideoGame`, and `ProductService.registerConsole()` creates a `Console`. The service receives the input fields and maintains the collection in which each object is registered.
- The abstract promotion and warranty classes define shared data and behavior, while their concrete subclasses provide the appropriate variant. In-scope product registration demonstrates a clear, evidence-backed Creator assignment; creation of returns and warranties in service workflows is outside this role's service scope.

**Evidence:** [ProductService.java, lines 106-133](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/ProductService.java#L106-L133); [ProductService.java, lines 135-164](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/ProductService.java#L135-L164); [Promotion.java, lines 11-18 and 114-121](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Promotion.java#L11-L18); [Warranty.java, lines 14-35](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Warranty.java#L14-L35).

## 3. Controller

**Verdict: `not applicable` within this report's scope**

The primary console event handler is `UI`, which belongs to the technical leader's scope. The product, promotion, return, warranty, validation, and exception classes covered here are domain objects or supporting components, not the application's menu event handler. This is a scope-limited verdict, not a claim that the system has no Controller.

**Evidence:** The assigned scope excludes `UI.java`; the scoped `ProductService.registerVideoGame()` and `registerConsole()` perform product operations rather than reading or routing console menu events. [ProductService.java, lines 106-164](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/ProductService.java#L106-L164).

## 4. Low Coupling

**Verdict: `violation`**

**Finding: `V-01` (same underlying finding as Polymorphism and Protected Variations).**

`CategoryDiscount.matchesTargetCategory()` branches directly on the concrete types `VideoGame` and `Console`. For the `ACCESSORY` category, it classifies every product that is neither a `VideoGame` nor a `Console` as an accessory. Consequently, category-discount behavior is coupled to the current set of concrete product types and must be edited whenever the product taxonomy changes. The fallback can also silently classify a future, unrelated `Product` subtype as an accessory.

This finding is limited to this concrete type-classification decision. The normal dependency of `ProductService` on `ProductRepository` is not itself considered a violation, because the service delegates persistence to the repository as intended.

**Evidence:** [CategoryDiscount.java, lines 90-115](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/CategoryDiscount.java#L90-L115).

## 5. High Cohesion

**Verdict: `correct`**

- `ProductService` registers and looks up products and coordinates their stock updates. These responsibilities belong to product/inventory management; `restoreStock()` itself is excluded from this audit, but its exclusion does not make the remaining product service responsibilities unrelated.
- Each concrete promotion class implements one promotion variant, and `CategoryDiscount` calculates a discount for its category. Type classification is part of that category-specific discount rule, even though its current implementation has the extension weakness recorded as `V-01`.
- `Return` groups return data, refund calculation, and the return receipt. `Warranty` groups warranty dates/status, type-specific details through abstract methods, and certificate generation.
- The evidence supports cohesion for these responsibilities; no separate High Cohesion violation is asserted merely because a class has multiple methods.

**Evidence:** [ProductService.java, lines 38-101 and 106-164](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/ProductService.java#L38-L101); [CategoryDiscount.java, lines 76-115](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/CategoryDiscount.java#L76-L115); [Return.java, lines 20-42 and 123-187](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Return.java#L20-L42); [Warranty.java, lines 73-119](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Warranty.java#L73-L119).

## 6. Polymorphism

**Verdict: `violation`**

**Finding: `V-01` (also reported under Low Coupling and Protected Variations).**

The model contains appropriate polymorphic extension points: `Product.getDescription()` is abstract and implemented by `VideoGame` and `Console`; `Promotion.calculateDiscount()` is abstract and implemented by the concrete promotion types; and `Warranty` declares type-specific methods implemented by `BasicWarranty` and `ExtendedWarranty`.

However, `CategoryDiscount.matchesTargetCategory()` does not use a polymorphic category contract. It explicitly checks concrete types with `instanceof` and infers the accessory category by exclusion. That leaves category behavior outside the product subtype contract and causes the extension risk documented in `V-01`.

**Evidence:** [Product.java, lines 105-116](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Product.java#L105-L116); [VideoGame.java, lines 86-98](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/VideoGame.java#L86-L98); [Console.java, lines 83-99](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Console.java#L83-L99); [Promotion.java, lines 114-121](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Promotion.java#L114-L121); [Warranty.java, lines 85-99](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Warranty.java#L85-L99); [CategoryDiscount.java, lines 104-115](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/CategoryDiscount.java#L104-L115).

## 7. Pure Fabrication

**Verdict: `correct`**

- `ProductRepository` isolates Java object serialization and file access from `Product` and its subclasses.
- `ProductValidator` and `SaleValidator` centralize reusable validation rules outside domain constructors and the UI.
- The application-specific exception hierarchy separates persistence failures, invalid input, missing resources, and business-rule violations without placing error-classification infrastructure inside product and return models.

**Evidence:** [ProductRepository.java, lines 10-20 and 26-60](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/persistence/ProductRepository.java#L10-L20); [ProductValidator.java, lines 13-25 and 33-89](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/validation/ProductValidator.java#L13-L25); [SaleValidator.java, lines 13-23 and 29-81](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/validation/SaleValidator.java#L13-L23); [GameZoneException.java, lines 8-35](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/exceptions/GameZoneException.java#L8-L35).

## 8. Indirection

**Verdict: `correct`**

- `ProductService` mediates between callers and `ProductRepository`: registration and stock operations update the managed collection and then ask the repository to persist it.
- `ProductService` delegates field and range rules to `ProductValidator`, keeping validation policy from being duplicated in each registration method.
- The exception subclasses provide a typed interface between lower-level operations and callers. In particular, `PersistenceException` allows storage failures to be reported without exposing raw I/O handling as part of product-domain behavior.

**Evidence:** [ProductService.java, lines 70-101 and 106-164](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/service/ProductService.java#L70-L101); [ProductValidator.java, lines 89-197](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/validation/ProductValidator.java#L89-L197); [PersistenceException.java, lines 5-30](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/exceptions/PersistenceException.java#L5-L30).

## 9. Protected Variations

**Verdict: `violation`**

**Finding: `V-01` (same underlying finding as Low Coupling and Polymorphism).**

The accessory category is inferred by excluding two known product types instead of explicitly identifying the category. A future `Product` subtype (for example, a service plan or collectible) would be treated as an accessory by `matchesTargetCategory()` unless the method were edited. This is an observable extension hazard, not evidence that the current `VideoGame`, `Console`, or `Accessory` types are misclassified today.

**Proposed design-level solution:** introduce an explicit category contract on `Product` (such as a small enum returned by `getPromotionCategory()`) and implement it in `VideoGame`, `Console`, and `Accessory`. Then let `CategoryDiscount` compare the declared category rather than infer it from negative `instanceof` checks. If the team chooses a smaller change, at minimum replace the fallback with an explicit `product instanceof Accessory` check and return false for unknown product types. This recommendation is for Phase 2 only.

**Evidence:** [CategoryDiscount.java, lines 90-115](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/CategoryDiscount.java#L90-L115); [Accessory.java, lines 13-29](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Accessory.java#L13-L29).

## Findings summary

| ID | GRASP pattern(s) | Component and evidence | Consequence | Proposed solution | Correction owner |
|---|---|---|---|---|---|
| `V-01` | Low Coupling, Polymorphism, Protected Variations | `CategoryDiscount.matchesTargetCategory()`, especially lines 104-115 | Category behavior depends on concrete product subclasses; a future unrelated `Product` subtype may be treated as an accessory and receive an unintended category discount | Add an explicit product-category contract or at minimum an explicit `Accessory` check with an unknown-type default of false | Developer 1 scope, subject to leader consolidation |

## Findings outside the GRASP refactor scope

- **`DEV1-OUT-01` - Return receipt text encoding:** `Return.generateReturnReceipt()` contains the literal text `"Reembolso por garant?as canceladas"` in the warranty-refund label. It may display a corrupted Spanish word in the receipt. This is a text/encoding defect, not a demonstrated GRASP violation, so no behavior change is made during Phase 1. Evidence: [Return.java, lines 168-177](https://github.com/carloscamelo2008-lang/GameZone-Entropy/blob/f6f35d676e8a29cdddabb28a093a1158175aa266/src/main/java/com/gamezone/model/Return.java#L168-L177).

## Verdict matrix

| GRASP pattern | Verdict | Finding |
|---|---|---|
| Information Expert | correct | Domain information and calculations remain with Product, Sale, Return, Promotion, and Warranty where supported by the evidence. |
| Creator | correct | ProductService creates product objects using the data it receives and manages. |
| Controller | not applicable | The UI controller is outside this role's assigned scope. |
| Low Coupling | violation | V-01 |
| High Cohesion | correct | No separate cohesion violation is established in the assigned scope. |
| Polymorphism | violation | V-01 |
| Pure Fabrication | correct | Repository, validator, and exception support classes separate technical concerns from domain models. |
| Indirection | correct | Services/validators mediate between callers, business operations, persistence, and typed error handling. |
| Protected Variations | violation | V-01 |

This report completes only the individual audit for the assigned scope. It does not authorize or implement Phase 2 refactoring. The technical leader should compare this finding with the other individual reports, resolve duplicate or conflicting findings, and consolidate the final conclusions.
