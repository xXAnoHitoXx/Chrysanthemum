package com.chrysanthemum.appdata.querries.transaction;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.DBUpdateQuery;
import com.chrysanthemum.appdata.querries.accounting._subquery.UpdateAccountingData;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DatabaseReference;

public class UpdateTransactionData extends DBUpdateQuery<Transaction> {

    private final Transaction transaction;
    private final int amount;
    private final int tip;
    private final String service;

    public UpdateTransactionData(Transaction transaction, int amount, int tip, String service) {
        this.transaction = transaction;
        this.amount = amount;
        this.tip = tip;
        this.service = service;
    }

    public UpdateTransactionData(Transaction transaction, String service) {
        this.transaction = transaction;
        this.amount = transaction.getAmount();
        this.tip = transaction.getTip();
        this.service = service;
    }

    @Override
    public void execute() {
        DatabaseReference commonRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID());

        //update amount
        commonRef.child(DatabaseStructure.TransactionBranch.T_AMOUNT)
                .setValue(amount + "");
        //update tip
        commonRef.child(DatabaseStructure.TransactionBranch.T_TIP).setValue(tip + "");
        //update services
        commonRef.child(DatabaseStructure.TransactionBranch.T_SERVICES).setValue(service);

        //update accounting entries;
        Transaction difference = transaction.setBill(amount, tip, service);

        if(difference.isNonZero()){
            new UpdateAccountingData(difference).execute();
        }
    }
}