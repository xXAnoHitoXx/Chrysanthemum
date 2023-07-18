package com.chrysanthemum.appdata.querries.util;

public class SyncFlag {
    private boolean flag = false;

    public synchronized void set(boolean b){
        flag = b;
    }

    public synchronized boolean read(){
        return flag;
    }
}
