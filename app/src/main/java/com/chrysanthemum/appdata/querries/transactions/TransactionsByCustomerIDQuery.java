package com.chrysanthemum.appdata.querries.transactions;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.RemoteDataBase;
import com.chrysanthemum.appdata.querries.Query;

import java.util.LinkedList;

/**
 * query transactions and sort the result base on appointment time
 */
public class TransactionsByCustomerIDQuery extends Query<LinkedList<Transaction>> {

    private final Customer customer;
    private int entryCount;

    private final LinkedList<Transaction> data;

    public TransactionsByCustomerIDQuery(Customer customer, DataRetriever<LinkedList<Transaction>> retriever) {
        super(retriever);

        data = new LinkedList<>();

        this.customer = customer;
    }

    @Override
    public void executeQuery() {
        getRemoteDB().findTransactionIDsByCustomerID(customer.getID(), new DataRetriever<LinkedList<Long>>() {
            @Override
            public void retrievedData(LinkedList<Long> ids) {
                entryCount = ids.size();

                if(entryCount > 0){
                    for(long id : ids){
                        executeSubQuery(id);
                    }
                } else {
                    complete(data);
                }
            }
        });
    }

    private void executeSubQuery(long transactionID) {
        TransactionByIDQuery subQuery = new TransactionByIDQuery(transactionID, new DataRetriever<Transaction>() {
            @Override
            public void retrievedData(Transaction transaction) {

                if(retrievedSubQueryData(transaction)){
                    complete(data);
                }
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
