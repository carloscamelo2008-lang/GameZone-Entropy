package com.gamezone.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a return of one or more products from a previously
 * registered sale. A return references the original sale but does
 * not modify it; the returned products may be a subset of the
 * products included in that sale.
 */
public class Return {

    private String id;
    private LocalDate date;
    private Sale sale;
    private List<Product> returnedProducts;
    private String reason;
    private double refundAmount;
    private double warrantyRefundAmount;

    /**
     * Creates a new return record and calculates its refund amount.
     *
     * @param id               unique identifier of the return
     * @param date             date the return is registered
     * @param sale             the original sale this return references
     * @param returnedProducts the specific products being returned
     * @param reason           reason given for the return
     */
    public Return(String id, LocalDate date, Sale sale, List<Product> returnedProducts, String reason) {
        this.id = id;
        this.date = date;
        this.sale = sale;
        this.returnedProducts = returnedProducts;
        this.reason = reason;
        this.refundAmount = calculateRefundAmount();
    }

    /**
     * Returns the unique identifier of the return.
     *
     * @return the return id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the date the return was registered.
     *
     * @return the return date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the original sale this return references.
     *
     * @return the associated sale
     */
    public Sale getSale() {
        return sale;
    }

    /**
     * Returns the specific products being returned.
     *
     * @return the list of returned products
     */
    public List<Product> getReturnedProducts() {
        return returnedProducts;
    }

    /**
     * Returns the reason given for the return.
     *
     * @return the return reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns the refund amount calculated for this return.
     *
     * @return the refund amount
     */
    public double getRefundAmount() {
        return refundAmount;
    }

    /**
     * Adds a refundable amount coming from warranties cancelled together
     * with this return (for example, the extended warranty cost of a
     * returned console), and updates the total refund amount accordingly.
     *
     * @param amount the additional amount to refund
     */
    public void addWarrantyRefund(double amount) {
        this.warrantyRefundAmount += amount;
        this.refundAmount += amount;
    }

    /**
     * Returns the total amount reimbursed for cancelled warranties
     * associated with this return.
     *
     * @return the warranty refund amount
     */
    public double getWarrantyRefundAmount() {
        return warrantyRefundAmount;
    }

    /**
     * Calculates the refund amount by summing the prices of the returned
     * products, reduced by the same discount rate that was applied to the
     * original sale, and stores it in this return's attribute.
     *
     * @return the calculated refund amount
     */
    public double calculateRefundAmount() {
        double discountRate = getDiscountRate();
        double total = 0;
        for (Product product : returnedProducts) {
            total += product.getPrice() * (1 - discountRate);
        }
        this.refundAmount = total;
        return total;
    }

    /**
     * Calculates the proportion of the original sale that was discounted.
     *
     * @return the discount rate between 0 and 1, or 0 if the sale had no
     *         subtotal
     */
    private double getDiscountRate() {
        double subtotal = sale.calculateTotal();
        if (subtotal <= 0) {
            return 0;
        }
        return sale.getDiscountAmount() / subtotal;
    }

    /**
     * Builds a detailed receipt describing this return, in Spanish, for
     * display to the end user. For each product it shows the original
     * price, the proportional discount and the refunded amount.
     *
     * @return a human-readable return receipt
     */
    public String generateReturnReceipt() {
        double discountRate = getDiscountRate();
        String promotion = sale.getAppliedPromotionName() == null
                ? "Sin promoción"
                : sale.getAppliedPromotionName();

        StringBuilder receipt = new StringBuilder();
        receipt.append("===== RECIBO DE DEVOLUCIÓN =====\n");
        receipt.append("ID: ").append(id).append("\n");
        receipt.append("Fecha: ").append(date).append("\n");
        receipt.append("Venta original: ").append(sale.getDate())
                .append(" | Cliente: ").append(sale.getCustomer().getName())
                .append(" (").append(sale.getCustomer().getId()).append(")\n");
        receipt.append("Promoción de la venta: ").append(promotion).append("\n");
        receipt.append("Productos devueltos:\n");
        for (Product product : returnedProducts) {
            double price = product.getPrice();
            double discount = price * discountRate;
            receipt.append(" - ").append(product.getTitle())
                    .append(" | Precio: $").append(String.format("%.2f", price))
                    .append(" | Descuento: -$").append(String.format("%.2f", discount))
                    .append(" | Reembolso: $").append(String.format("%.2f", price - discount))
                    .append("\n");
        }
        if (warrantyRefundAmount > 0) {
            receipt.append("Reembolso por garant?as canceladas: $")
                    .append(String.format("%.2f", warrantyRefundAmount)).append("\n");
        }
        receipt.append("Motivo: ").append(reason).append("\n");
        receipt.append("Total reembolsado: $")
                .append(String.format("%.2f", refundAmount)).append("\n");
        return receipt.toString();
    }
}