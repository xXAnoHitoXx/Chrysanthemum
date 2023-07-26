package com.chrysanthemum.firebase.subModules;

import com.chrysanthemum.appdata.RemoteDataBase.AccountingManager;
import com.chrysanthemum.appdata.RemoteDataBase.TransactionManager;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.LinkedList;
import java.util.Scanner;

public class TransactionManagerModule implements TransactionManager {

    private final AccountingManager am;

    public TransactionManagerModule(AccountingManager am){
        this.am = am;
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


}
