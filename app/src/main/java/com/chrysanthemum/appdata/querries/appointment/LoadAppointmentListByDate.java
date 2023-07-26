package com.chrysanthemum.appdata.querries.appointment;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.transaction.read.subquery.FindTransactionsByID;
import com.chrysanthemum.appdata.querries.util.AsyncSubQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class LoadAppointmentListByDate extends DBReadQuery<List<Transaction>> {

    private final String date;
    private final LinkedList<Transaction> transactionList = new LinkedList<>();
    private long count;

    public LoadAppointmentListByDate(String date) {
        this.date = date;
    }

    @Override
    protected void executeQuery() {
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

                        assert snapshot != null;
                        if(snapshot.exists()){

                            count = snapshot.getChildrenCount();
                            for(DataSnapshot child : snapshot.getChildren()){
                                long id = child.getValue(Long.class);

                                AsyncSubQuery<Transaction> subQuery = new FindTransactionsByID(id);
                                subQuery.execute(this::retrieveCustomer);
                            }
                        }
                    }
                });
    }

    private synchronized void retrieveCustomer(Transaction transaction){
        transactionList.add(transaction);

        if(transactionList.size() == count){
            returnQueryData(transactionList);
        }
    }
}
