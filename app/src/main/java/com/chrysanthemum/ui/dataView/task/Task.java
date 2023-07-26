package com.chrysanthemum.ui.dataView.task;

public abstract class Task {
    protected final TaskHostestActivity host;

    public Task(TaskHostestActivity host) {
        this.host = host;
    }

    public abstract void start();
}
