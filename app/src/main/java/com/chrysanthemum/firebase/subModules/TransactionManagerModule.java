package com.chrysanthemum.firebase.subModules;

import com.chrysanthemum.appdata.RemoteDataBase.AccountingManager;
import com.chrysanthemum.appdata.RemoteDataBase.TransactionManager;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;
import com.chrysanthemum.appdata.dataType.subType.TransactionStatus;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Scanner;

public class TransactionManagerModule implements TransactionManager {

    private final AccountingManager am;

    public TransactionManagerModule(AccountingManager am){
        this.am = am;
    }

    @Override
    public void uploadTransaction(Transaction transaction) {
        DatabaseReference commonRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME);

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
    public void uploadSaleRecord(Transaction transaction) {
        DatabaseReference commonRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME);

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
                .child(transaction.getTech().getID() + "")
                .child("" + transaction.getID()).setValue(transaction.getID());

        // indexed by customer
        commonRef.child(DatabaseStructure.TransactionBranch.CUSTOMER_ID_INDEX)
                .child("" + transaction.getCustomer().getID())
                .child(transaction.getID() + "").setValue(transaction.getID());

        // accounting
        am.updateAccountingData(transaction);
    }

    @Override
    public void findTransactionByID(final long id, final DataRetriever<TransactionFrame> retriever) {
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + id).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        DataSnapshot snap = task.getResult();

                        if(snap != null){
                            TransactionFrame c = snap.getValue(TransactionFrame.class);
                            assert c != null;
                            c.setID(id);
                            retriever.retrievedData(c);
                        }
                    }
                });
    }

    @Override
    public void findOpenTransactionIDsByDate(String date, final DataRetriever<LinkedList<Long>> retriever) {
        Scanner scanner = new Scanner(date);
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(year).child(month).child(day)
                .child(DatabaseStructure.TransactionBranch.OPEN_APPOINTMENT)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();
                        LinkedList<Long> IDList = new LinkedList<>();

                        assert snapshot != null;
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
    public void findTransactionIDsByCustomerID(long customerID, final DataRetriever<LinkedList<Long>> retriever) {
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.CUSTOMER_ID_INDEX)
                .child(customerID + "").get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();
                        LinkedList<Long> IDList = new LinkedList<>();

                        assert snapshot != null;
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
    public void findTransactionIDsByDateAndTechnicianID(String date, long technicianId, final DataRetriever<LinkedList<Long>> retriever) {
        Scanner scanner = new Scanner(date);
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        final LinkedList<Long> ids = new LinkedList<>();

        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(year).child(month).child(day)
                .child("" + technicianId).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();

                        for(DataSnapshot child : snapshot.getChildren()){
                            ids.add(child.getValue(Long.class));
                        }
                    }

                    retriever.retrievedData(ids);
                });
    }

    @Override
    public void markNoShow(Transaction transaction) {
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID())
                .child(DatabaseStructure.TransactionBranch.T_AMOUNT).setValue("" + transaction.getAmount());
    }

    public void attachTransactionTech(Transaction transaction){

        long techID = (transaction.getTech() == null)? -1 : transaction.getTech().getID();

        if(transaction.getTech() != null){
            techID = transaction.getTech().getID();
        }

        //update technicianID
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID())
                .child(DatabaseStructure.TransactionBranch.T_TECH_ID).setValue(techID);
    }

    public void closeTransaction(Transaction transaction){
        DatabaseReference commonRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID());

        //update amount
        commonRef.child(DatabaseStructure.TransactionBranch.T_AMOUNT)
                .setValue(transaction.getAmount() + "");
        //update tip
        commonRef.child(DatabaseStructure.TransactionBranch.T_TIP).setValue(transaction.getTip() + "");
        //update services
        commonRef.child(DatabaseStructure.TransactionBranch.T_SERVICES).setValue(transaction.getServices());

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // accounting
        am.updateAccountingData(transaction);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // close transaction
        LocalDate date = transaction.getLocalDateAppointmentDate();

        // yearly accounting
        DatabaseReference timeIndexRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(date.getYear() + "")
                .child(date.getMonthValue() + "")
                .child(date.getDayOfMonth() + "");

        //move from open appointment to closed
        timeIndexRef.child(DatabaseStructure.TransactionBranch.OPEN_APPOINTMENT)
                .child("" + transaction.getID()).removeValue();

        timeIndexRef.child(transaction.getTech().getID() + "")
                .child("" + transaction.getID()).setValue(transaction.getID());
    }

    @Override
    public void editRecord(Transaction transaction, int amount, int tip, String service) {
        DatabaseReference commonRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID());

        //update amount
        commonRef.child(DatabaseStructure.TransactionBranch.T_AMOUNT)
                .setValue(amount + "");
        //update tip
        commonRef.child(DatabaseStructure.TransactionBranch.T_TIP).setValue(tip + "");
        //update services
        commonRef.child(DatabaseStructure.TransactionBranch.T_SERVICES).setValue(service);

        // accounting
        am.updateAccountingData(transaction.setBill(amount, tip, service));
    }

    @Override
    public void removeAppointment(Transaction transaction) {
        if(transaction.getTransactionStatus() == TransactionStatus.Noshow){
            DatabaseReference commonRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME);

            // store by id
            commonRef.child(DatabaseStructure.TransactionBranch.LIST)
                    .child("" + transaction.getID()).removeValue();

            //indexed by date
            Scanner scanner = new Scanner(transaction.getDate());
            String day = scanner.next();
            String month = scanner.next();
            String year = scanner.next();
            scanner.close();

            commonRef.child(year).child(month).child(day)
                    .child(DatabaseStructure.TransactionBranch.OPEN_APPOINTMENT)
                    .child("" + transaction.getID()).removeValue();

            // indexed by customer
            commonRef.child(DatabaseStructure.TransactionBranch.CUSTOMER_ID_INDEX)
                    .child("" + transaction.getCustomer().getID())
                    .child(transaction.getID() + "").removeValue();
        }
    }


}
