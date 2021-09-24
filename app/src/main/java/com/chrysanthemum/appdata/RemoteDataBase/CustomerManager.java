package com.chrysanthemum.appdata.RemoteDataBase;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;

import java.util.LinkedList;

public interface CustomerManager {

    void uploadCustomer(Customer c);

    void findCustomerByID(long id, DataRetriever<Customer> retriever);
    void findCustomersByName(String name, DataRetriever<LinkedList<Customer>> retriever);

    void findCustomerIDsByPhone(long phoneNumber, DataRetriever<LinkedList<Long>> retriever);

    void changeCustomerName(Customer customer, String name);
    void changeCustomerPhoneNumber(Customer customer, long phone);

}
