package com.chrysanthemum.appdata.querries.customers;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.RemoteDataBase;
import com.chrysanthemum.appdata.querries.Query;

public class CustomerByIDQuery extends Query<Customer> {

    private final long id;
    public CustomerByIDQuery(long id,
                             DataRetriever<Customer> retriever) {
        super(retriever);
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
