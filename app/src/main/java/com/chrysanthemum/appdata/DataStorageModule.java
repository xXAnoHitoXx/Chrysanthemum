package com.chrysanthemum.appdata;

import android.annotation.SuppressLint;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.queryHandlers.CustomerByPhoneQuery;
import com.chrysanthemum.appdata.queryHandlers.Query;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.firebase.FireDatabase;
import com.chrysanthemum.firebase.RemoteDataBase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public boolean isDate(String s){
        Scanner scan = new Scanner(s);

        if(!scan.hasNextInt()){
            return false;
        }

        int year = scan.nextInt();

        if(!scan.hasNextInt()){
            return false;
        }

        int m = scan.nextInt();
        String month = (m < 10)? "0" + m: "" + m;

        if(!scan.hasNextInt()){
            return false;
        }

        int d =scan.nextInt();
        String day = (d < 10)? "0"+ d : "" + d;


        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        // Input to be parsed should strictly follow the defined date format
        // above.
        format.setLenient(false);

        String date = day + "/" + month + "/" + year;
        try {
            format.parse(date);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public int parseTime(String s) {
        Scanner scan = new Scanner(s);

        if(!scan.hasNextInt()){
            return -1;
        }

        int hour = scan.nextInt();

        if(!scan.hasNextInt() || hour < 1 || hour > 12){
            return -1;
        }

        int min = scan.nextInt();

        if(!scan.hasNext() || min < 0 || min > 59){
            return -1;
        }

        String m = scan.next();
        m = m.toLowerCase();

        int time = 0;

        if(m.equals("am")){
            if(hour != 12){
                time += hour;
            }
        } else if(m.equals("pm")){
            if(hour != 12){
                time += hour;
            }
            time += 12;
        } else {
            return -1;
        }

        time *= 60;
        time += min;

        return time;
    }

    //-----------------------------------------------------------------------------------------------

    public void requestCustomerByPhone(long phoneNumber, DataRetriever<Map<String, Customer>> retriever){
        Query<Map<String, Customer>> q = new CustomerByPhoneQuery(remote, retriever, phoneNumber);
        q.executeQuery();
    }

    public Customer createNewCustomerEntry(String name, long phoneNumber){
        Customer c = new Customer(name, phoneNumber, generateID());
        remote.uploadCustomer(c);
        return c;
    }

    //-----------------------------------------------------------------------------------------------

    public Transaction createAppointment(String date, int time, Customer c){
        Transaction transaction = new Transaction(date, time, c, generateID());
        remote.uploadTransaction(transaction);
        return transaction;
    }

    public void attachTransactionTech(Transaction transaction, Technician technician){
        transaction.setTech(technician);
        remote.attachTransactionTech(transaction);
    }

    public void closeTransaction(Transaction transaction, float amount, float tip, String services){
        transaction.setBill(amount, tip, services);
        remote.closeTransaction(transaction);
    }
}
