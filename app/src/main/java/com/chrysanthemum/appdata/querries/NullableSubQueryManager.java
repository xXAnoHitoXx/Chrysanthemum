package com.chrysanthemum.appdata.querries;

import com.chrysanthemum.appdata.dataType.retreiver.NullableDataRetriever;

public class NullableSubQueryManager<T> extends  SubQueryManager<T> {
    public NullableSubQueryManager(int subQueryCount, NullableDataRetriever<T> ret) {
        super(subQueryCount, ret);
    }
}
