package com.chrysanthemum.appdata;

import androidx.lifecycle.MutableLiveData;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;

import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 * interface between the UI and the data that are readily available for the whole app
 */
public interface DataStorageFrontEnd {
    void close();
    SecurityModule getSecurityModule();
    Iterable<Technician> getTechList();
    Technician getTech(long id);

    //--------------------------------------------------------------------------------------------------
    void requestCustomerByPhone(long phoneNumber, DataRetriever<Map<String, Customer>> retriever);
    Customer createNewCustomerEntry(String name, long phoneNumber);

    //--------------------------------------------------------------------------------------------------
    void loadAppointmentList(String date, DataRetriever<LinkedList<Transaction>> retriever);

    Transaction createAppointment(String date, int time, int duration, Customer c);
    void attachTransactionTech(Transaction transaction, Technician technician);
    void closeTransaction(Transaction transaction, float amount, float tip, String services);
}
