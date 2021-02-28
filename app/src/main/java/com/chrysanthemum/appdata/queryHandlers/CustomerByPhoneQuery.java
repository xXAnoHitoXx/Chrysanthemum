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

    public CustomerByPhoneQuery(RemoteDataBase remote, long phoneNumber,
                                DataRetriever<Map<String, Customer>> retriever) {
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
                                customerByIDSubQuery(id);
                            }
                        } else {
                            complete(data);
                        }
                    }
                });
    }

    private void customerByIDSubQuery(long id){
        CustomerByIDQuery q = new CustomerByIDQuery(getRemoteDB(), id, new DataRetriever<Customer>() {
            @Override
            public void retrievedData(Customer c) {
                retrievedSubQueryData(c);

                if(data.size() == entryCount){
                    complete(data);
                }
            }
        });

        q.executeQuery();
    }

    private synchronized void retrievedSubQueryData(Customer c){
        if(data.size() < entryCount){
            data.put(c.getName(), c);
        }
    }
}
