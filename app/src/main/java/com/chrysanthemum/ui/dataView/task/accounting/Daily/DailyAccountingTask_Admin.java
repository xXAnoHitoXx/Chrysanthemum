package com.chrysanthemum.ui.dataView.task.accounting.Daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.MoneyParser;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.accounting.LoadTransactionRecordsOfDateQuery;
import com.chrysanthemum.appdata.querries.transactions.UpdateTransactionRecordQuery;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.TaskSelectionButtion;
import com.chrysanthemum.ui.dataView.task.accounting.Cal.Amount;
import com.chrysanthemum.ui.dataView.task.subTasks.DaySelectorTask;
import com.chrysanthemum.ui.dataView.task.subTasks.MultiTechSelectionTask;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class DailyAccountingTask_Admin extends DailyAccountingTask {

    private static final float TASK_SCALE = 2f;

    private String selectedDate;
    private LinkedList<Transaction> transactionList;

    public DailyAccountingTask_Admin(TaskHostestActivity host) {
        super(host);
    }

    @Override
    public void start() {
        selectedDate = TimeParser.parseDateDisplayDay(LocalDate.now());
        setUpDaySelectorForm();
        host.getBoard().clear(host.getScale().scale(TASK_SCALE));
    }

    @SuppressLint("SetTextI18n")
    private void setUpDaySelectorForm() {
        host.clearForm();

        TextView label = host.createFormLabel(1);
        final EditText daySelect = host.createEditableForm(1);

        label.setText("Day:");
        daySelect.setFocusable(false);
        daySelect.setText(selectedDate);

        daySelect.setOnClickListener(v -> {
            Task sub = new DaySelectorTask(host, date -> {
                selectedDate = TimeParser.parseDateDisplayDay(date);
                setUpDaySelectorForm();
                host.getBoard().clear(host.getScale().scale(TASK_SCALE));
            });

            sub.start();
        });

        Button button = host.getFormButton();
        button.setText("Select");
        button.setOnClickListener(v -> {
            String date = selectedDate.replaceAll("/", " ");

            if(date != null){
                startTechSelectTask(date);
            } else {
                daySelect.setError("Tinn Fucked Up! Tell her to fix her damn app OMG!");
            }
        });
    }

    private void startTechSelectTask(final String date){
        MultiTechSelectionTask task = new MultiTechSelectionTask(host, techs -> {
            setUpDaySelectorForm();
            loadBoard(date, techs);
        });
        task.start();
    }


    private void loadBoard(String date, final LinkedList<Technician> techs) {
        host.getBoard().clear(host.getScale().scale(TASK_SCALE));
        displayLabel();

        LoadTransactionRecordsOfDateQuery query = new LoadTransactionRecordsOfDateQuery(date, techs, transactionList -> {
            setTransactionList(transactionList);
            displayTransactions();
        });

        query.executeQuery();
    }

    private void setTransactionList(LinkedList<Transaction> transactionList){
        this.transactionList = transactionList;
    }

    private void displayTransactions(){
        host.getBoard().clear(host.getScale().scale(TASK_SCALE));
        displayLabel();

        DailyUtil.orderedByTime(transactionList);

        int row = 1;

        Amount shopTotal = new Amount();

        Map<Long, Amount> techTotals = new TreeMap<>();

        for(final Transaction transaction : transactionList){
            displayTransaction(transaction, row++, v -> {
                setUpTransactionEditForm(transaction);
                return true;
            });

            shopTotal.add(transaction.getAmount(), transaction.getTip());

            if(techTotals.containsKey(transaction.getTech().getID())){
                techTotals.get(transaction.getTech().getID()).add(transaction.getAmount(), transaction.getTip());
            } else {
                Amount amt = new Amount();
                amt.add(transaction.getAmount(), transaction.getTip());
                techTotals.put(transaction.getTech().getID(), amt);
            }
        }

        for(long techID : techTotals.keySet()){
            Technician tech = DataStorageModule.getFrontEnd().getTech(techID);
            Amount techTotal = techTotals.get(techID);

            displayTotal(tech.getName(), techTotal.getAmount(), techTotal.getTip(), tech.getColour(), row++);
        }

        displayTotal("Shop", shopTotal.getAmount(), shopTotal.getTip(), Color.LTGRAY, row);
    }

    @SuppressLint("SetTextI18n")
    private void setUpTransactionEditForm(final Transaction transaction){
        host.clearForm();

        TextView amL = host.createFormLabel(1);
        amL.setText("Amount (tip):");
        final EditText payment = host.createEditableForm(1);
        payment.setText(MoneyParser.reverseParse(transaction.getAmount(), transaction.getTip()));

        TextView svcL = host.createFormLabel(2);
        svcL.setText("Service:");
        final EditText svc = host.createEditableForm(2);
        svc.setText(transaction.getServices());

        Button button = host.getFormButton();
        button.setText("Update Record");
        button.setOnClickListener(v -> {
            String money = payment.getText().toString();

            money = money.replaceAll("\\(" , " ");
            money = money.replaceAll("[$).]", "");
            money = money.replaceAll("[ ]+", " ");

            int[] pay = MoneyParser.parsePayment(money);

            if(pay == null){
                payment.setError("example: $57.50 ($2.00)");
                return;
            }

            new UpdateTransactionRecordQuery(transaction, pay[0], pay[1], svc.getText().toString()).executeQuery();
            displayTransactions();
        });
    }

    public static TaskSelectionButtion getMenuButton(Context context, final TaskHostestActivity host) {
        return new TaskSelectionButtion(context) {
            @Override
            public Task getTask() {
                return new DailyAccountingTask_Admin(host);
            }

            @Override
            public String getTaskName() {
                return "Daily Shop Record";
            }
        };
    }
}
