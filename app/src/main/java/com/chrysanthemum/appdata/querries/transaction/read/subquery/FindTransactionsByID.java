package com.chrysanthemum.appdata.querries.transaction.read.subquery;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;
import com.chrysanthemum.appdata.querries.customer.read.subquery.FindCustomerByID;
import com.chrysanthemum.appdata.querries.util.AsyncSubQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;

public class FindTransactionsByID implements AsyncSubQuery<Transaction> {

    private final long id;
    private TransactionFrame frame;
    private Technician tech;

    public FindTransactionsByID(long id) {
        this.id = id;
    }

    @Override
    public void execute(DataRetriever<Transaction> retriever) {
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + id).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        DataSnapshot snap = task.getResult();

                        if(snap != null){
                            frame = snap.getValue(TransactionFrame.class);

                            if(frame.getTechnicianID() == Transaction.NO_TECH_ID){
                                tech = null;
                            } else if (frame.getTechnicianID() == DatabaseStructure.Accounting.SALE_TECH.getID()){
                                tech = DatabaseStructure.Accounting.SALE_TECH;
                            } else {
                                tech = DataStorageModule.getModule().getTech(frame.getTechnicianID());
                            }

                            AsyncSubQuery<Customer> subQuery = new FindCustomerByID(frame.getCustomerID());
                            subQuery.execute(customer -> retriever.retrievedData(frame.formTransaction(customer, tech)));
                        }
                    }
                });
    }
}
