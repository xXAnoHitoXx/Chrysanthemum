package com.chrysanthemum.appdata.querries.appointment.update;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.DBUpdateQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;

public class AssignTechnicianToAppointment extends DBUpdateQuery<Transaction> {

    private final Transaction transaction;
    private final Technician technician;

    public AssignTechnicianToAppointment(Transaction transaction, Technician technician) {
        this.transaction = transaction;
        this.technician = technician;
    }

    @Override
    public void execute() {
        long techID = (transaction.getTech() == null)? Transaction.NO_TECH_ID : transaction.getTech().getID();

        //update technicianID
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID())
                .child(DatabaseStructure.TransactionBranch.T_TECH_ID).setValue(techID);
    }
}
