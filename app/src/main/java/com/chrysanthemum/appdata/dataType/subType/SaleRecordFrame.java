package com.chrysanthemum.appdata.dataType.subType;

public class SaleRecordFrame {

    private final String date;
    private final long customerID;
    private final long id;

    private final String description;

    public SaleRecordFrame(String date, long customerID, long id, String description) {
        this.date = date;
        this.customerID = customerID;
        this.id = id;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public long getCustomerID() {
        return customerID;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
