package com.chrysanthemum.appdata.queryHandlers;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.firebase.RemoteDataBase;

import java.util.LinkedList;

public class LoadAppointmentListByDayQuery extends Query<LinkedList<Transaction>> {

    private final String date;
    private final LinkedList<Transaction> data;
    private int entryCount;

    public LoadAppointmentListByDayQuery(RemoteDataBase remote, String date,
                                         DataRetriever<LinkedList<Transaction>> retriever) {
        super(remote, retriever);
        data = new LinkedList<>();
        this.date = date;
    }

    @Override
    public void executeQuery() {
        getRemoteDB().findOpenTransactionIDsByDate(date, new DataRetriever<LinkedList<Long>>() {
            @Override
            public void retrievedData(LinkedList<Long> ids) {
                entryCount = ids.size();

                if(entryCount > 0){
                    for(long id : ids){
                        transactionByIDSubQuery(id);
                    }
                } else {
                    complete(data);
                }
            }
        });
    }

    private void transactionByIDSubQuery(long id){
        TransactionByIDQuery q = new TransactionByIDQuery(getRemoteDB(), id, new DataRetriever<Transaction>() {
            @Override
            public void retrievedData(Transaction transaction) {
                retrievedSubQueryData(transaction);

                if(data.size() == entryCount){
                    complete(data);
                }
            }
        });

        q.executeQuery();
    }


    private synchronized void retrievedSubQueryData(Transaction transaction){
        if(data.size() < entryCount){
            data.add(transaction);
        }
    }
}
