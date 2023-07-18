package com.chrysanthemum.appdata.querries;

/**
 * update data in DB
 * the original object is modified with the new changes
 */
public abstract class DBUpdateQuery<T> {
    public abstract void execute();
}
