package com.chrysanthemum.appdata.querries._old.appointments;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.subType.TransactionStatus;
import com.chrysanthemum.appdata.querries._old.InstantQuery;

public class DeleteNoshowAppointmentQuery  extends InstantQuery<Transaction>{

    private final Transaction transaction;

    public DeleteNoshowAppointmentQuery(Transaction transaction){
        this.transaction = transaction;
    }

    @Override
    public Transaction executeQuery() {
        if(transaction.getTransactionStatus() == TransactionStatus.Noshow){
            getRemoteDB().getTransactionManager().removeAppointment(transaction);
        }
        return null;
    }
}
