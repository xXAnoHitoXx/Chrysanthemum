package com.chrysanthemum.ui.dataView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.dataView.task.AppointmentViewerTask;
import com.chrysanthemum.ui.dataView.task.subTasks.CustomerFinderTask;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.TaskSelectionButtion;
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

import com.chrysanthemum.R;

import java.util.LinkedList;
import java.util.Objects;

import static com.chrysanthemum.appdata.Util.AppUtil.dpToPx;

public class DataDisplayActivity extends AppCompatActivity implements TaskHostestActivity {

    private final LinkedList<TaskSelectionButtion> taskPanel= new LinkedList<>();
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

    private void logout(){
        DataStorageModule.getFrontEnd().getSecurityModule().logout();

        Intent intent = new Intent(this, TechnicianLoginActivity.class);
        startActivity(intent);
    }

    private void instantiateTaskPanel(){

        TaskSelectionButtion appointmentViewer = AppointmentViewerTask.getMenuButton(this, this);
        addTaskPanelButton(appointmentViewer);

        TaskSelectionButtion customerFinder = CustomerFinderTask.getMenuButton(this, this,
                new DataRetriever<Customer>() {
                    @Override
                    public void retrievedData(Customer customer) {
                        //TODO
                    }
                });

        addTaskPanelButton(customerFinder);

        // starting default Task
        setTask(appointmentViewer.getTask(), appointmentViewer.getTaskName());
    }

    private void addTaskPanelButton(TaskSelectionButtion button){
        button.setOnClickListener(getTaskPanelButtonListener(button));
        button.setText(button.getTaskName());
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setMinHeight(100);
        taskPanel.add(button);
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
    public AlertDialog.Builder createAlertBox() {
        return new AlertDialog.Builder(this);
    }

}