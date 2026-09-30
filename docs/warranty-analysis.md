# Warranty Analysis

## Purpose

The warranty module manages the warranties associated with products sold by GameZone Unicesar.

## Warranty

`Warranty` is an abstract class containing the common information of all warranty types:

- `id`
- `product`
- `sale`
- `startDate`
- `endDate`

The end date is calculated using the duration supplied by the concrete warranty type.

It defines polymorphic operations for:

- warranty duration;
- warranty type;
- additional cost.

## BasicWarranty

`BasicWarranty` is automatically assigned to consoles.

It provides:

- 6 months of coverage;
- zero additional cost.

## ExtendedWarranty

`ExtendedWarranty` is optional.

It provides:

- 12 months of coverage;
- an additional cost equal to 10% of the covered product price.

## WarrantyService

`WarrantyService` coordinates:

- creation of basic warranties;
- creation of extended warranties;
- warranty lookup;
- active warranty queries;
- upcoming expiration queries;
- warranty cancellation.

The `cancelWarranties(String productId, String saleId)` operation removes all warranties associated with the specified product and sale and returns the refundable additional warranty cost.

## Persistence and Circular Dependency

The warranty integration originally created a circular dependency between the sales and warranty modules.

The integrated design avoids this dependency by making `WarrantyRepository` persist raw warranty identifiers.

`WarrantyService` receives:

- `WarrantyRepository`
- `SaleRepository`
- `ProductService`

and resolves the corresponding `Sale` and `Product` objects itself.

This keeps persistence responsibilities separated from service coordination.

## Integration with Returns

When a console is returned, `ReturnService` invokes `WarrantyService.cancelWarranties(...)`.

Basic warranties contribute zero to the refund, while extended warranties contribute their additional cost.