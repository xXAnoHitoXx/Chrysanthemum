package com.chrysanthemum.appdata.querries.appointments;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.Query;
import com.chrysanthemum.appdata.querries.transactions.TransactionByIDQuery;

import java.util.LinkedList;

public class LoadAppointmentListByDayQuery extends Query<LinkedList<Transaction>> {

    private final String date;
    private final LinkedList<Transaction> data;
    private int entryCount;

    public LoadAppointmentListByDayQuery(String date, DataRetriever<LinkedList<Transaction>> retriever) {
        super(retriever);
        data = new LinkedList<>();
        this.date = date;
    }

    @Override
    public void executeQuery() {
        getRemoteDB().getTransactionManager().findOpenTransactionIDsByDate(date, ids -> {
            entryCount = ids.size();

            if(entryCount > 0){
                for(long id : ids){
                    transactionByIDSubQuery(id);
                }
            } else {
                complete(data);
            }
        });
    }

    private void transactionByIDSubQuery(long id){
        TransactionByIDQuery q = new TransactionByIDQuery(id, transaction -> {

            if(retrievedSubQueryData(transaction)){
                complete(data);
            }
        });

        q.executeQuery();
    }


    private synchronized boolean retrievedSubQueryData(Transaction transaction){
        if(data.size() < entryCount){
            data.add(transaction);

            return data.size() == entryCount;
        }

        return false;
    }
}
