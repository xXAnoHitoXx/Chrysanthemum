package com.chrysanthemum.appdata.querries.transactions;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.querries.InstantQuery;
import com.chrysanthemum.firebase.DatabaseStructure;

import java.time.LocalDate;

public class NewSaleTransactionQuery extends InstantQuery<Transaction> {

    private final Transaction transaction;
    public NewSaleTransactionQuery(LocalDate date, Customer c, String services, int pretax){
        String dateValue = TimeParser.parseDateData(date);

        int time = TimeParser.currentTime();

        transaction = new Transaction(dateValue, time, 0, c, DataStorageModule.generateID(),
                DatabaseStructure.Accounting.SALE_TECH, pretax, 0, services);
    }

    @Override
    public Transaction executeQuery() {
        getRemoteDB().getTransactionManager().uploadSaleRecord(transaction);
        return transaction;
    }
}
