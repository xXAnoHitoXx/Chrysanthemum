package com.chrysanthemum.appdata.querries.customer.read;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.customer.read.subquery.FindCustomerByID;
import com.chrysanthemum.appdata.querries.util.AsyncSubQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.Map;
import java.util.TreeMap;

public class FindCustomersByPhone extends DBReadQuery<Map<String, Customer>> {

    private final long phoneNumber;
    private final Map<String, Customer> result = new TreeMap<>();
    private long count;

    public FindCustomersByPhone(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    protected void executeQuery() {
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.PHONE_NUMBER_INDEX)
                .child(phoneNumber + "")
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();

                        if(snapshot != null){

                            count = snapshot.getChildrenCount();
                            for(DataSnapshot child : snapshot.getChildren()){
                                long id = child.getValue(Long.class);

                                AsyncSubQuery<Customer> subQuery = new FindCustomerByID(id);
                                subQuery.execute(this::found);

                            }
                        }

                    }
                });
    }

    private synchronized void found(Customer customer){
        result.put(customer.getName(), customer);

        if(result.size() == count){
            returnQueryData(result);
        }
    }
}
