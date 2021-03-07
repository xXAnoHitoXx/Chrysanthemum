package com.chrysanthemum.ui.dataView.task;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.technicianLogin.TechnicianSelectorPanel;

import java.util.LinkedList;

public interface TaskHostestActivity {
    void clearForm();

    //--------------------------------------------------------------------
    DisplayBoard getBoard();
    EditText createEditableForm(int row);
    TextView createFormLabel(int row);
    Button getFormButton();
    TechnicianSelectorPanel createTechPanel();
    AlertDialog.Builder createAlertBox();
    Resources getResources();
    //-------------------------------------------------------------------
    void setBarText(String s);
    String getMainTaskTitle();
    String formatPhoneNumber(long phone);
}
