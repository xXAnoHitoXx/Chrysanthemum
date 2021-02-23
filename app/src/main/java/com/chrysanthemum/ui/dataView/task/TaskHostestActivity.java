package com.chrysanthemum.ui.dataView.task;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chrysanthemum.ui.dataView.display.DisplayBoard;

public interface TaskHostestActivity {
    DisplayBoard createBoard(int col);
    EditText getForm(int row);
    Button getFormButton();
    LinearLayout getTechList();
    void setBarText(String s);
    String getTaskTitle();
    String formatPhoneNumber(long phone);
}
