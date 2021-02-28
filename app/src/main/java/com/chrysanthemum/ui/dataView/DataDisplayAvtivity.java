package com.chrysanthemum.ui.dataView;

import android.os.Bundle;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.dataView.task.CustomerFinderTask;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.taskListener.CustomerFoundListener;
import com.chrysanthemum.ui.dataView.task.TaskSelectionButtion;
import com.chrysanthemum.ui.technicianLogin.TechnicianSelectorPanel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chrysanthemum.R;

import java.util.LinkedList;
import java.util.Objects;

import static com.chrysanthemum.appdata.Util.AppUtil.dpToPx;

public class DataDisplayAvtivity extends AppCompatActivity implements TaskHostestActivity {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void instantiateTaskPanel(){
        final DataDisplayAvtivity thisActivity = this;

        TaskSelectionButtion customerFinder = CustomerFinderTask.getMenuButton(this, this,
                new CustomerFoundListener() {
                    @Override
                    public void found(Customer customer) {
                        //TODO
                    }
                });

        addTaskPanelButton(customerFinder);
    }

    private void addTaskPanelButton(TaskSelectionButtion button){
        button.setOnClickListener(getTaskPannelButtonListener(button));
        button.setText(button.getTaskName());
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setMinHeight(100);
        taskPanel.add(button);
        LinearLayout l = findViewById(R.id.action_list);
        l.addView(button);
        button.invalidate();
    }

    private View.OnClickListener getTaskPannelButtonListener(final TaskSelectionButtion button){
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

    public String getTaskTitle(){
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

    private LinkedList<View> formEntries = new LinkedList<>();

    public void clearForm(){
        ConstraintLayout form = (ConstraintLayout)findViewById(R.id.Form);

        for(View v : formEntries){
            form.removeView(v);
        }

        formEntries.clear();

        LinearLayout list = findViewById(R.id.TechList);
        list.removeAllViews();
    }

    public EditText createEditableForm(int row) {
        EditText e = new EditText(this);
        setRow(e, row);

        return e;
    }

    private void setRow(View v, int row){
        int viewID = ViewCompat.generateViewId();
        v.setId(viewID);
        int layoutID = R.id.Form;
        ConstraintLayout form = (ConstraintLayout)findViewById(layoutID);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.connect(viewID, ConstraintSet.LEFT, layoutID, ConstraintSet.LEFT,
                dpToPx(this, 75));
        constraintSet.connect(viewID, ConstraintSet.RIGHT, R.id.formButton, ConstraintSet.LEFT,
                dpToPx(this, 75));

        int topMargin, botMargin;

        if(row == 1){
            topMargin = R.dimen.data_view_form_height_div;
            botMargin = R.dimen.data_view_form_height_text_align;
        } else {
            topMargin = R.dimen.data_view_form_height_text_align;
            botMargin = R.dimen.data_view_form_height_div;
        }

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
        return findViewById(R.id.formButton);
    }

    public TechnicianSelectorPanel createTechList() {
        LinearLayout list = findViewById(R.id.TechList);
        return new TechnicianSelectorPanel(this, list, false);
    }
}