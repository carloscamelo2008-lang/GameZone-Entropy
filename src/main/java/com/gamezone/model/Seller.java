package com.gamezone.model;

/**
 * Represents a seller (employee) of the store. Sellers attend customers
 * and register sales. They are preloaded into the system since they are
 * already hired, rather than registered through the user interface.
 */
public class Seller extends Person {

    private String employeeCode;
    private String shift;

    /**
     * Creates a new Seller.
     *
     * @param id           unique identification of the seller
     * @param name         full name of the seller
     * @param phone        contact phone number
     * @param employeeCode employee code assigned to the seller
     * @param shift        work shift assigned to the seller
     */
    public Seller(String id, String name, String phone, String employeeCode, String shift) {
        super(id, name, phone);
        this.employeeCode = employeeCode;
        this.shift = shift;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    /**
     * Returns a description of this seller's role-specific information.
     *
     * @return a string describing the seller's employee code and shift
     */
    @Override
    public String getRoleDescription() {
        return "Seller - Employee code: " + employeeCode + ", Shift: " + shift;
    }
}
