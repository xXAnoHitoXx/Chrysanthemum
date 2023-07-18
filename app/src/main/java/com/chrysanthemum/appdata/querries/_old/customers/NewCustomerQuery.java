package com.chrysanthemum.appdata.querries._old.customers;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.querries._old.InstantQuery;

public class NewCustomerQuery extends InstantQuery<Customer> {
    private final Customer c;

    public NewCustomerQuery(String name, long phoneNumber) {
        c = new Customer(name, phoneNumber, DataStorageModule.generateID());
    }

    @Override
    public Customer executeQuery() {
        getRemoteDB().getCustomerManager().uploadCustomer(c);
        return c;
    }
}
