package com.chrysanthemum.appdata.querries.customers;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.Query;

import java.util.Map;
import java.util.TreeMap;

public class CustomerByNameQuery extends Query<Map<String, Customer>> {

    private final String name;

    public CustomerByNameQuery(String name, DataRetriever<Map<String, Customer>> retriever) {
        super(retriever);
        this.name = name;
    }

    @Override
    public void executeQuery() {
        getRemoteDB().getCustomerManager().findCustomersByName(name, data -> {

            Map<String, Customer> map = new TreeMap<>();

            for(Customer c : data){
                map.put(c.getName(), c);
            }

            complete(map);
        });
    }
}
