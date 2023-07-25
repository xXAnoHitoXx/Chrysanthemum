package com.chrysanthemum.appdata.querries.appointment.update;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.subType.TransactionStatus;
import com.chrysanthemum.appdata.querries.DBDeleteQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.Scanner;

public class DeleteNoShowAppointment extends DBDeleteQuery<Transaction> {

    private final Transaction transaction;

    public DeleteNoShowAppointment(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void deleteData() {
        if(transaction.getTransactionStatus() == TransactionStatus.Noshow){
            DatabaseReference commonRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME);

            // store by id
            commonRef.child(DatabaseStructure.TransactionBranch.LIST)
                    .child("" + transaction.getID()).removeValue();

            //indexed by date
            Scanner scanner = new Scanner(transaction.getDate());
            String day = scanner.next();
            String month = scanner.next();
            String year = scanner.next();
            scanner.close();

            commonRef.child(year).child(month).child(day)
                    .child(DatabaseStructure.TransactionBranch.OPEN_APPOINTMENT)
                    .child("" + transaction.getID()).removeValue();

            // indexed by customer
            commonRef.child(DatabaseStructure.TransactionBranch.CUSTOMER_ID_INDEX)
                    .child("" + transaction.getCustomer().getID())
                    .child(transaction.getID() + "").removeValue();
        }
    }
}
