package com.chrysanthemum.appdata.querries.transaction.read;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.transaction.read.subquery.FindTransactionsByID;
import com.chrysanthemum.appdata.querries.util.AsyncSubQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.LinkedList;
import java.util.List;

public class LoadTransactionsByCustomerID extends DBReadQuery<List<Transaction>> {

    private final long customerID;
    private final LinkedList<Transaction> list = new LinkedList<>();
    private long count;

    public LoadTransactionsByCustomerID(long customerID) {
        this.customerID = customerID;
    }

    @Override
    protected void executeQuery() {
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.CUSTOMER_ID_INDEX)
                .child(customerID + "").get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();

                        assert snapshot != null;
                        if(snapshot.exists()){

                            count = snapshot.getChildrenCount();

                            for(DataSnapshot child : snapshot.getChildren()){
                                long transactionID = child.getValue(Long.class);

                                AsyncSubQuery<Transaction> subQuery = new FindTransactionsByID(transactionID);
                                subQuery.execute(this::retrievedTransaction);
                            }
                        }
                    }
                });
    }

    private synchronized void retrievedTransaction(Transaction transaction){
        list.add(transaction);

        if(list.size() == count){
            returnQueryData(list);
        }
    }
}
