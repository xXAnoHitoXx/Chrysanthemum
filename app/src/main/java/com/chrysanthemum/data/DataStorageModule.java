package com.chrysanthemum.data;

import android.content.Context;

import com.chrysanthemum.firebase.FireDatabase;

import java.util.LinkedList;

/**
 * the database as percived by the logic system,
 * the connector to different modules
 */
public class DataStorageModule implements DataStorageFrontEnd, DataStorageBackEnd {

    private static volatile DataStorageModule database;

    public static void init(Context context){
        if(database == null){
            database = new DataStorageModule(context);
        }
    }

    public static DataStorageFrontEnd getFrontEnd() {
        return database;
    }

    public static DataStorageBackEnd getBackEnd() {
        return database;
    }

    private DataStorageModule(Context context){
        FireDatabase firebase = new FireDatabase();
        firebase.initialization(context);

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
    private LinkedList<TechnicianIdentifier> techList;

    public void storeTechList(LinkedList<TechnicianIdentifier> list){
        techList = list;
    }

    public Iterable<TechnicianIdentifier> getTechList(){
        return techList;
    }
}
