package com.chrysanthemum.appdata.querries.customers;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.retreiver.NullableDataRetriever;
import com.chrysanthemum.appdata.querries.NullableSubQueryManager;
import com.chrysanthemum.appdata.querries.Query;

import java.util.Map;
import java.util.TreeMap;

public class CustomerByPhoneQuery extends Query<Map<String, Customer>>
        implements NullableDataRetriever<Customer> {
    private final Map<String, Customer> data;
    private final long phoneNumber;
    private NullableSubQueryManager<Customer> dom;

    public CustomerByPhoneQuery(long phoneNumber,
                                DataRetriever<Map<String, Customer>> retriever) {
        super(retriever);
        data = new TreeMap<>();
        this.phoneNumber = phoneNumber;
    }

    public void executeQuery(){
        getRemoteDB().getCustomerManager().findCustomerIDsByPhone(phoneNumber,
                ids -> {
                    dom = new NullableSubQueryManager<>(
                            ids.size(), this);

                    if(ids.size() > 0){
                        for(long id : ids){
                            new CustomerByIDQuery(id, dom).executeQuery();
                        }
                    } else {
                        complete(data);
                    }
                });
    }

    @Override
    public void retrievedData(Customer c) {
        if(c != null){
            data.put(c.getName(), c);
        }

        if(dom.isCompleted()){
            complete(data);
        }
    }
}
