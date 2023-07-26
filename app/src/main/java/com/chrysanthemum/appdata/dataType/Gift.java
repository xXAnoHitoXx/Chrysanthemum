package com.chrysanthemum.appdata.dataType;

public class Gift {
    private String id;
    private String amount;
    private String dateIssued;
    private String dateExpires;

    public Gift(){

    }

    public Gift(String id, String amount, String dateIssued, String dateExpires){
        this.id = id;
        this.amount = amount;
        this.dateExpires = dateExpires;
        this.dateIssued = dateIssued;
    }

    public String getId(){
        return id;
    }

    public String getAmount(){
        return amount;
    }

    public String getDateIssued() {
        return dateIssued;
    }

    public String getDateExpires(){
        return dateExpires;
    }

    public void setAmount(String amount){
        this.amount = amount;
    }

}
