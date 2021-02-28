package com.chrysanthemum.appdata.queryHandlers;

import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.firebase.RemoteDataBase;


public abstract class Query<T> {

    private final DataRetriever<T> retriever;
    private final RemoteDataBase remote;
    private boolean incomplete;

    public Query(RemoteDataBase remote, DataRetriever<T> retriever){
        this.remote = remote;
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
        return remote;
    }
}
