package com.chrysanthemum.appdata.dataType;

public class TechnicianIdentifier {
    private String name;
    private int colour;
    private long id;

    public TechnicianIdentifier(){

    }

    public TechnicianIdentifier(String name, int colour, long id){
        this.name = name;
        this.colour = colour;
        this.id = id;
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
