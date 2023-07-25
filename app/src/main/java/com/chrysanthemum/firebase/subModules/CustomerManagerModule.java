package com.chrysanthemum.firebase.subModules;

import com.chrysanthemum.appdata.RemoteDataBase.CustomerManager;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.LinkedList;

public class CustomerManagerModule implements CustomerManager {


    public void findCustomerByID(long id, final DataRetriever<Customer> retriever){
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + id).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Customer c = task.getResult().getValue(Customer.class);
                        retriever.retrievedData(c);
                    } else {
                        retriever.retrievedData(null);
                    }
                });
    }

    @Override
    public void findCustomersByName(String name, DataRetriever<LinkedList<Customer>> retriever) {
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .orderByChild(DatabaseStructure.CustomerBranch.C_NAME)
                .startAt(name).endAt(name + " zzz").get().addOnCompleteListener(task -> {

                    LinkedList<Customer> IDList = new LinkedList<>();

                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();
                        if(snapshot != null){
                            for(DataSnapshot child : snapshot.getChildren()){
                                Customer c = child.getValue(Customer.class);
                                IDList.add(c);
                            }
                        }
                    }

                    retriever.retrievedData(IDList);
                });
    }

    public void findCustomerIDsByPhone(long phoneNumber, final DataRetriever<LinkedList<Long>> retriever){
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.PHONE_NUMBER_INDEX)
                .child(phoneNumber + "")
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();
                        LinkedList<Long> IDList = new LinkedList<>();

                        if(snapshot != null){
                            for(DataSnapshot child : snapshot.getChildren()){
                                long id = child.getValue(Long.class);
                                IDList.add(id);
                            }
                        }

                        retriever.retrievedData(IDList);
                    }
                });
    }


}
