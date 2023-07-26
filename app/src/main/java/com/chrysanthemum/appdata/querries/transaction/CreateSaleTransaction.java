package com.chrysanthemum.appdata.querries.transaction;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;
import com.chrysanthemum.appdata.querries.DBCreateQuery;
import com.chrysanthemum.appdata.querries.DBUpdateQuery;
import com.chrysanthemum.appdata.querries.accounting._subquery.UpdateAccountingData;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DatabaseReference;

import java.time.LocalDate;
import java.util.Scanner;

public class CreateSaleTransaction extends DBCreateQuery<Transaction> {

    private final Transaction transaction;

    public CreateSaleTransaction(LocalDate date, Customer c, String services, int pretax){
        String dateValue = TimeParser.parseDateData(date);

        int time = TimeParser.currentTime();

        transaction = new Transaction(dateValue, time, 0, c, DataStorageModule.generateID(),
                DatabaseStructure.Accounting.SALE_TECH, pretax, 0, services);
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
                .child(transaction.getTech().getID() + "")
                .child("" + transaction.getID()).setValue(transaction.getID());

        // indexed by customer
        commonRef.child(DatabaseStructure.TransactionBranch.CUSTOMER_ID_INDEX)
                .child("" + transaction.getCustomer().getID())
                .child(transaction.getID() + "").setValue(transaction.getID());

        DBUpdateQuery<Void> subQuery = new UpdateAccountingData(transaction);
        subQuery.execute();

        return transaction;
    }
}
