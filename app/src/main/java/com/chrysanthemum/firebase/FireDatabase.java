package com.chrysanthemum.firebase;

import androidx.annotation.NonNull;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.Util.AppUtil;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.time.LocalDate;
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
        getRef().child(DatabaseStructure.TechnicianBranch.BRANCH_NAME)
                .child(DatabaseStructure.TechnicianBranch.LIST).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    Map<String, Technician> m = new TreeMap<>();

                    for(DataSnapshot child : snapshot.getChildren()){
                        Technician i = child.getValue(Technician.class);
                        m.put(i.getID()+ "", i);
                    }

                    DataStorageModule.getBackEnd().storeTechMap(m);
                }
            }
        });

    }

    public void findCustomerIDsByPhone(long phoneNumber, final DataRetriever<LinkedList<Long>> retriever){
        getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.PHONE_NUMBER_INDEX)
                .child(phoneNumber + "")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
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
            }
        });
    }

    public void findCustomerByID(long id, final DataRetriever<Customer> retriever){
        getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Customer c = task.getResult().getValue(Customer.class);
                    retriever.retrievedData(c);
                }
            }
        });
    }

    public void uploadCustomer(final Customer c){
        //indexed by phone number
        getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.PHONE_NUMBER_INDEX)
                .child("" + c.getPhoneNumber())
                .child("" + c.getID()).setValue(c.getID());

        // store the customer by id
        getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + c.getID()).setValue(c);
    }

    @Override
    public void findOpenTransactionIDsByDate(String date, final DataRetriever<LinkedList<Long>> retriever) {
        Scanner scanner = new Scanner(date);
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(year).child(month).child(day)
                .child(DatabaseStructure.TransactionBranch.OPEN_APPOINTMENT)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
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
            }
        });
    }

    @Override
    public void findTransactionByID(final long id, final DataRetriever<TransactionFrame> retriever) {
        getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    TransactionFrame c = task.getResult().getValue(TransactionFrame.class);
                    c.setID(id);
                    retriever.retrievedData(c);
                }
            }
        });
    }

    @Override
    public void uploadTransaction(Transaction transaction) {
        DatabaseReference commonRef = getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME);

        // store by id
        TransactionFrame frame = new TransactionFrame(transaction);
        commonRef.child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID()).setValue(frame);

        //indexed by date
        Scanner scanner = new Scanner(transaction.getDate());
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        commonRef.child(year).child(month).child(day)
                .child(DatabaseStructure.TransactionBranch.OPEN_APPOINTMENT)
        .child("" + transaction.getID()).setValue(transaction.getID());

        // indexed by customer
        commonRef.child(DatabaseStructure.TransactionBranch.CUSTOMER_ID_INDEX)
                .child("" + transaction.getCustomer().getID())
                .child(transaction.getID() + "").setValue(transaction.getID());
    }

    @Override
    public void markNoShow(Transaction transaction) {
        getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID())
                .child(DatabaseStructure.TransactionBranch.T_AMOUNT).setValue("" + transaction.getAmount());
    }

    public void attachTransactionTech(Transaction transaction){
        //update technicianID
        getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID())
                .child(DatabaseStructure.TransactionBranch.T_TECH_ID).setValue(transaction.getTech().getID());
    }

    public void closeTransaction(Transaction transaction){
        DatabaseReference commonRef = getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID());

        //update amount
        commonRef.child(DatabaseStructure.TransactionBranch.T_AMOUNT)
                .setValue(transaction.getAmount());
        //update tip
        commonRef.child(DatabaseStructure.TransactionBranch.T_TIP).setValue(transaction.getTip());
        //update services
        commonRef.child(DatabaseStructure.TransactionBranch.T_SERVICES).setValue(transaction.getServices());

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // accounting
        updateWeeklyAccountingData(transaction);
        updateMonthlyAccountingData(transaction);
    }

    private void updateMonthlyAccountingData(Transaction transaction){
        LocalDate date = transaction.getLocalDate();

        // yearly accounting
        DatabaseReference timeIndexRef = getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME).child(date.getYear() + "");
        updateStandardAccountingData(timeIndexRef, transaction);

        // monthly accounting
        timeIndexRef = timeIndexRef.child(date.getMonthValue() + "");
        updateStandardAccountingData(timeIndexRef, transaction);

        // daily accounting
        timeIndexRef = timeIndexRef.child(date.getDayOfMonth() + "");
        updateStandardAccountingData(timeIndexRef, transaction);

        //move from open appointment to closed
        timeIndexRef.child(DatabaseStructure.TransactionBranch.OPEN_APPOINTMENT)
                .child("" + transaction.getID()).removeValue();

        timeIndexRef.child(transaction.getTech().getID() + "")
                .child("" + transaction.getID()).setValue(transaction.getID());
    }

    private void updateWeeklyAccountingData(Transaction transaction){

        LocalDate startOfWeek = AppUtil.getMonday(transaction.getLocalDate());

        DatabaseReference weekRef = getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(startOfWeek.getYear() + "").child(startOfWeek.getMonthValue() + "").child(startOfWeek.getDayOfMonth() + "")
                .child(DatabaseStructure.Accounting.BRANCH_NAME).child(DatabaseStructure.Accounting.WEEKLY_TOTALS);

        updateAccountingData(weekRef, transaction);

    }

    private void updateStandardAccountingData(DatabaseReference timeIndexRef, Transaction transaction) {
        updateAccountingData(timeIndexRef.child(DatabaseStructure.Accounting.BRANCH_NAME), transaction);
    }

    private void updateAccountingData(DatabaseReference timeIndexRef, Transaction transaction){
        // shop total
        timeIndexRef.child(DatabaseStructure.Accounting.SHOP_TOTAL)
                .setValue(ServerValue.increment(transaction.getAmount()));

        // technician earnings
        timeIndexRef.child("" + transaction.getTech().getID())
                .setValue(ServerValue.increment(transaction.getAmount()));
    }

    static DatabaseReference getRef(){
        return FirebaseDatabase.getInstance().getReference();
    }
}
