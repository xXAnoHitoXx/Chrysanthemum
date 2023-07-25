package com.chrysanthemum.appdata.querries.util;

import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;

public interface SubQuery<T> {
    void execute(DataRetriever<T> retriever);
}
