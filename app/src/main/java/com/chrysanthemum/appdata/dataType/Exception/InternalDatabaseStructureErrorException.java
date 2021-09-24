package com.chrysanthemum.appdata.dataType.Exception;

public class InternalDatabaseStructureErrorException  extends Exception{
    private final String location;

    public InternalDatabaseStructureErrorException(String location){
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
