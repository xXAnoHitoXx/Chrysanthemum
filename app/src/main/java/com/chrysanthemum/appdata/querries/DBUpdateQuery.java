package com.chrysanthemum.appdata.querries;

/**
 * update data in DB
 * the original object of type T is modified with the new changes
 *
 *
 */
public abstract class DBUpdateQuery<T> {
    public abstract void execute();
}
