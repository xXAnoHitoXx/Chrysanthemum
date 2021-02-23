package com.chrysanthemum.appdata.queryHandlers;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.firebase.RemoteDataBase;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class CustomerByPhoneQuery extends Query<Map<String, Customer>>{
    private final Map<String, Customer> data;
    private final long phoneNumber;
    private int entryCount;

    public CustomerByPhoneQuery(RemoteDataBase remote, DataRetriever<Map<String, Customer>> retriever,
                                long phoneNumber) {
        super(remote, retriever);
        data = new TreeMap<>();
        this.phoneNumber = phoneNumber;
    }

    public void executeQuery(){
        getRemoteDB().findCustomerIDsByPhone(phoneNumber,
                new DataRetriever<LinkedList<Long>>() {
                    @Override
                    public void retrievedData(LinkedList<Long> ids) {
                        entryCount = ids.size();

                        if(entryCount > 0){
                            for(long id : ids){
                                executeSubQuery(id);
                            }
                        } else {
                            complete(data);
                        }
                    }
                });
    }

    private void executeSubQuery(long id){
        getRemoteDB().findCustomerByID(id, createSubQueryRetriever());
    }

    private DataRetriever<Customer> createSubQueryRetriever(){
        return new DataRetriever<Customer>() {
            @Override
            public void retrievedData(Customer c) {
                retrievedSubQueryData(c);
            }
        };
    }

    private synchronized void retrievedSubQueryData(Customer c){
        if(data.size() < entryCount){
            data.put(c.getName(), c);
        }

        if(data.size() == entryCount && !isCompleted()){
            complete(data);
        }
    }
}
