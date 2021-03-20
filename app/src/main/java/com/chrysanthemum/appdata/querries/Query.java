package com.chrysanthemum.appdata.querries;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.RemoteDataBase;


public abstract class Query<T> {

    private final DataRetriever<T> retriever;
    private boolean incomplete;

    public Query(DataRetriever<T> retriever){
        this.retriever = retriever;
        incomplete = true;
    }

    public abstract void executeQuery();

    private synchronized boolean isIncomplete(){
        boolean status = incomplete;
        incomplete = false;
        return status;
    }

    // calling retriever outside of synchronization
    protected void complete(T data){
        if(isIncomplete()){
            retriever.retrievedData(data);
        }
    }

    protected RemoteDataBase getRemoteDB(){
        return DataStorageModule.getRemoteDataBaseModule();
    }
}
