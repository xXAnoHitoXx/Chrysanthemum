package com.chrysanthemum.appdata.querries._old.transactions;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries._old.Query;

import java.util.LinkedList;

public class LoadTransactionsByTechnicianIDQuery  extends Query<LinkedList<Transaction>> {

    private final Technician technician;
    private final String date;
    private int entryCount;

    private final LinkedList<Transaction> data;

    public LoadTransactionsByTechnicianIDQuery(String date, Technician technician, DataRetriever<LinkedList<Transaction>> retriever) {
        super(retriever);

        data = new LinkedList<>();

        this.technician = technician;
        this.date = date;
    }

    @Override
    public void executeQuery() {
        getRemoteDB().getTransactionManager().findTransactionIDsByDateAndTechnicianID(date, technician.getID(), ids -> {
            entryCount = ids.size();

            if(entryCount > 0){
                for(long id : ids){
                    executeSubQuery(id);
                }
            } else {
                complete(data);
            }
        });
    }

    private void executeSubQuery(long transactionID) {
        TransactionByIDQuery subQuery = new TransactionByIDQuery(transactionID, transaction -> {

            if(retrievedSubQueryData(transaction)){
                complete(data);
            }
        });

        subQuery.executeQuery();
    }


    private synchronized boolean retrievedSubQueryData(Transaction transaction){
        if(data.size() < entryCount){
            data.add(transaction);

            return data.size() == entryCount;
        }

        return false;
    }
}
