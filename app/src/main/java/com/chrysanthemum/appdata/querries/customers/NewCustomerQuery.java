package com.chrysanthemum.appdata.querries.customers;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.querries.InstantQuery;

public class NewCustomerQuery extends InstantQuery<Customer> {
    private Customer c;

    public NewCustomerQuery(String name, long phoneNumber) {
        c = new Customer(name, phoneNumber, DataStorageModule.generateID());
    }

    @Override
    public Customer executeQuery() {
        getRemoteDB().uploadCustomer(c);
        return c;
    }
}
