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
     * Calculates the refund amount by summing the prices of the
     * returned products, and stores it in this return's attribute.
     *
     * @return the calculated refund amount
     */
    public double calculateRefundAmount() {
        double total = 0;
        for (Product product : returnedProducts) {
            total += product.getPrice();
        }
        this.refundAmount = total;
        return total;
    }

    /**
     * Builds a formatted receipt describing this return, in Spanish,
     * for display to the end user. The receipt includes a reference to
     * the original sale (its date and customer).
     *
     * @return a human-readable return receipt
     */
    public String generateReturnReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("Recibo de Devolución\n");
        receipt.append("ID: ").append(id).append("\n");
        receipt.append("Fecha: ").append(date).append("\n");
        receipt.append("Venta original: ").append(sale.getDate())
                .append(" | Cliente: ").append(sale.getCustomer().getName())
                .append(" (").append(sale.getCustomer().getId()).append(")\n");
        receipt.append("Productos devueltos:\n");
        for (Product product : returnedProducts) {
            receipt.append(" - ").append(product.getTitle())
                    .append(": $").append(product.getPrice()).append("\n");
        }
        receipt.append("Motivo: ").append(reason).append("\n");
        receipt.append("Monto reembolsado: $").append(refundAmount).append("\n");
        return receipt.toString();
    }
}