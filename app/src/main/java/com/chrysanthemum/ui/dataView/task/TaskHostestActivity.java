package com.chrysanthemum.ui.dataView.task;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.technicianLogin.TechnicianSelectorPanel;

public interface TaskHostestActivity {
    void clearForm();

    //--------------------------------------------------------------------
    DisplayBoard getBoard();
    EditText createEditableForm(int row);
    Button getFormButton();
    TechnicianSelectorPanel createTechList();

    //-------------------------------------------------------------------
    void setBarText(String s);
    String getTaskTitle();
    String formatPhoneNumber(long phone);
}
