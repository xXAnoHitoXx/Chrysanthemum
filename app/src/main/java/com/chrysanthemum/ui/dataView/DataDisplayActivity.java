package com.chrysanthemum.ui.dataView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.Util.Scaler;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.dataView.task.AppointmentViewerTask;
import com.chrysanthemum.ui.dataView.task.CustomerManagerTask;
import com.chrysanthemum.ui.dataView.task.GiftManager;
import com.chrysanthemum.ui.dataView.task.SaleTask;
import com.chrysanthemum.ui.dataView.task.accounting.Daily.TallyTask;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.TaskSelectionButtion;
import com.chrysanthemum.ui.dataView.task.accounting.Daily.DailyAccountingTask_Admin;
import com.chrysanthemum.ui.dataView.task.accounting.Daily.DailyAccountingTask_Personal;
import com.chrysanthemum.ui.dataView.task.accounting.Monthly.GenerateMonthlyTotalTask;
import com.chrysanthemum.ui.dataView.task.display.MultiTechnicianSelectorPanel;
import com.chrysanthemum.ui.technicianLogin.TechnicianLoginActivity;
import com.chrysanthemum.ui.technicianLogin.TechnicianSelectorPanel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chrysanthemum.R;

import java.util.LinkedList;
import java.util.Objects;

import static com.chrysanthemum.appdata.Util.AppUtil.dpToPx;

public class DataDisplayActivity extends AppCompatActivity implements TaskHostestActivity {

    private DisplayBoard db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display_avtivity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setBarText(DataStorageModule.getFrontEnd().getSecurityModule().getLoggedinTech().getName());

        instantiateTaskPanel();

        ConstraintLayout layout = findViewById(R.id.Board);
        db = new DisplayBoard(this, layout);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    @Override
    public void onBackPressed(){

    }


    private void logout(){
        DataStorageModule.getFrontEnd().getSecurityModule().logout();

        Intent intent = new Intent(this, TechnicianLoginActivity.class);
        startActivity(intent);
    }

    private void instantiateTaskPanel(){

        TaskSelectionButtion appointmentViewer = AppointmentViewerTask.getMenuButton(this, this);
        addTaskPanelButton(appointmentViewer);

        TaskSelectionButtion sales = SaleTask.getMenuButton(this, this);
        addTaskPanelButton(sales);

        TaskSelectionButtion giftCardManager = GiftManager.getMenuButton(this, this);
        addTaskPanelButton(giftCardManager);

        TaskSelectionButtion customerManager = CustomerManagerTask.getMenuButton(this, this);
        addTaskPanelButton(customerManager);

        TaskSelectionButtion personalAccounting = DailyAccountingTask_Personal.getMenuButton(this, this);
        addTaskPanelButton(personalAccounting);

        //------------------------------------------------------------------------------------------

        Technician tech = DataStorageModule.getFrontEnd().getSecurityModule().getLoggedinTech();

        if(tech.getRole().equals(Technician.ADMIN)){
            addTaskPanelDivider();

            TaskSelectionButtion dailyAccounting = DailyAccountingTask_Admin.getMenuButton(this, this);
            addTaskPanelButton(dailyAccounting);

            TaskSelectionButtion dailyClosing = TallyTask.getMenuButton(this, this);
            addTaskPanelButton(dailyClosing);

            TaskSelectionButtion monthReport = GenerateMonthlyTotalTask.getMenuButton(this, this);
            addTaskPanelButton(monthReport);

        }

        Context c = this;

        System.out.println(c.getFilesDir().getAbsolutePath());

        //------------------------------------------------------------------------------------------

        // starting default Task
        setTask(appointmentViewer.getTask(), appointmentViewer.getTaskName());
    }

    private void addTaskPanelDivider() {
        TaskSelectionButtion button = new TaskSelectionButtion(this) {
            @Override
            public Task getTask() {
                return null;
            }

            @Override
            public String getTaskName() {
                return "--------------------------------";
            }
        };

        addTaskPanelButton(button, null);
    }

