package com.chrysanthemum.appdata;

import com.chrysanthemum.appdata.dataType.retreiver.NullRetriever;

public interface DataStorageBackEnd {
    /**
     * the back end signify when it is ok to load in starting data
     */
    void loadTechMap(NullRetriever retriever);


}
