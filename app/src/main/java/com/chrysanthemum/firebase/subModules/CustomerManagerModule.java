package com.chrysanthemum.firebase.subModules;

import com.chrysanthemum.appdata.RemoteDataBase.CustomerManager;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.LinkedList;

public class CustomerManagerModule implements CustomerManager {

    @Override
    public void uploadCustomer(final Customer c){
        //indexed by phone number
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.PHONE_NUMBER_INDEX)
                .child("" + c.getPhoneNumber())
                .child("" + c.getID()).setValue(c.getID());

        // store the customer by id
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + c.getID()).setValue(c);
    }


    public void findCustomerByID(long id, final DataRetriever<Customer> retriever){
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + id).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Customer c = task.getResult().getValue(Customer.class);
                        retriever.retrievedData(c);
                    }
                });
    }

    @Override
    public void findCustomersByName(String name, DataRetriever<LinkedList<Customer>> retriever) {
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .orderByChild(DatabaseStructure.CustomerBranch.C_NAME)
                .startAt(name).endAt(name + " Z").get().addOnCompleteListener(task -> {

                    LinkedList<Customer> IDList = new LinkedList<>();

                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();
                        if(snapshot.exists()){
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

                        if(snapshot.exists()){
                            for(DataSnapshot child : snapshot.getChildren()){
                                long id = child.getValue(Long.class);
                                IDList.add(id);
                            }
                        }

                        retriever.retrievedData(IDList);
                    }
                });
    }


    @Override
    public void changeCustomerName(Customer customer, String name) {
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + customer.getID()).child(DatabaseStructure.CustomerBranch.C_NAME).setValue(name);
    }

    @Override
    public void changeCustomerPhoneNumber(Customer customer, long phone) {

        //customer data
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + customer.getID()).child(DatabaseStructure.CustomerBranch.C_PHONE)
                .setValue(phone);

        // index
        // common ref
        DatabaseReference ref = FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.PHONE_NUMBER_INDEX);

        // remove the index from old phone number
        // add index from new phone number
        ref.child(customer.getPhoneNumber() + "").child(customer.getID() + "").removeValue();
        ref.child(phone + "").child(customer.getID() + "").setValue(customer.getID());
    }

}
