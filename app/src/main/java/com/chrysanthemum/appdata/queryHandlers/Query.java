package com.chrysanthemum.appdata.queryHandlers;

import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.firebase.RemoteDataBase;


public abstract class Query<T> {

    private DataRetriever<T> retriever;
    private RemoteDataBase remote;
    private boolean completed;

    public Query(RemoteDataBase remote, DataRetriever<T> retriever){
        this.remote = remote;
        this.retriever = retriever;
        completed = false;
    }

    public abstract void executeQuery();
    public boolean isCompleted(){
        return completed;
    }

    protected void complete(T data){
        completed = true;
        retriever.retrievedData(data);
    }

    protected RemoteDataBase getRemoteDB(){
        return remote;
    }
}
