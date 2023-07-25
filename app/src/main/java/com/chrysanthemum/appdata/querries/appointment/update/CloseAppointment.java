package com.chrysanthemum.appdata.querries.appointment.update;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.transaction.UpdateTransactionData;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DatabaseReference;

import java.time.LocalDate;

public class CloseAppointment extends UpdateTransactionData {

    private final LocalDate date;
    private final long transactionID;
    private final long technicianID;

    public CloseAppointment(Transaction transaction, int amount, int tip, String service) {
        super(transaction, amount, tip, service);
        date = transaction.getLocalDateAppointmentDate();
        transactionID = transaction.getID();
        technicianID = transaction.getTech().getID();
    }

    @Override
    public void execute() {
        super.execute();

        // indexing
        DatabaseReference timeIndexRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(date.getYear() + "")
                .child(date.getMonthValue() + "")
                .child(date.getDayOfMonth() + "");

        //move from open appointment to closed
        timeIndexRef.child(DatabaseStructure.TransactionBranch.OPEN_APPOINTMENT)
                .child("" + transactionID).removeValue();

        timeIndexRef.child(technicianID + "")
                .child("" + transactionID).setValue(transactionID);
    }
}
