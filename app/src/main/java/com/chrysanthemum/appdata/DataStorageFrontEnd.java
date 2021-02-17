package com.chrysanthemum.appdata;

import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.security.TechnicianIdentifier;

/**
 * interface between DataStorageModule and UI
 */
public interface DataStorageFrontEnd {
    void close();
    SecurityModule getSecurityModule();
    Iterable<TechnicianIdentifier> getTechList();
}
