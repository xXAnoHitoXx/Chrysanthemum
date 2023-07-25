package com.chrysanthemum.appdata.querries;

/**
 * return null when timed out.
 */
public abstract class DBReadQuery<T> {
    private T data = null;
    private long timeOutInMilliseconds = 5000;

    private Thread waitThread;

    /**
     * return null when timed out.
     */
    public T execute() {
        waitThread = Thread.currentThread();
        executeQuery();

        try {
            Thread.sleep(timeOutInMilliseconds);
        } catch (InterruptedException e) {
            return data;
        }

        return null;
    }

    protected void returnQueryData(T data) {
        this.data = data;
        waitThread.interrupt();
    }

    protected void setTimeOutInMilliseconds(long timeout) {
        this.timeOutInMilliseconds = timeout;
    }

    /**
     * the query try to set data value before timeout
     */
    protected abstract void executeQuery();

}
