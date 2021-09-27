package com.chrysanthemum.appdata.dataType;

public class Technician {

    public static final String ADMIN = "Admin";
    public static final String Dummy = "Dummy";

    private String name;
    private String role;
    private int colour;
    private long id;

    public Technician(){

    }

    public Technician(String name, String role, int colour, long id){
        this.name = name;
        this.role = role;
        this.colour = colour;
        this.id = id;
    }

    public String getRole(){
        return role;
    }

    public String getName(){
        return name;
    }

    public int getColour(){
        return colour;
    }
    public long getID(){
        return id;
    }
}
