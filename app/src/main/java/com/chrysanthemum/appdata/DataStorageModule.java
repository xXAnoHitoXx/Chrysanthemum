package com.chrysanthemum.appdata;

import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.retreiver.NullRetriever;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.firebase.FireDatabase;

import java.util.Map;

/**
 * the database as percived by the system,
 * the central data storage / interpretation unit
 */
public class DataStorageModule implements DataStorageFrontEnd, DataStorageBackEnd {

    private static volatile DataStorageModule database;
    private final RemoteDataBase remote;

    public static void init(){
        if(database == null){
            database = new DataStorageModule();
        }
    }

    public static synchronized long generateID(){
        return System.currentTimeMillis();
    }

    public static RemoteDataBase getRemoteDataBaseModule(){
        return database.remote;
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

    public void loadTechMap(final NullRetriever retriever) {
        remote.getTechnicianMap(new DataRetriever<Map<String, Technician>>() {
            @Override
            public void retrievedData(Map<String, Technician> data) {
                techMap = data;
                retriever.retrieved();
            }
        });
    }

    public Iterable<Technician> getTechList(){
        return techMap.values();
    }

    public Technician getTech(long id){
        return techMap.get(id + "");
    }
}
