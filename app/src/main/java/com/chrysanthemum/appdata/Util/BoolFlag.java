package com.chrysanthemum.appdata.Util;

public class BoolFlag {

    private boolean f;

    public BoolFlag(){
        f = false;
    }

    public void set(){
        f = true;
    }

    public void reset(){
        f = false;
    }

    public boolean read(){
        return f;
    }
}
