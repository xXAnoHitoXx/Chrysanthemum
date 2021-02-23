package com.chrysanthemum.firebase;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;

import java.util.LinkedList;

public interface RemoteDataBase {
    /**
     * retrieve a list of customer ids with this phone number
     * list can be empty
     */
    void findCustomerIDsByPhone(long phoneNumber, DataRetriever<LinkedList<Long>> retriever);
    void findCustomerByID(long id, DataRetriever<Customer> retriever);
    void uploadCustomer(Customer c);

    //----------------------------------------------------------------------------------------------

    void uploadTransaction(Transaction transaction);
    void attachTransactionTech(Transaction transaction);
    void closeTransaction(Transaction transaction);
}
