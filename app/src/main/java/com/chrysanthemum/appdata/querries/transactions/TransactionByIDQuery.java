package com.chrysanthemum.appdata.querries.transactions;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;
import com.chrysanthemum.appdata.RemoteDataBase;
import com.chrysanthemum.appdata.querries.Query;
import com.chrysanthemum.appdata.querries.customers.CustomerByIDQuery;
import com.chrysanthemum.firebase.DatabaseStructure;

public class TransactionByIDQuery extends Query<Transaction> {

    private final long id;
    private TransactionFrame frame;
    private Technician tech;

    public TransactionByIDQuery(long id,
                                DataRetriever<Transaction> retriever) {
        super(retriever);

        this.id = id;
    }

    @Override
    public void executeQuery() {
        getRemoteDB().findTransactionByID(id, new DataRetriever<TransactionFrame>() {
            @Override
            public void retrievedData(TransactionFrame data) {
                frame = data;
                tech = (frame.getTechnicianID() > 0)? DataStorageModule.getFrontEnd().getTech(frame.getTechnicianID()) : null;
                if(frame.getTechnicianID() == DatabaseStructure.Accounting.SALE_TECH.getID()){
                    tech = DatabaseStructure.Accounting.SALE_TECH;
                }
                customerByIDSubQuery(frame.getCustomerID());

            }
        });
    }

    private void customerByIDSubQuery(long id){
        CustomerByIDQuery q = new CustomerByIDQuery(id,
                new DataRetriever<Customer>() {
                    @Override
                    public void retrievedData(Customer c) {
                        Transaction data = frame.formTransaction(c, tech);
                        complete(data);
                    }
                });
        q.executeQuery();
    }
}
