package com.chrysanthemum.appdata.querries.customers;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.querries.InstantQuery;

public class UpdateCustomerInfoQuery extends InstantQuery<Customer> {

    private Customer customer;
    private String name;
    private long phoneNumber;

    public UpdateCustomerInfoQuery(Customer customer, String name, long phoneNumber){
        this.customer = customer;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Customer executeQuery() {
        if(!customer.getName().equals(name)){
            getRemoteDB().changeCustomerName(customer, name);
            customer.setName(name);
        }

        if(customer.getPhoneNumber() != phoneNumber){
            getRemoteDB().changeCustomerPhoneNumber(customer, phoneNumber);
            customer.setPhoneNumber(phoneNumber);
        }

        return customer;
    }
}
