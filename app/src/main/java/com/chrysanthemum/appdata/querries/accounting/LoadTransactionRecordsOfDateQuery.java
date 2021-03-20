package com.chrysanthemum.appdata.querries.accounting;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.Query;
import com.chrysanthemum.appdata.querries.transactions.LoadTransactionsByTechnicianIDQuery;

import java.util.LinkedList;

/**
 * load the Records belonging to a specified list of technicians
 */
public class LoadTransactionRecordsOfDateQuery extends Query<LinkedList<Transaction>> {

    private final int entryCount;

    private final LinkedList<Transaction> data;
    private final LinkedList<Technician> techs;
    private final String date;

    private int returnedSubQuery = 0;

    public LoadTransactionRecordsOfDateQuery(String date, LinkedList<Technician> techs, DataRetriever<LinkedList<Transaction>> retriever) {
        super(retriever);

        this.techs = techs;
        this.date = date;

        data = new LinkedList<>();
        entryCount = techs.size();

        if(entryCount <= 0){
            complete(data);
        }
    }

    @Override
    public void executeQuery() {
        for(Technician tech : techs){
            LoadTransactionsByTechnicianIDQuery LoadTransactionsByTechnicianIDQuery = new LoadTransactionsByTechnicianIDQuery(date, tech, new DataRetriever<LinkedList<Transaction>>() {
                @Override
                public void retrievedData(LinkedList<Transaction> transactions) {

                    if(retrievedSubQueryData(transactions)){
                        complete(data);
                    }
                }
            });

            LoadTransactionsByTechnicianIDQuery.executeQuery();
        }
    }


    private synchronized boolean retrievedSubQueryData(LinkedList<Transaction> transaction){
        if(returnedSubQuery < entryCount){
            data.addAll(transaction);

            return ++returnedSubQuery == entryCount;
        }

        return false;
    }
}
