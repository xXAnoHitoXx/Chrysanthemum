package com.chrysanthemum.firebase;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;

import java.util.LinkedList;

public interface RemoteDataBase {
    void findCustomerIDsByPhone(long phoneNumber, DataRetriever<LinkedList<Long>> retriever);
    void findCustomerByID(long id, DataRetriever<Customer> retriever);
    void addNewCustomer(Customer c);
}
