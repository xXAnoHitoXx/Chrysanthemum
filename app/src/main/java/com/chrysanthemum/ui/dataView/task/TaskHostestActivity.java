package com.chrysanthemum.ui.dataView.task;

import android.widget.Button;
import android.widget.EditText;

import com.chrysanthemum.ui.dataView.display.DisplayBoard;

public interface TaskHostestActivity {
    DisplayBoard createBoard(int col);
    EditText getForm(int row);
    Button getFormButton();
}
