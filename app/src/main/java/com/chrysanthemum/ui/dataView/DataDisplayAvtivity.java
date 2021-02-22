package com.chrysanthemum.ui.dataView;

import android.os.Bundle;

import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.dataView.task.CustomerFinderTask;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.TaskSelectionButtion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chrysanthemum.R;

import java.util.LinkedList;
import java.util.Objects;

public class DataDisplayAvtivity extends AppCompatActivity implements TaskHostestActivity {

    private final LinkedList<TaskSelectionButtion> taskPanel= new LinkedList<>();
    private Task activeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display_avtivity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        instantiateTaskPanel();

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

        TaskSelectionButtion customerFinder = new TaskSelectionButtion(this){
            @Override
            public CustomerFinderTask getTask() {
                return new CustomerFinderTask(thisActivity);
            }

            @Override
            public String getTaskName() {
                return "Customer Finder";
            }
        };

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

    private void setTask(Task t, String taskName){
        activeTask = t;
        Objects.requireNonNull(getSupportActionBar()).setTitle(taskName);
    }

    public DisplayBoard createBoard(int col) {
        LinearLayout layout = findViewById(R.id.display_columns_list);
        return new DisplayBoard(this, this, layout, col);
    }

    @Override
    public EditText getForm(int row) {
        EditText r = (EditText) ((row == 1)? findViewById(R.id.FormText1)
                        : findViewById(R.id.FormText2));
        return r;
    }

    @Override
    public Button getFormButton() {
        return findViewById(R.id.formButton);
    }
}