package com.gamezone.model;

/**
 * Abstract base class representing a person that interacts with the store.
 * A generic Person cannot exist on its own in this domain: every person
 * must be either a Customer or a Seller, so this class is declared abstract.
 */
public abstract class Person {

    private String id;
    private String name;
    private String phone;

    /**
     * Creates a new Person with the common attributes shared by all roles.
     *
     * @param id    unique identification of the person
     * @param name  full name of the person
     * @param phone contact phone number
     */
    public Person(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns role-specific information about this person. Each subclass
     * must implement this according to its own attributes, since the
     * behaviour depends entirely on the specialization (Customer or Seller).
     *
     * @return a description of the person's role-specific data
     */
    public abstract String getRoleDescription();
}
