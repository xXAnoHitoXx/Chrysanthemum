package com.chrysanthemum.data;

/**
 * interface between DataStorageModule and UI
 */
public interface DataStorageFrontEnd {
    void close();
    SecurityModule getSecurityModule();
    Iterable<TechnicianIdentifier> getTechList();
}
