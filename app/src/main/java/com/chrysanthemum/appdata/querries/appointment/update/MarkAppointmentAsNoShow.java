package com.chrysanthemum.appdata.querries.appointment.update;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.DBUpdateQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;

public class MarkAppointmentAsNoShow extends DBUpdateQuery<Transaction> {

    private final Transaction transaction;

    public MarkAppointmentAsNoShow(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void execute() {
        transaction.markNoShow();
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID())
                .child(DatabaseStructure.TransactionBranch.T_AMOUNT).setValue("" + transaction.getAmount());
    }
}
