package com.chrysanthemum.appdata.dataType;

public class Customer {
    private String name;
    private long phoneNumber;
    private long id;

    public Customer(){

    }

    public Customer(String name, long phoneNumber, long id){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public long getPhoneNumber(){
        return phoneNumber;
    }

    public long getID(){
        return id;
    }
}
