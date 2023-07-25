package com.chrysanthemum.appdata.querries._old.customers;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries._old.Query;
import com.chrysanthemum.appdata.querries.util.SubQuery;

public class CustomerByIDQuery implements SubQuery<Customer> {
    private final long id;
    public CustomerByIDQuery(long id) {
        this.id = id;
    }

    @Override
    public void execute(DataRetriever<Customer> retriever) {

    }
}
