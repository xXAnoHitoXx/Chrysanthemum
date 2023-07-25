package com.chrysanthemum.appdata.querries.customer.read.subquery;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.util.SubQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;

public class FindCustomerByID implements SubQuery<Customer> {

    private final long id;

    public FindCustomerByID(long id) {
        this.id = id;
    }

    @Override
    public void execute(DataRetriever<Customer> retriever) {
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + id).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Customer customer = task.getResult().getValue(Customer.class);
                        retriever.retrievedData(customer);
                    } else {
                        retriever.retrievedData(null);
                    }
                });
    }
}
