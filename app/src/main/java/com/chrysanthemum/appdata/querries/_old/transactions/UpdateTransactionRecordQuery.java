package com.chrysanthemum.appdata.querries._old.transactions;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries._old.InstantQuery;

public class UpdateTransactionRecordQuery extends InstantQuery<Transaction> {

    private final Transaction transaction;
    private final int amount;
    private final int tip;
    private final String service;

    public UpdateTransactionRecordQuery(Transaction transaction, int amount, int tip, String service) {
        this.transaction = transaction;
        this.amount = amount;
        this.tip = tip;
        this.service = service;
    }

    @Override
    public Transaction executeQuery() {
        getRemoteDB().getTransactionManager().editRecord(transaction, amount, tip, service);
        return transaction;
    }
}
