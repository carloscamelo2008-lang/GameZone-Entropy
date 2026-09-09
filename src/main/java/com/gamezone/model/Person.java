package com.gamezone.model;
import java.io.Serializable;

/**
 * Abstract base class representing a person that interacts with the store.
 * A generic Person cannot exist on its own in this domain: every person
 * must be either a Customer or a Seller, so this class is declared abstract.
 */
public abstract class Person implements Serializable {

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

    /** * Returns the unique identification of this person. * * @return the person's id */
    public String getId() {
        return id;
    }

    /** * Updates the unique identification of this person. * * @param id the new id */
    public void setId(String id) {
        this.id = id;
    }

    /**
 * Returns the full name of this person.
 *
 * @return the person's name
 */
    public String getName() {
        return name;
    }

    /**
 * Updates the full name of this person.
 *
 * @param name the new name
 */
    public void setName(String name) {
        this.name = name;
    }

    /**
 * Returns the contact phone number of this person.
 *
 * @return the person's phone number
 */
    public String getPhone() {
        return phone;
    }

    /**
 * Updates the contact phone number of this person.
 *
 * @param phone the new phone number
 */
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
