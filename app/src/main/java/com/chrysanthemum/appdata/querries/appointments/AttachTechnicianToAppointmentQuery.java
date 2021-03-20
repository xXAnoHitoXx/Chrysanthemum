package com.chrysanthemum.appdata.querries.appointments;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.InstantQuery;

public class AttachTechnicianToAppointmentQuery extends InstantQuery<Transaction> {

    private Transaction transaction;
    private Technician technician;

    public AttachTechnicianToAppointmentQuery(Transaction transaction, Technician technician){
        this.technician = technician;
        this.transaction = transaction;
    }

    @Override
    public Transaction executeQuery() {
        transaction.setTech(technician);
        getRemoteDB().attachTransactionTech(transaction);

        return transaction;
    }
}
