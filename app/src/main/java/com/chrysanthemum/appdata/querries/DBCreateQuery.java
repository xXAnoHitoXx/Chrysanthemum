package com.chrysanthemum.appdata.querries;

/**
 * Create and return an object/entry on the DB
 */
public abstract class DBCreateQuery<T> {
    public abstract T execute();
}
