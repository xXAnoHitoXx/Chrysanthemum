package com.chrysanthemum.appdata.dataType.retreiver;

import com.chrysanthemum.appdata.dataType.Exception.InternalDatabaseStructureErrorException;

public interface DataRetriever<T> {
    void retrievedData(T data);
}
