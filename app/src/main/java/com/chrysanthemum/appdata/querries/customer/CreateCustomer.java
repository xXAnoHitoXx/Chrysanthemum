package com.chrysanthemum.appdata.querries.customer;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.querries.DBCreateQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;

public class CreateCustomer extends DBCreateQuery<Customer> {
    private final Customer c;

    public CreateCustomer(String name, long phoneNumber) {
        c = new Customer(name, phoneNumber, DataStorageModule.generateID());
    }

    @Override
    public Customer execute() {

        //indexed by phone number
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.PHONE_NUMBER_INDEX)
                .child("" + c.getPhoneNumber())
                .child("" + c.getID()).setValue(c.getID());

        // store the customer by id
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + c.getID()).setValue(c);

        return c;
    }
}
