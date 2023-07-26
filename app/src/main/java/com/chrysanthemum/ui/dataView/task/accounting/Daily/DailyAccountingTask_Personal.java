package com.chrysanthemum.ui.dataView.task.accounting.Daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Amount;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.TimedOutException;
import com.chrysanthemum.appdata.querries.transaction.read.LoadTransactionOfTechnicianOnDate;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.TaskSelectionButton;
import com.chrysanthemum.ui.dataView.task.subTasks.DaySelectorTask;

import java.time.LocalDate;
import java.util.List;

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

        daySelect.setOnClickListener(v -> {
            Task sub = new DaySelectorTask(host, this::loadBoard);

            sub.start();
        });

        host.getFormButton().setVisibility(View.INVISIBLE);

        loadBoard(today);
    }

    private void loadBoard(LocalDate date) {
        host.getBoard().clear(host.getScale().scale(TASK_SCALE));

        final Technician tech = DataStorageModule.getModule().getSecurityModule().getLoggedinTech();

        try {
            DBReadQuery<List<Transaction>> query = new LoadTransactionOfTechnicianOnDate(tech.getID(), TimeParser.parseDateData(date));
            List<Transaction> transactionList = query.execute();

            displayLabel();

            DailyUtil.orderedByTime(transactionList);

            int row = 1;

            Amount shopTotal = new Amount();

            for(Transaction transaction : transactionList){
                displayTransaction(transaction, row++, null);

                shopTotal.add(transaction.getAmount(), transaction.getTip());
            }

            displayTotal(tech.getName(), shopTotal.getAmount(), shopTotal.getTip(), Color.LTGRAY, row);
        } catch (TimedOutException e) {
            host.popMessage("Load Transactions Timed Out");
        }
    }

    public static TaskSelectionButton getMenuButton(Context context, final TaskHostestActivity host) {
        return new TaskSelectionButton(context) {
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
