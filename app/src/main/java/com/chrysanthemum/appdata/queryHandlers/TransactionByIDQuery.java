package com.chrysanthemum.appdata.queryHandlers;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;
import com.chrysanthemum.firebase.RemoteDataBase;

public class TransactionByIDQuery extends Query<Transaction> {

    private final long id;
    private TransactionFrame frame;
    private Technician tech;

    public TransactionByIDQuery(RemoteDataBase remote, long id,
                                DataRetriever<Transaction> retriever) {
        super(remote, retriever);

        this.id = id;
    }

    @Override
    public void executeQuery() {
        getRemoteDB().findTransactionByID(id, new DataRetriever<TransactionFrame>() {
            @Override
            public void retrievedData(TransactionFrame data) {
                frame = data;
                tech = DataStorageModule.getFrontEnd().getTech(frame.getTechnicianID());
                customerByIDSubQuery(frame.getCustomerID());

            }
        });
    }

    private void customerByIDSubQuery(long id){
        CustomerByIDQuery q = new CustomerByIDQuery(getRemoteDB(), id,
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
