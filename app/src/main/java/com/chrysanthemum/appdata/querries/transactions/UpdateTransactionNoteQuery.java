package com.chrysanthemum.appdata.querries.transactions;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.InstantQuery;

public class UpdateTransactionNoteQuery extends InstantQuery<Transaction> {

    private final Transaction transaction;
    private final String service;

    public UpdateTransactionNoteQuery(Transaction transaction, String service) {
        this.transaction = transaction;
        this.service = service;
    }

    @Override
    public Transaction executeQuery() {
        getRemoteDB().getTransactionManager().editRecord(transaction,transaction.getAmount(), transaction.getTip(), service);
        return transaction;
    }
}