package com.chrysanthemum.ui.dataView.task.accounting.Daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.Query;
import com.chrysanthemum.appdata.querries.transactions.LoadTransactionsByTechnicianIDQuery;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.TaskSelectionButtion;
import com.chrysanthemum.ui.dataView.task.accounting.Cal.Amount;
import com.chrysanthemum.ui.dataView.task.subTasks.DaySelectorTask;

import java.time.LocalDate;
import java.util.LinkedList;

public class DailyAccountingTask_Personal extends DailyAccountingTask {

    private static final float TASK_SCALE = 2f;

    public DailyAccountingTask_Personal(TaskHostestActivity host) {
        super(host);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void start() {
        host.clearForm();

        TextView label = host.createFormLabel(1);
        EditText daySelect = host.createEditableForm(1);

        LocalDate today = LocalDate.now();

        label.setText("Day:");
        daySelect.setFocusable(false);
        daySelect.setText(TimeParser.parseDateDisplayDay(today));

        daySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task sub = new DaySelectorTask(host, new DataRetriever<LocalDate>() {
                    @Override
                    public void retrievedData(LocalDate date) {
                        loadBoard(date);
                    }

                });

                sub.start();
            }
        });

        host.getFormButton().setVisibility(View.INVISIBLE);

        loadBoard(today);
    }

    private void loadBoard(LocalDate date) {
        host.getBoard().clear(host.getScale().scale(TASK_SCALE));
        displayLabel();

        final Technician tech = DataStorageModule.getFrontEnd().getSecurityModule().getLoggedinTech();

        Query<LinkedList<Transaction>> query = new LoadTransactionsByTechnicianIDQuery(TimeParser.parseDateData(date), tech, new DataRetriever<LinkedList<Transaction>>() {
            @Override
            public void retrievedData(LinkedList<Transaction> transactionList) {

                DailyUtil.orderedByTime(transactionList);

                int row = 1;

                Amount shopTotal = new Amount();

                for(Transaction transaction : transactionList){
                    displayTransaction(transaction, row++, null);

                    shopTotal.add(transaction.getAmount(), transaction.getTip());
                }

                displayTotal(tech.getName(), shopTotal.getAmount(), shopTotal.getTip(), Color.LTGRAY, row);
            }
        });

        query.executeQuery();
    }

    public static TaskSelectionButtion getMenuButton(Context context, final TaskHostestActivity host) {
        return new TaskSelectionButtion(context) {
            @Override
            public Task getTask() {
                return new DailyAccountingTask_Personal(host);
            }

            @Override
            public String getTaskName() {
                return "Daily Record";
            }
        };
    }

}
