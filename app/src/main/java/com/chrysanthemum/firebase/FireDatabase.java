package com.chrysanthemum.firebase;

import androidx.annotation.NonNull;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
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
                        LinkedList<Long> IDList = new LinkedList<>();

                        if(snapshot.exists()){
                            for(DataSnapshot child : snapshot.getChildren()){
                                long id = child.getValue(Long.class);
                                IDList.add(id);
                            }
                        }

                        retriever.retrievedData(IDList);
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

    public void uploadCustomer(final Customer c){
        //indexed by phone number
        getRef().child("customer").child("phone").child("" + c.getPhoneNumber()).child("" + c.getID()).setValue(c.getID());

        // store the customer by id
        getRef().child("customer").child("id").child("" + c.getID()).setValue(c);
    }

    @Override
    public void uploadTransaction(Transaction transaction) {
        DatabaseReference commonRef = getRef().child("transaction");

        // store by id
        commonRef.child("id").child("" + transaction.getID())
                .setValue(new TransactionFrame(transaction));

        //indexed by date
        Scanner scanner = new Scanner(transaction.getDate());
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        commonRef.child(year).child(month).child(day).child("Open")
        .child("" + transaction.getID()).setValue(transaction.getID());

        // indexed by customer
        commonRef.child("customer")
                .child("" + transaction.getCustomer().getID())
                .child(transaction.getID() + "").setValue(transaction.getID());
    }

    public void attachTransactionTech(Transaction transaction){
        //update technicianID
        getRef().child("transaction").child("id").child("" + transaction.getID())
                .child("technicianID").setValue(transaction.getTech().getID());
    }

    public void closeTransaction(Transaction transaction){
        DatabaseReference commonRef = getRef().child("transaction").child("id")
                .child("" + transaction.getID());

        //update amount
        commonRef.child("amount").setValue(transaction.getAmount());
        //update tip
        commonRef.child("tip").setValue(transaction.getTip());
        //update services
        commonRef.child("services").setValue(transaction.getServices());

        //change grouping

        //indexed by date
        Scanner scanner = new Scanner(transaction.getDate());
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        DatabaseReference indexRef = getRef().child("transaction")
                .child(year).child(month).child(day);

        //move data from one place to another
        indexRef.child("Open").child("" + transaction.getID()).removeValue();
        indexRef.child(transaction.getTech().getID() + "")
                .child("" + transaction.getID()).setValue(transaction.getID());
    }

    static DatabaseReference getRef(){
        return FirebaseDatabase.getInstance().getReference();
    }
}
