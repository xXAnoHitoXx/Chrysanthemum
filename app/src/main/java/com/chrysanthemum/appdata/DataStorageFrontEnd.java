package com.chrysanthemum.appdata;

import androidx.lifecycle.MutableLiveData;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;

import java.util.LinkedList;

/**
 * interface between the UI and the data that are readily available for the whole app
 */
public interface DataStorageFrontEnd {
    void close();
    SecurityModule getSecurityModule();
    Iterable<Technician> getTechList();
    Technician getTech(long id);
    void requestCustomerByPhone(long phoneNumber, MutableLiveData<LinkedList<Customer>> dataViewer);
}
