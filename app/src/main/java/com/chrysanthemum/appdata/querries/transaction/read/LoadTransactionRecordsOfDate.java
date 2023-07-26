package com.chrysanthemum.appdata.querries.transaction.read;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.transaction.read.subquery.LoadTransactionOfTechnicianOnDateAsync;
import com.chrysanthemum.appdata.querries.util.AsyncSubQuery;

import java.util.LinkedList;
import java.util.List;

public class LoadTransactionRecordsOfDate extends DBReadQuery<List<Transaction>> {

    private final String date;
    private final List<Technician> techs;
    private final List<Transaction> data = new LinkedList<>();
    private int count;

    public LoadTransactionRecordsOfDate(String date, List<Technician> techs) {
        this.date = date;
        this.techs = techs;
        count = 0;
    }

    @Override
    protected void executeQuery() {
        for(Technician tech : techs){
            AsyncSubQuery<List<Transaction>> subQuery = new LoadTransactionOfTechnicianOnDateAsync(tech.getID(), date);
            subQuery.execute(this::retrievedData);
        }
    }

    private synchronized void retrievedData(List<Transaction> data){
        this.data.addAll(data);
        count++;

        if(count == techs.size()){
            returnQueryData(this.data);
        }
    }
}
