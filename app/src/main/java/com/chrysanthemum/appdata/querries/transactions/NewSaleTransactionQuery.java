package com.chrysanthemum.appdata.querries.transactions;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.querries.InstantQuery;
import com.chrysanthemum.firebase.DatabaseStructure;

import java.time.LocalDate;

public class NewSaleTransactionQuery extends InstantQuery<Transaction> {

    Transaction transaction;
    public NewSaleTransactionQuery(Customer c, String services, int postTax){
        LocalDate today = LocalDate.now();

        String date = today.getDayOfMonth() + " " + today.getMonthValue() + " " + today.getYear();

        int time = TimeParser.currentTime();

        transaction = new Transaction(date, time, 0, c, DataStorageModule.generateID(),
                DatabaseStructure.Accounting.SALE_TECH, 0, postTax, services);
    }

    @Override
    public Transaction executeQuery() {
        getRemoteDB().uploadSaleRecord(transaction);
        return transaction;
    }
}
