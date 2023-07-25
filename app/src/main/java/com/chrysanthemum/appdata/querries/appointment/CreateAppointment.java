package com.chrysanthemum.appdata.querries.appointment;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;
import com.chrysanthemum.appdata.querries.DBCreateQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.Scanner;

public class CreateAppointment extends DBCreateQuery<Transaction> {

    private final Transaction transaction;

    public CreateAppointment(String date, int time, int duration, Customer c, String services) {
        transaction = new Transaction(date, time, duration, c, DataStorageModule.generateID(), services);
    }

    @Override
    public Transaction execute() {
        DatabaseReference commonRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME);

        // store by id
        TransactionFrame frame = new TransactionFrame(transaction);
        commonRef.child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID()).setValue(frame);

        //indexed by date
        Scanner scanner = new Scanner(transaction.getDate());
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        commonRef.child(year).child(month).child(day)
                .child(DatabaseStructure.TransactionBranch.OPEN_APPOINTMENT)
                .child("" + transaction.getID()).setValue(transaction.getID());

        // indexed by customer
        commonRef.child(DatabaseStructure.TransactionBranch.CUSTOMER_ID_INDEX)
                .child("" + transaction.getCustomer().getID())
                .child(transaction.getID() + "").setValue(transaction.getID());

        return transaction;
    }
}
