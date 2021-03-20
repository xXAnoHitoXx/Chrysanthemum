package com.chrysanthemum.ui.dataView.task;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.Util.Scaler;
import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.dataView.task.display.MultiTechnicianSelectorPanel;
import com.chrysanthemum.ui.technicianLogin.TechnicianSelectorPanel;

public interface TaskHostestActivity {
    void clearForm();

    //--------------------------------------------------------------------
    DisplayBoard getBoard();
    EditText createEditableForm(int row);
    TextView createFormLabel(int row);
    Button getFormButton();
    TechnicianSelectorPanel createTechPanel();
    MultiTechnicianSelectorPanel createMultiTechPanel();
    AlertDialog.Builder createAlertBox();
    Resources getResources();
    Scaler getScale();

    //-------------------------------------------------------------------
    void setBarText(String s);
    String getMainTaskTitle();
    String formatPhoneNumber(long phone);
    void popMessage(String message);
}
