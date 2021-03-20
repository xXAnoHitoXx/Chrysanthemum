package com.chrysanthemum.appdata.dataType;

public class SaleRecord {
    private final String date;
    private final Customer c;
    private final long id;

    private final String description;

    public SaleRecord(String date, Customer c, long id, String description) {
        this.date = date;
        this.c = c;
        this.id = id;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public Customer getCustomer() {
        return c;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
