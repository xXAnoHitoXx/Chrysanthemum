package com.chrysanthemum.appdata.querries._old;

import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;

public class SubQueryManager<T> implements DataRetriever<T> {

    private final int subQueryCount;
    private final DataRetriever<T> ret;
    private int returnedSubQuery;

    public SubQueryManager(int subQueryCount, DataRetriever<T> ret) {
        this.subQueryCount = subQueryCount;
        this.ret = ret;
    }

    @Override
    public synchronized void retrievedData(T data) {
        returnedSubQuery++;
        ret.retrievedData(data);
    }

    public boolean isCompleted(){
        return returnedSubQuery >= subQueryCount;
    }
}
