package com.chrysanthemum.ui.dataView.task;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class TaskSelectionButtion extends androidx.appcompat.widget.AppCompatButton {

    public TaskSelectionButtion(@NonNull Context context) {
        super(context);
    }

    public TaskSelectionButtion(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract Task getTask();
    public abstract String getTaskName();
}
