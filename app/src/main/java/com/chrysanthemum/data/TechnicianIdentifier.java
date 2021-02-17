package com.chrysanthemum.data;

public class TechnicianIdentifier {
    private String name;
    private int colour;

    public TechnicianIdentifier(){

    }

    public TechnicianIdentifier(String name, int colour){
        this.name = name;
        this.colour = colour;
    }

    public String getName(){
        return name;
    }

    public int getColour(){
        return colour;
    }
}
