package com.chrysanthemum.ui.dataView.task.accounting.Weekly;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.Tax;
import com.chrysanthemum.appdata.dataType.TechTallyBlock;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.parsing.MoneyParser;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.accounting.read.ReadTechTallyBlocks;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.TaskSelectionButtion;
import com.chrysanthemum.appdata.dataType.Amount;
import com.chrysanthemum.ui.dataView.task.display.LineDisplayLayoutTask;
import com.chrysanthemum.ui.dataView.task.subTasks.DaySelectorTask;
import com.chrysanthemum.ui.dataView.task.subTasks.MultiTechSelectionTask;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Objects;
import java.util.TreeMap;

public class WeeklyAccountingTask  extends LineDisplayLayoutTask {

    private static final float TASK_SCALE = 2f;

    private final Technician tech;

    private String dayA, dayB, selA, selB;


    public WeeklyAccountingTask(TaskHostestActivity host, Technician tech) {
        super(host);

        this.tech = tech;
    }

    @Override
    public void start() {
        host.clearForm();
        host.getBoard().clear(host.getScale().scale(TASK_SCALE));
        LocalDate b = LocalDate.now().minusDays(1);
        dayB = TimeParser.parseDateDisplayDay(b);

        LocalDate a = b.minusDays((b.getDayOfWeek().getValue() % 7));
        dayA = TimeParser.parseDateDisplayDay(a);

        setupDaySelectionForm();
    }

    @SuppressLint("SetTextI18n")
    private void setupDaySelectionForm(){
        host.clearForm();

        TextView dayALabel = host.createFormLabel(1);
        final EditText dayASelect = host.createEditableForm(1);

        dayALabel.setText("From:");
        dayASelect.setFocusable(false);
        dayASelect.setText(dayA);

        dayASelect.setOnClickListener(v -> {
            Task sub = new DaySelectorTask(host, date -> {
                dayA = TimeParser.parseDateDisplayDay(date);
                setupDaySelectionForm();
                host.getBoard().clear(host.getScale().scale(TASK_SCALE));
            });

            sub.start();
        });

        TextView dayBLabel = host.createFormLabel(2);
        final EditText dayBSelect = host.createEditableForm(2);

        dayBLabel.setText("To:");
        dayBSelect.setFocusable(false);
        dayBSelect.setText(dayB);

        dayBSelect.setOnClickListener(v -> {
            Task sub = new DaySelectorTask(host, date -> {
                dayB = TimeParser.parseDateDisplayDay(date);
                setupDaySelectionForm();
                host.getBoard().clear(host.getScale().scale(TASK_SCALE));
            });

            sub.start();
        });

        Button button = host.getFormButton();
        button.setText("Select");
        button.setOnClickListener(v -> {

            if(dayA != null && dayB != null){

                if(dayA.equals(selA) && dayB.equals(selB)){
                    dayA = TimeParser.parseDateDisplayDay(Objects.requireNonNull(TimeParser.parseDate(dayA)).minusDays(7));
                    dayB = TimeParser.parseDateDisplayDay(Objects.requireNonNull(TimeParser.parseDate(dayB)).minusDays(7));
                }

                selA = dayA;
                selB = dayB;

                startTechSelectTask(selA, selB);
            } else  if (dayA == null){
                dayASelect.setError("Tinn Fucked Up! Tell her to fix her damn app OMG!");
            } else {
                dayBSelect.setError("Tinn Fucked Up! Tell her to fix her damn app OMG!");
            }
        });
    }

    private void startTechSelectTask(final String dayA, final String dayB){
        if(tech.getRole().equals(Technician.ADMIN)){
            MultiTechSelectionTask task = new MultiTechSelectionTask(host, techs -> {
                setupDaySelectionForm();
                loadBoard(dayA, dayB, techs);
            });
            task.start();
        } else {
            LinkedList<Technician> list = new LinkedList<>();
            list.add(tech);
            loadBoard(dayA, dayB, list);
        }
    }

    private int row = 1;

    private void loadBoard(final String dayA, final String dayB, LinkedList<Technician> techs){
        host.getBoard().clear(host.getScale().scale(TASK_SCALE));
        displayLabel();
        row = 1;

        DBReadQuery<TreeMap<Long, TechTallyBlock>> query =
                new ReadTechTallyBlocks(
                        TimeParser.parseDate(dayA.replaceAll("/", " ")),
                                TimeParser.parseDate(dayB.replaceAll("/", " ")),
                                        techs);

        TreeMap<Long, TechTallyBlock> blocks = query.execute();

        for(long techID : blocks.keySet()){
            DisplayTallyBlock(blocks.get(techID));
        }
    }

    private void DisplayTallyBlock(TechTallyBlock block){
        LocalDate startDate = TimeParser.parseDate(selA.replaceAll("/", " "));

        for(int i : block.Keys()){
            assert startDate != null;
            String date = TimeParser.parseDateDisplayDay(startDate.plusDays(i));
            Amount a = block.get(i);

            String[] data = {
              date, MoneyParser.parseSingleAmount(a.getAmount()),
                    MoneyParser.parseSingleAmount(a.getTip()),
                    MoneyParser.parseSingleAmount(Tax.getPreTax(a.getTip()))
            };

            displayLine(data, block.getTechColour(), row++, null);
        }
        Amount tots = block.getTotal();
        displayTotal(tots.getAmount(), tots.getTip(), block.getTechColour());
    }

    private void displayTotal(int amount, int tip, int colour){
        String[] data = new String[] {
                "Total:",
                MoneyParser.parseSingleAmount(amount), MoneyParser.parseSingleAmount(tip), MoneyParser.parseSingleAmount(Tax.getPreTax(tip))
        };

        displayLine(data, colour, row++, null);
    }


    private void displayLabel(){
        String[] data = new String[] {
                "Day", "Amount", "Tip", "TPO"
        };

        displayLine(data, Color.GRAY, 0, null);
    }

    public static TaskSelectionButtion getMenuButton(Context context, final TaskHostestActivity host, final Technician tech){
        return new TaskSelectionButtion(context){
            @Override
            public Task getTask() {
                return new WeeklyAccountingTask(host, tech);
            }

            @Override
            public String getTaskName() {
                return "Summary";
            }
        };
    }
}
