package com.chrysanthemum.appdata.querries.transaction.read.subquery;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.util.AsyncSubQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class LoadTransactionOfTechnicianOnDateAsync implements AsyncSubQuery<List<Transaction>> {

    private final long technicianId;
    private final String date;
    private final List<Transaction> transactionList = new LinkedList<>();
    private long count;

    private DataRetriever<List<Transaction>> retriever;


    public LoadTransactionOfTechnicianOnDateAsync(long technicianId, String date) {
        this.technicianId = technicianId;
        this.date = date;
    }

    @Override
    public void execute(DataRetriever<List<Transaction>> retriever) {
        this.retriever = retriever;

        Scanner scanner = new Scanner(date);
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(year).child(month).child(day)
                .child("" + technicianId).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();

                        count = snapshot.getChildrenCount();

                        for(DataSnapshot child : snapshot.getChildren()) {
                            long transactionID = child.getValue(Long.class);

                            AsyncSubQuery<Transaction> subQuery = new FindTransactionsByID(transactionID);
                            subQuery.execute(this::retrieveTransaction);
                        }
                    }
                });
    }

    private synchronized void retrieveTransaction(Transaction transaction) {
        transactionList.add(transaction);

        if(transactionList.size() == count){
            retriever.retrievedData(transactionList);
        }
    }
}
