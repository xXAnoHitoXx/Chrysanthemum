package com.chrysanthemum.appdata;

import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;

/**
 * interface between DataStorageModule and UI
 */
public interface DataStorageFrontEnd {
    void close();
    SecurityModule getSecurityModule();
    Iterable<Technician> getTechList();
    Technician getTech(int id);
}
