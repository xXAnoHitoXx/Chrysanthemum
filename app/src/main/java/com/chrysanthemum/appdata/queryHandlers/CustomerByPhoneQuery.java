package com.chrysanthemum.appdata.queryHandlers;

import androidx.lifecycle.MutableLiveData;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.firebase.RemoteDataBase;

import java.util.LinkedList;

public class CustomerByPhoneQuery {
    private RemoteDataBase remote;
    private final LinkedList<Customer> data;

    public CustomerByPhoneQuery(RemoteDataBase remote) {
        this.remote = remote;
        data = new LinkedList<>();
    }

    public void excecuteQuery(long phoneNumber){
        remote.findCustomerIDsByPhone(phoneNumber,
                new DataRetriever<LinkedList<Long>>() {
                    @Override
                    public void retrievedData(LinkedList<Long> ids) {
                        if(ids != null){
                            for(long id : ids){
                                executeSubQuery(id);
                            }
                        }
                    }
                });
    }

    private void executeSubQuery(long id){
        remote.findCustomerByID(id,
                new DataRetriever<Customer>() {
                    @Override
                    public void retrievedData(Customer c) {
                        data.add(c);
                    }
                });
    }

    public static class Result {
        private final LinkedList<Customer> data;

        private Result(LinkedList<Customer> data){
            this.data = data;
        }

        public LinkedList<Customer> getData(){
            return data;
        }
    }
}
