package com.chrysanthemum.appdata.querries.transaction.read;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.transaction.read.subquery.LoadTransactionOfTechnicianOnDateAsync;
import com.chrysanthemum.appdata.querries.util.AsyncSubQuery;

import java.util.List;

public class LoadTransactionOfTechnicianOnDate extends DBReadQuery<List<Transaction>> {

    private final AsyncSubQuery<List<Transaction>> subQuery;

    public LoadTransactionOfTechnicianOnDate(long technicianId, String date) {
        subQuery = new LoadTransactionOfTechnicianOnDateAsync(technicianId, date);
    }

    @Override
    protected void executeQuery() {
        subQuery.execute(this::returnQueryData);
    }
}
