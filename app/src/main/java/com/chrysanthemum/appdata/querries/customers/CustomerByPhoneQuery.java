package com.chrysanthemum.appdata.querries.customers;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.Query;

import java.util.Map;
import java.util.TreeMap;

public class CustomerByPhoneQuery extends Query<Map<String, Customer>> {
    private final Map<String, Customer> data;
    private final long phoneNumber;
    private int entryCount;

    public CustomerByPhoneQuery(long phoneNumber,
                                DataRetriever<Map<String, Customer>> retriever) {
        super(retriever);
        data = new TreeMap<>();
        this.phoneNumber = phoneNumber;
    }

    public void executeQuery(){
        getRemoteDB().getCustomerManager().findCustomerIDsByPhone(phoneNumber,
                ids -> {
                    entryCount = ids.size();

                    if(entryCount > 0){
                        for(long id : ids){
                            customerByIDSubQuery(id);
                        }
                    } else {
                        complete(data);
                    }
                });
    }

    private void customerByIDSubQuery(long id){
        CustomerByIDQuery q = new CustomerByIDQuery(id, customer -> {

            if(retrievedSubQueryData(customer)){
                complete(data);
            }
        });

        q.executeQuery();
    }

    private synchronized boolean retrievedSubQueryData(Customer c){
        if(data.size() < entryCount){
            data.put(c.getName(), c);

            return data.size() == entryCount;
        }

        return false;
    }
}
