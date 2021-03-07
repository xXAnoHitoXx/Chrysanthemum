package com.chrysanthemum.appdata;

import android.annotation.SuppressLint;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.queryHandlers.CustomerByPhoneQuery;
import com.chrysanthemum.appdata.queryHandlers.LoadAppointmentListByDayQuery;
import com.chrysanthemum.appdata.queryHandlers.Query;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.firebase.FireDatabase;
import com.chrysanthemum.firebase.RemoteDataBase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

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

    private synchronized long generateID(){
        return System.currentTimeMillis();
    }

    //-----------------------------------------------------------------------------------------------

    public void requestCustomerByPhone(long phoneNumber, DataRetriever<Map<String, Customer>> retriever){
        Query<Map<String, Customer>> q = new CustomerByPhoneQuery(remote, phoneNumber, retriever);
        q.executeQuery();
    }

    public Customer createNewCustomerEntry(String name, long phoneNumber){
        Customer c = new Customer(name, phoneNumber, generateID());
        remote.uploadCustomer(c);
        return c;
    }

    //-----------------------------------------------------------------------------------------------

    public void loadAppointmentList(String date, DataRetriever<LinkedList<Transaction>> retriever){
        Query<LinkedList<Transaction>> q = new LoadAppointmentListByDayQuery(remote, date, retriever);
        q.executeQuery();
    }

    public Transaction createAppointment(String date, int time, int duration, Customer c, String services){
        Transaction transaction = new Transaction(date, time, duration, c, generateID(), services);
        remote.uploadTransaction(transaction);
        return transaction;
    }

    @Override
    public void markAsNoShow(Transaction transaction) {
        transaction.markNoShow();
        remote.markNoShow(transaction);
    }

    public void attachTransactionTech(Transaction transaction, Technician technician){
        transaction.setTech(technician);
        remote.attachTransactionTech(transaction);
    }

    public void closeTransaction(Transaction transaction, int amount, int tip, String services){
        transaction.setBill(amount, tip, services);
        remote.closeTransaction(transaction);
    }
}
