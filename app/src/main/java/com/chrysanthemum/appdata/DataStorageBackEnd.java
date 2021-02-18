package com.chrysanthemum.appdata;

import com.chrysanthemum.appdata.dataType.Technician;

import java.util.Map;

public interface DataStorageBackEnd {
    void storeTechMap(Map<String, Technician> techMap);
}
