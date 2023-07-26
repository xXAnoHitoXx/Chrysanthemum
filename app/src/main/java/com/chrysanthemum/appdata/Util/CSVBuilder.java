package com.chrysanthemum.appdata.Util;

public class CSVBuilder {
    final StringBuilder file = new StringBuilder();
    boolean nl = true;

    public CSVBuilder(String title){
        append(title);
        newLine();
    }

    public void append(String s){
        if(nl){
            nl = false;
        } else {
            file.append(", ");
        }

        file.append(s);

    }

    public void newLine(){
        file.append("\n");
        nl = true;
    }

    public String toString(){
        return file.toString();
    }
}
