# Return Class Diagram

```mermaid
classDiagram

    class Return {
        -String id
        -LocalDate date
        -Sale sale
        -List~Product~ returnedProducts
        -String reason
        -double refundAmount
        -double warrantyRefundAmount
        +getId() String
        +getDate() LocalDate
        +getSale() Sale
        +getReturnedProducts() List~Product~
        +getReason() String
        +getRefundAmount() double
        +addWarrantyRefund(double amount) void
        +getWarrantyRefundAmount() double
        +calculateRefundAmount() double
        +generateReturnReceipt() String
    }

    class ReturnService {
        -ReturnRepository returnRepository
        -SaleService saleService
        -ProductService productService
        -AccessoryService accessoryService
        -WarrantyService warrantyService
        -List~Return~ returns
        +registerReturn(String saleId, List~String~ productIds, String reason) Return
        +viewAllReturns() List~Return~
        +viewReturnsByCustomer(String customerId) List~Return~
        +viewReturnsBySale(String saleId) List~Return~
        +calculateMonthlySales(int month, int year) double
        +calculateMonthlyReturns(int month, int year) double
        +generateMonthlyBalance(int month, int year) double
    }

    class ReturnRepository {
        -String filePath
        -SaleRepository saleRepository
        -ProductService productService
        -AccessoryService accessoryService
        +saveAll(List~Return~ returns) void
        +loadAll() List~Return~
    }

    class Sale
    class Product
    class ProductService
    class AccessoryService
    class WarrantyService

    Return --> Sale : references
    Return --> Product : returns

    ReturnService --> ReturnRepository : persists
    ReturnService --> SaleService : validates sales
    ReturnService --> ProductService : restores products
    ReturnService --> AccessoryService : restores accessories
    ReturnService --> WarrantyService : cancels warranties
    ReturnService --> Return : manages

    ReturnRepository --> SaleRepository : resolves sales
    ReturnRepository --> ProductService : resolves products
    ReturnRepository --> AccessoryService : resolves accessories