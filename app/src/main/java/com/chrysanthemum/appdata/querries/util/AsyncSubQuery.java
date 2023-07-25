package com.chrysanthemum.appdata.querries.util;

import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;

public interface AsyncSubQuery<T> {
    void execute(DataRetriever<T> retriever);
}
