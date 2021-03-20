package com.chrysanthemum.appdata.querries;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.RemoteDataBase;

public abstract class InstantQuery<T> {
    public abstract T executeQuery();

    protected RemoteDataBase getRemoteDB(){
        return DataStorageModule.getRemoteDataBaseModule();
    }
}
