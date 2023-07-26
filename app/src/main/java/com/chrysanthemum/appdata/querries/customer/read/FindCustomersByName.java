package com.chrysanthemum.appdata.querries.customer.read;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.Map;
import java.util.TreeMap;

public class FindCustomersByName extends DBReadQuery<Map<String, Customer>> {

    private final String name;

    public FindCustomersByName(String name) {
        this.name = name;
    }

    @Override
    protected void executeQuery() {
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .orderByChild(DatabaseStructure.CustomerBranch.C_NAME)
                .startAt(name).endAt(name + " zzz").get().addOnCompleteListener(task -> {

                    Map<String, Customer> result = new TreeMap<>();

                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();
                        if(snapshot != null){
                            for(DataSnapshot child : snapshot.getChildren()){
                                Customer c = child.getValue(Customer.class);
                                result.put(c.getName(), c);
                            }
                        }
                    }

                    returnQueryData(result);
                });
    }

}
