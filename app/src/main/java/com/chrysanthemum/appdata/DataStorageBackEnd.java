package com.chrysanthemum.appdata;

import com.chrysanthemum.appdata.security.TechnicianIdentifier;

import java.util.LinkedList;

public interface DataStorageBackEnd {
    void storeTechList(LinkedList<TechnicianIdentifier> list);
}
