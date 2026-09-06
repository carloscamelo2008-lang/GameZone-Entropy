package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import com.gamezone.persistence.PersonRepository;

import java.util.List;

/**
 * Contains the business rules related to customers and sellers. This is
 * the only layer allowed to call the PersonRepository; the UI layer must
 * go through this service to reach persisted person data.
 */
public class PersonService {

    private final PersonRepository personRepository;
    private List<Customer> customers;
    private List<Seller> sellers;

    /**
     * Creates the service and loads the current customers and sellers
     * from the persistence layer.
     *
     * @param personRepository the repository used to load and save person data
     */
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
        this.customers = personRepository.loadCustomers();
        this.sellers = personRepository.loadSellers();
    }

    /**
     * Registers a new customer and immediately persists the updated list.
     *
     * @param id    unique identification of the customer
     * @param name  full name of the customer
     * @param phone contact phone number
     * @param email email address of the customer
     * @return the newly created Customer
     */
    public Customer registerCustomer(String id, String name, String phone, String email) {
        Customer customer = new Customer(id, name, phone, email);
        customers.add(customer);
        personRepository.saveCustomers(customers);
        return customer;
    }

    /**
     * Returns the full list of registered customers.
     *
     * @return the list of customers
     */
    public List<Customer> listCustomers() {
        return customers;
    }

    /**
     * Returns the full list of registered sellers.
     *
     * @return the list of sellers
     */
    public List<Seller> listSellers() {
        return sellers;
    }

    /**
     * Finds a customer by their id.
     *
     * @param id the id to search for
     * @return the matching Customer, or null if not found
     */
    public Customer findCustomerById(String id) {
        for (Customer c : customers) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Finds a seller by their id.
     *
     * @param id the id to search for
     * @return the matching Seller, or null if not found
     */
    public Seller findSellerById(String id) {
        for (Seller s : sellers) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }
}
