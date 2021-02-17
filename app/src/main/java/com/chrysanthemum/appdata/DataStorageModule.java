package com.chrysanthemum.appdata;

import android.content.Context;

import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.TechnicianIdentifier;
import com.chrysanthemum.firebase.FireDatabase;

import java.util.Map;

/**
 * the database as percived by the system,
 * the central data storage unit
 */
public class DataStorageModule implements DataStorageFrontEnd, DataStorageBackEnd {

    private static volatile DataStorageModule database;

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
    private Map<String, TechnicianIdentifier> techMap;

    public void storeTechMap(Map<String, TechnicianIdentifier> techMap){
        this.techMap = techMap;
    }

    public Iterable<TechnicianIdentifier> getTechList(){
        return techMap.values();
    }

    public TechnicianIdentifier getTech(int id){
        return techMap.get(id + "");
    }
}
