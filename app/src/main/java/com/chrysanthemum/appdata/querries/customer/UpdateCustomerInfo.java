package com.chrysanthemum.appdata.querries.customer;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.querries.DBUpdateQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DatabaseReference;

public class UpdateCustomerInfo extends DBUpdateQuery<Customer> {

    private final Customer customer;
    private final String name;
    private final long phoneNumber;

    public UpdateCustomerInfo(Customer customer, String name, long phoneNumber){
        this.customer = customer;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void execute() {
        if(!customer.getName().equals(name)){
            changeCustomerName();
        }

        if(customer.getPhoneNumber() != phoneNumber){
            changeCustomerPhoneNumber();
        }
    }

    private void changeCustomerName(){
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + customer.getID()).child(DatabaseStructure.CustomerBranch.C_NAME).setValue(name);

        customer.setName(name);
    }

    private void changeCustomerPhoneNumber() {

        //customer data
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + customer.getID()).child(DatabaseStructure.CustomerBranch.C_PHONE)
                .setValue(phoneNumber);

        // index
        // common ref
        DatabaseReference ref = FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.PHONE_NUMBER_INDEX);

        // remove the index from old phone number
        // add index from new phone number
        ref.child(customer.getPhoneNumber() + "").child(customer.getID() + "").removeValue();
        ref.child(phoneNumber + "").child(customer.getID() + "").setValue(customer.getID());

        customer.setPhoneNumber(phoneNumber);
    }
}
