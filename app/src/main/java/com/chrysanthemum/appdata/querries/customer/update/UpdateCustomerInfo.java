package com.chrysanthemum.appdata.querries.customer.update;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.querries._old.InstantQuery;

public class UpdateCustomerInfo extends InstantQuery<Customer> {

    private final Customer customer;
    private final String name;
    private final long phoneNumber;

    public UpdateCustomerInfo(Customer customer, String name, long phoneNumber){
        this.customer = customer;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Customer executeQuery() {
        if(!customer.getName().equals(name)){
            getRemoteDB().getCustomerManager().changeCustomerName(customer, name);
            customer.setName(name);
        }

        if(customer.getPhoneNumber() != phoneNumber){
            getRemoteDB().getCustomerManager().changeCustomerPhoneNumber(customer, phoneNumber);
            customer.setPhoneNumber(phoneNumber);
        }

        return customer;
    }
}
