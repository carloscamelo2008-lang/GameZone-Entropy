```markdown
# Return Analysis

## Purpose

The return module manages product returns associated with previously registered sales.

A return references the original sale and contains the specific products being returned.

## Return

`Return` stores:

- `id`
- `date`
- `sale`
- `returnedProducts`
- `reason`
- `refundAmount`
- `warrantyRefundAmount`

Its responsibilities include:

- calculating the refund amount;
- storing additional refundable warranty costs;
- generating a detailed return receipt.

The refund for each returned product respects the proportional discount of the original sale.

Formula:

`price × (1 - discount / subtotal)`

The implementation safely handles a zero subtotal.

## Sale.canBeReturned

A sale is eligible for return when between 0 and 30 days have elapsed since the sale date.

Future-dated sales are rejected.

## ReturnService

`ReturnService` coordinates:

- return validation;
- sale lookup;
- ownership validation;
- quantity validation;
- product stock restoration;
- accessory stock restoration;
- warranty cancellation;
- monthly return calculations;
- persistence of returns.

Returned accessories are restored through `AccessoryService`, while other products are restored through `ProductService`.

## Partial Returns

The system allows partial returns.

The requested product identifiers are counted so that multiple units of the same product can be returned while preventing more units from being returned than were originally purchased or already returned.

## Warranty Integration

When a returned product is a console, `ReturnService` asks `WarrantyService` to cancel the warranties associated with that product and sale.

If an extended warranty existed, its additional cost is included in the refund.

## ReturnRepository

`ReturnRepository` persists return information in CSV format.

During loading, sale references are resolved through `SaleRepository`, while returned products are resolved through both `ProductService` and `AccessoryService`.