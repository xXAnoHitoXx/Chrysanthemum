package com.chrysanthemum.appdata;

import com.chrysanthemum.appdata.RemoteDataBase.RemoteDataBase;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.retreiver.NullRetriever;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * the database as percived by the system,
 * the central data storage / interpretation unit
 */
public class DataStorageModule implements DataStorageBackEnd {

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

    public static DataStorageModule getModule() {
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
        remote.getTechnicianMap(data -> {
            techMap = data;
            Technician sale = DatabaseStructure.Accounting.SALE_TECH;
            techMap.put(sale.getID() + "", sale);
            retriever.retrieved();
        });
    }


    public Iterable<Technician> getActiveTechList(){
        Collection<Technician> val = techMap.values();

        LinkedList<Technician> list = new LinkedList<>();

        for(Technician technician : val){
            if(technician.getID() != DatabaseStructure.Accounting.SALE_TECH.getID() && !technician.getRole().equals(Technician.Inactive)){
                list.add(technician);
            }
        }

        return list;
    }

    public Iterable<Technician> getFullTechList(){
        Collection<Technician> val = techMap.values();

        LinkedList<Technician> list = new LinkedList<>();

        for(Technician technician : val){
            if(technician.getID() != DatabaseStructure.Accounting.SALE_TECH.getID()){
                list.add(technician);
            }
        }

        return list;
    }

    public Technician getTech(long id){
        return techMap.get(id + "");
    }

}