    private void addTaskPanelButton(TaskSelectionButtion button){
        addTaskPanelButton(button, getTaskPanelButtonListener(button));
    }

    private void addTaskPanelButton(TaskSelectionButtion button, View.OnClickListener listener){
        button.setOnClickListener(listener);
        button.setText(button.getTaskName());
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setMinHeight(100);
        LinearLayout l = findViewById(R.id.action_list);
        l.addView(button);
        button.invalidate();
    }

    private View.OnClickListener getTaskPanelButtonListener(final TaskSelectionButtion button){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTask(button.getTask(), button.getTaskName());
            }
        };
    }

    //----------------------------------------------------------------------------------------------

    private String taskTitle;

    private void setTask(Task t, String taskName){
        taskTitle = taskName;
        setBarText(taskName);
        t.start();
    }

    public String getMainTaskTitle(){
        return taskTitle;
    }

    @Override
    public String formatPhoneNumber(long phone) {

        StringBuilder builder = new StringBuilder();

        builder.append("-");
        builder.append(phone % 10000);

        phone /= 10000;
        builder.insert(0, phone % 1000);
        builder.insert(0, ")");

        phone /= 1000;
        builder.insert(0, phone % 1000);
        builder.insert(0, "(");

        phone /= 1000;
        builder.insert(0, phone);
        builder.insert(0, "+");

        return builder.toString();
    }

    @Override
    public void popMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void setBarText(String s){
        Objects.requireNonNull(getSupportActionBar()).setTitle(s);
    }

    //----------------------------------------------------------------------------------------------

    public DisplayBoard getBoard() {
        return db;
    }

    private final LinkedList<View> formEntries = new LinkedList<>();

    public void clearForm(){
        ConstraintLayout form = findViewById(R.id.Form);

        for(View v : formEntries){
            form.removeView(v);
        }

        formEntries.clear();

        LinearLayout list = findViewById(R.id.TechList);
        list.removeAllViews();
    }

    public EditText createEditableForm(int row) {
        row = row * 2 - 1;
        EditText e = new EditText(this);
        setRow(e, row);

        return e;
    }

    @Override
    public TextView createFormLabel(int row) {
        row = (row - 1) * 2;

        TextView t = new TextView(this);
        setRow(t, row);

        t.setGravity(Gravity.BOTTOM | Gravity.START);

        return t;
    }

    private void setRow(View v, int row){
        int viewID = ViewCompat.generateViewId();
        v.setId(viewID);
        int layoutID = R.id.Form;
        ConstraintLayout form = findViewById(layoutID);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.connect(viewID, ConstraintSet.LEFT, layoutID, ConstraintSet.LEFT,
                dpToPx(this, 75));
        constraintSet.connect(viewID, ConstraintSet.RIGHT, R.id.formButton, ConstraintSet.LEFT,
                dpToPx(this, 75));

        int topMargin = row * 40;
        int botMargin = (5 - row - 1) * 40;

        constraintSet.connect(viewID, ConstraintSet.TOP, layoutID, ConstraintSet.TOP,
                dpToPx(this, topMargin));
        constraintSet.connect(viewID, ConstraintSet.BOTTOM, layoutID, ConstraintSet.BOTTOM,
                dpToPx(this, botMargin));

        form.addView(v);
        constraintSet.applyTo(form);
        formEntries.add(v);
    }

    @Override
    public Button getFormButton() {
        Button button = findViewById(R.id.formButton);
        button.setVisibility(View.VISIBLE);
        return button;
    }

    public TechnicianSelectorPanel createTechPanel() {
        LinearLayout list = findViewById(R.id.TechList);
        return new TechnicianSelectorPanel(this, list, false);
    }

    @Override
    public MultiTechnicianSelectorPanel createMultiTechPanel() {
        LinearLayout list = findViewById(R.id.TechList);
        return new MultiTechnicianSelectorPanel(this, list);
    }

    @Override
    public AlertDialog.Builder createAlertBox() {
        return new AlertDialog.Builder(this);
    }

    @Override
    public Scaler getScale() {
        return new Scaler(1);
    }

}