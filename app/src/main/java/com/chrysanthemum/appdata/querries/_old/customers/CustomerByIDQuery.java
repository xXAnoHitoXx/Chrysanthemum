package com.chrysanthemum.appdata.querries._old.customers;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries._old.Query;

public class CustomerByIDQuery extends Query<Customer> {

    private final long id;
    public CustomerByIDQuery(long id,
                             DataRetriever<Customer> retriever) {
        super(retriever);
        this.id = id;
    }

    @Override
    public void executeQuery() {
        getRemoteDB().getCustomerManager().findCustomerByID(id, this::complete);
    }
}
