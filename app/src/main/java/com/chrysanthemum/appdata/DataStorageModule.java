package com.chrysanthemum.appdata;

import androidx.lifecycle.MutableLiveData;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.firebase.FireDatabase;
import com.chrysanthemum.firebase.RemoteDataBase;

import java.util.LinkedList;
import java.util.Map;

/**
 * the database as percived by the system,
 * the central data storage unit
 */
public class DataStorageModule implements DataStorageFrontEnd, DataStorageBackEnd {

    private static volatile DataStorageModule database;
    private RemoteDataBase remote;

    public static void init(){
        if(database == null){
            database = new DataStorageModule();
        }
    }

    public static DataStorageFrontEnd getFrontEnd() {
        return database;
    }

    public static DataStorageBackEnd getBackEnd() {
        return database;
    }

    private DataStorageModule(){
        FireDatabase firebase = new FireDatabase();
        firebase.initialization();

        this.setSecurityModule(firebase.generateSecurityModule());
        remote = firebase;
    }

    public void close(){
        logRepo.logout();
        logRepo.releaseAccess();
    }

    //-----------------------------------------------------------------------------------------------

    private SecurityModule logRepo;

    private void setSecurityModule(SecurityModule repo){
        logRepo = repo;
    }

    public SecurityModule getSecurityModule(){
        return logRepo;
    }

    //-----------------------------------------------------------------------------------------------
    private Map<String, Technician> techMap;

    public void storeTechMap(Map<String, Technician> techMap){
        this.techMap = techMap;
    }

    public Iterable<Technician> getTechList(){
        return techMap.values();
    }

    public Technician getTech(long id){
        return techMap.get(id + "");
    }

    //-----------------------------------------------------------------------------------------------

    public void requestCustomerByPhone(long phoneNumber, MutableLiveData<LinkedList<Customer>> dataViewer){

    }

}
