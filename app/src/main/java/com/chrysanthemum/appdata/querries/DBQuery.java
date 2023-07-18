package com.chrysanthemum.appdata.querries;

/**
 * return null when timed out.
 */
public abstract class DBQuery<T> {
    private T data = null;
    private long timeOutInMilliseconds = 5000;

    /**
     * return null when timed out.
     */
    public T execute(){
        long startTime = System.currentTimeMillis();
        executeQuery();

        while (data == null && System.currentTimeMillis() < startTime + timeOutInMilliseconds){
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {}
        }

        return data;
    }

    protected void setData(T data) {
        this.data = data;
    }

    protected void setTimeOutInMilliseconds(long timeout) {
        this.timeOutInMilliseconds = timeout;
    }

    /**
     * the query try to set data value before timeout
     */
    protected abstract void executeQuery();

}
