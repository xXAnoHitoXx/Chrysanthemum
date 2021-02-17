package com.chrysanthemum.appdata;

import com.chrysanthemum.appdata.dataType.TechnicianIdentifier;

import java.util.Map;

public interface DataStorageBackEnd {
    void storeTechMap(Map<String, TechnicianIdentifier> techMap);
}
