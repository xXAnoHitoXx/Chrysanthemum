package com.chrysanthemum.appdata;

import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;

/**
 * interface between the UI and the data that are readily available for the whole app
 */
public interface DataStorageFrontEnd {
    void close();
    SecurityModule getSecurityModule();
    Iterable<Technician> getTechList();
    Technician getTech(long id);

}
