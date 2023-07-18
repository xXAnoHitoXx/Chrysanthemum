package com.chrysanthemum.appdata.querries;

/**
 * update data in DB
 * returns updated data
 */
public abstract class DBWriteQuery<T> {
    public abstract T execute();
}
