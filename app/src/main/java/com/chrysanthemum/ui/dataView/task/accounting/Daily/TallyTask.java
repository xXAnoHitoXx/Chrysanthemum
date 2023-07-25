package com.chrysanthemum.ui.dataView.task.accounting.Daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.dataType.DailyTally;
import com.chrysanthemum.appdata.Util.Scaler;
import com.chrysanthemum.appdata.dataType.parsing.MoneyParser;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.DBUpdateQuery;
import com.chrysanthemum.appdata.querries.accounting.UpdateClosingData;
import com.chrysanthemum.appdata.querries.accounting.read.ReadDailyTallyOfDate;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.TaskSelectionButtion;
import com.chrysanthemum.ui.dataView.task.display.LineDisplayLayoutTask;
import com.chrysanthemum.ui.dataView.task.subTasks.DaySelectorTask;

import java.time.LocalDate;
import java.util.Scanner;

public class TallyTask  extends LineDisplayLayoutTask {

    private static final float TASK_SCALE = 2f;

    public TallyTask(TaskHostestActivity host) {
        super(host);
    }
    private LocalDate selectedDate;

    @Override
    public void start() {
        host.setBarText(host.getMainTaskTitle());
        selectedDate = LocalDate.now().minusDays(1);
        setupDaySelectionForm();
        displayClosingData();
    }

    @SuppressLint("SetTextI18n")
    private void setupDaySelectionForm() {
        host.clearForm();

        final TextView dateLabel = host.createFormLabel(1);
        dateLabel.setText("Date:");

        final EditText dateForm = host.createEditableForm(1);
        dateForm.setText(TimeParser.parseDateDisplayDay(selectedDate));
        dateForm.setFocusable(false);

        dateForm.setOnClickListener(v -> setupDaySelectorTask());

        Button button = host.getFormButton();
        button.setVisibility(View.INVISIBLE);
    }

    private void setupDaySelectorTask(){
        DaySelectorTask task = new DaySelectorTask(host, date -> {
            selectedDate = date;
            setupDaySelectionForm();
            displayClosingData();
        });

        task.start();
    }

    private void displayClosingData(){
        DBReadQuery<DailyTally> query = new ReadDailyTallyOfDate(selectedDate);
        DailyTally tally = query.execute();

        if(tally == null){
            host.popMessage("Daily Tally Loading Timed Out!");
        } else {
            displayTally(tally);
        }
    }

    private void displayTally(DailyTally tally){
        host.getBoard().clear(new Scaler(TASK_SCALE));

        String[] topLabels = new String[] {
                "Difference" , "Amount", "Tip", "Tax"
        };

        displayLine(topLabels, new int[] {Color.GREEN, Color.GRAY, Color.GRAY, Color.GRAY}, 0);

        String[] topData = new String[] {
                tally.getDiff(), tally.getAmount(), tally.getTip(), tally.getTax()
        };

        displayLine(topData, new int[] {Color.GREEN, Color.LTGRAY, Color.LTGRAY, Color.LTGRAY}, 1);

        String[] closeLabels = {
                "Cash" , "Machine", "Gift", "Discounts"
        };

        displayLine(closeLabels, Color.GRAY, 2, null);

        String[] closeData = new String[] {
                tally.getCash(), tally.getMachine(), tally.getGift(), tally.getDiscount()
        };

        displayLine(closeData, Color.LTGRAY, 3, (View v) -> {
            closingUpdateForm(tally);
            return true;
        });
    }

    @SuppressLint("SetTextI18n")
    private void closingUpdateForm(DailyTally tally){
        host.clearForm();

        final TextView label = host.createFormLabel(1);
        label.setText("Cash Machine Gift Discount:");

        final EditText form = host.createEditableForm(1);
        form.setText(tally.getCash() + " " + tally.getMachine() + " " + tally.getGift() + " " + tally.getDiscount());

        Button button = host.getFormButton();
        button.setText("Update!");

        button.setOnClickListener(v -> {

            Scanner scanner = new Scanner(form.getText().toString());

            boolean errorDetected = false;

            long cash =  scanner.hasNext() ? MoneyParser.parseSingleAmount(scanner.next()) : 0;

            if(cash == Integer.MIN_VALUE){
                form.setError("Invalid Amount");
                errorDetected = true;
            }

            long machine = scanner.hasNext() ? MoneyParser.parseSingleAmount(scanner.next()) : 0;

            if(machine == Integer.MIN_VALUE){
                form.setError("Invalid Amount");
                errorDetected = true;
            }

            long gift = scanner.hasNext() ? MoneyParser.parseSingleAmount(scanner.next()) : 0;

            if(gift == Integer.MIN_VALUE){
                form.setError("Invalid Amount");
                errorDetected = true;
            }

            long discount = scanner.hasNext() ? MoneyParser.parseSingleAmount(scanner.next()) : 0;

            if(discount == Integer.MIN_VALUE){
                form.setError("Invalid Amount");
                errorDetected = true;
            }

            if(errorDetected){
                return;
            }

            DBUpdateQuery<DailyTally> query = new UpdateClosingData(cash, machine, gift, discount, selectedDate, tally);
            query.execute();

            displayTally(tally);
            setupDaySelectionForm();
        });
    }

    public static TaskSelectionButtion getMenuButton(final Context context, final TaskHostestActivity host) {
        return new TaskSelectionButtion(context) {
            @Override
            public Task getTask() {
                return new TallyTask(host);
            }

            @Override
            public String getTaskName() {
                return "Daily Closing";
            }
        };
    }
}
