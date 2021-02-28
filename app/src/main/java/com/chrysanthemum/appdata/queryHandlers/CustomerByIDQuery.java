package com.chrysanthemum.appdata.queryHandlers;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.firebase.RemoteDataBase;

public class CustomerByIDQuery extends Query<Customer> {

    private final long id;
    public CustomerByIDQuery(RemoteDataBase remote, long id,
                             DataRetriever<Customer> retriever) {
        super(remote, retriever);
        this.id = id;
    }

    @Override
    public void executeQuery() {
        getRemoteDB().findCustomerByID(id, new DataRetriever<Customer>() {
            @Override
            public void retrievedData(Customer c) {
                complete(c);
            }
        });
    }
}
