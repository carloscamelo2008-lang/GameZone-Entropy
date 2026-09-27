# Accessory Module — Analysis

## 1. Should accessories extend the existing Product hierarchy or form an independent one?

Accessories extend `Product`. Controllers, cables and memories are sellable
items with the same core attributes as any other product (id, title, price,
stock), and they must be included in the same sales flow (`SaleService`,
stock validation, receipt generation). Extending `Product` reuses that
behavior directly through inheritance and polymorphism, instead of
duplicating it in a parallel hierarchy. An independent hierarchy would force
`SaleService` and `Sale` to handle two unrelated item types, breaking the
existing design instead of extending it additively.

## 2. Common vs. specific attributes

Common attributes (inherited from `Product`): id, title, price and stock.
`Accessory` itself adds one attribute common to all three accessory types:
the list of compatible console ids. Type-specific attributes are declared
only in the concrete subclasses:

- `Controller`: connection type (wireless/wired).
- `Cable`: length in meters, connector type.
- `Memory`: capacity in gigabytes, memory type.

This distinction is reflected in the hierarchy by keeping shared state in
the abstract `Accessory` class and pushing only the differing state and
`getDescription()` behavior down into each concrete subclass.

## 3. How is console compatibility represented?

Compatibility is modeled as an attribute of the accessory: a list of
console ids stored inside `Accessory`. It is a one-directional association
(accessory → console ids), not a bidirectional relationship between
`Accessory` and `Console` objects. This keeps `Console` unaware of
accessories, avoids a circular dependency between the two classes, and is
simple to persist as a single delimited field in the CSV file.

## 4. Required changes to SaleService

Because `Accessory` extends `Product`, `SaleService.registerSale` does not
need a separate code path for validating or calculating totals: a sale's
item list can already contain both regular products and accessories through
polymorphism. However, three concrete integration points do need to change:

- **Item resolution**: `SaleService` currently resolves each item id only
  through `ProductService`. To support accessories, it must also attempt to
  resolve the id through `AccessoryService` when it is not found among
  products (or vice versa), so an accessory id passed when registering a
  sale is recognized instead of rejected.
- **Stock validation**: before building the sale, `SaleService` currently
  checks sufficient stock only through `ProductService.hasSufficientStock`.
  This validation must also be routed to `AccessoryService.hasSufficientStock`
  when the resolved item is an accessory, so a sale is rejected only when
  the correct service reports insufficient stock.
- **Stock reduction**: after the sale is confirmed, the method must check the
  runtime type of each resolved item and delegate to
  `ProductService.reduceStock` for products or `AccessoryService.reduceStock`
  for accessories, instead of assuming every item is a plain `Product`.

## 5. Which layer do the new classes belong to?

- `Accessory`, `Controller`, `Cable`, `Memory` → **model** layer, since they
  represent domain entities with no file or persistence logic.
- `AccessoryRepository` → **persistence** layer, since it is the only class
  allowed to read and write `data/accessories.csv`.
- `AccessoryService` → **service** layer, since it contains the business
  rules for registering and querying accessories, sitting between the UI
  and the repository.

This keeps the existing `ui → service → persistence → model` dependency
direction intact, with no new class breaking that boundary.
