package com.chrysanthemum.firebase;

import androidx.annotation.NonNull;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * provide connection to firebase database
 * handle data query
 */
public class FireDatabase implements RemoteDataBase {

    public void initialization(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        observeTechnicianList();
    }

    public SecurityModule generateSecurityModule(){
        return new LoginRepository();
    }

    private void observeTechnicianList(){
        getRef().child("technician").child("technicianList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, Technician> m = new TreeMap<>();

                for(DataSnapshot child : snapshot.getChildren()){
                    Technician i = child.getValue(Technician.class);
                    m.put(i.getID()+ "", i);
                }

                DataStorageModule.getBackEnd().storeTechMap(m);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void findCustomerIDsByPhone(long phoneNumber, final DataRetriever<LinkedList<Long>> retriever){
        getRef().child("customer").child("phone").child(phoneNumber + "").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            LinkedList<Long> IDList = new LinkedList<>();
                            for(DataSnapshot child : snapshot.getChildren()){
                                long id = child.getValue(Long.class);
                                IDList.add(id);
                            }

                            retriever.retrievedData(IDList);
                        } else {
                            retriever.retrievedData(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    public void findCustomerByID(long id, final DataRetriever<Customer> retriever){
        getRef().child("customer").child("id").child("" + id).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Customer c = snapshot.getValue(Customer.class);
                        retriever.retrievedData(c);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    public void addNewCustomer(final Customer c){
        findCustomerIDsByPhone(c.getPhoneNumber(), new DataRetriever<LinkedList<Long>>() {
            @Override
            public void retrievedData(LinkedList<Long> data) {
                if(data == null){
                    data = new LinkedList<>(); //if there's no list associated with this number make a new list
                }

                //add c's id to then list, then send the list to firebase
                data.add(c.getID());
                getRef().child("customer").child("phone").child("" + c.getPhoneNumber()).setValue(data);
            }
        });

        // insert the customer at this position too
        getRef().child("customer").child("id").child("" + c.getID()).setValue(c);
    }

    static DatabaseReference getRef(){
        return FirebaseDatabase.getInstance().getReference();
    }
}
