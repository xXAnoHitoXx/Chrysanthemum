package com.chrysanthemum.ui.dataView.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.Util.AppUtil;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.retreiver.NullRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionStatus;
import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.dataView.display.Displayable;
import com.chrysanthemum.ui.dataView.task.subTasks.AppointmentClaimTask;
import com.chrysanthemum.ui.dataView.task.subTasks.DaySelectorTask;
import com.chrysanthemum.ui.dataView.task.subTasks.NewAppointmentTask;
import com.chrysanthemum.ui.dataView.task.subTasks.RecordPaymentTask;

import java.time.LocalDate;
import java.util.LinkedList;

public class AppointmentViewerTask extends Task {

    private static final int SLOT_HEIGHT = 50;
    private static final int MINUTE_WIDTH = 4;

    private LinkedList<LinkedList<TransactionDisplay>> todayAppointments;
    private String selectedDate;
    private LinkedList<Transaction> appointmentList = new LinkedList<>();

    public AppointmentViewerTask(TaskHostestActivity host) {
        super(host);
    }

    @Override
    public void start() {
        host.setBarText(host.getMainTaskTitle());
        selectedDate = TimeParser.parseDay(LocalDate.now());
        setupDaySelectionForm();
        loadAppointment();
    }

    @SuppressLint("SetTextI18n")
    private void setupDaySelectionForm() {
        host.clearForm();

        final TextView dateLabel = host.createFormLabel(1);
        dateLabel.setText("Date:");

        final EditText dateForm = host.createEditableForm(1);
        dateForm.setText(selectedDate);
        dateForm.setFocusable(false);

        dateForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDaySelectorTask();
            }
        });

        Button button = host.getFormButton();
        button.setVisibility(View.INVISIBLE);
    }

    private void setupDaySelectorTask(){
        DaySelectorTask task = new DaySelectorTask(host, new DataRetriever<LocalDate>() {
            @Override
            public void retrievedData(LocalDate date) {
                selectedDate = TimeParser.parseDay(date);
                setupDaySelectionForm();
                loadAppointment();
            }
        });

        task.start();
    }

    /**
     * retrieve all appointments from database and request display update
     */
    private void loadAppointment(){
        DataStorageModule.getFrontEnd().loadAppointmentList(selectedDate.replaceAll("/", " "), new DataRetriever<LinkedList<Transaction>>() {
            @Override
            public void retrievedData(LinkedList<Transaction> data) {
                appointmentList = data;
                displayTransactions();
            }
        });

    }

    /**
     * display transactions as stored in local memory
     */
    private void displayTransactions(){
        todayAppointments = new LinkedList<>();

        DisplayBoard board = host.getBoard();
        board.clear();

        // add hour markers
        for(int hour = 8; hour < 21; hour ++){
            board.displayData(new HourMarker(hour));
        }

        for(Transaction transaction : appointmentList){
            addTransactionToDisplay(transaction);
        }
    }

    /**
     * display one additional transaction
     */
    private void addTransactionToDisplay(Transaction transaction){
        int rowNum = 0;

        for(LinkedList<TransactionDisplay> row : todayAppointments){
            Rect space = alocateSpace(rowNum, transaction);
            boolean hasSpace = true;

            for(TransactionDisplay d : row){
                if(AppUtil.AABBCD(d.hitbox, space)){
                    hasSpace = false;
                    break;
                }
            }

            if(hasSpace){
                display(row, space, transaction);
                return;
            }

            rowNum++;
        }

        LinkedList<TransactionDisplay> newRow = new LinkedList<>();
        todayAppointments.add(newRow);

        Rect space = alocateSpace(rowNum, transaction);
        display(newRow, space, transaction);
    }

    private void display(LinkedList<TransactionDisplay> row, Rect space, Transaction transaction){
        TransactionDisplay display = new TransactionDisplay(space, transaction);
        row.add(display);
        host.getBoard().displayData(display);
    }

    private Rect alocateSpace(int rowNum, Transaction transaction){
        int left = getTimeXpos(transaction.getAppointmentTime());
        int right = getTimeXpos(transaction.getAppointmentTime()
                + transaction.getDuration());
        int top = getRowYpos(rowNum + 1); // shift down by 1 row
        int bottom = top + SLOT_HEIGHT;

        return new Rect(left, top, right, bottom);
    }


    private static int getRowYpos(int row){
        return row * (SLOT_HEIGHT + 2);
    }

    private static int getTimeXpos(int time){
        return (time - (8 * 60)) * MINUTE_WIDTH;
    }

    private class TransactionDisplay extends Displayable {

        private final Rect hitbox;
        private final Transaction t;

        public TransactionDisplay(Rect hitbox, Transaction t){
            this.hitbox = hitbox;
            this.t = t;
        }

        @Nullable
        @Override
        public Drawable getBGDrawable(Rect boundingBox) {
            ShapeDrawable drawable =  new ShapeDrawable(new RectShape());

            int color;

            if(t.noShow()){
                color = 0xFF8C8C8C;
            } else if (t.getTech() == null) {
                color = Color.LTGRAY;
            } else {
                color = t.getTech().getColour();
            }

            drawable.getPaint().setColor(color);
            drawable.setBounds(boundingBox);
            return drawable;
        }

        @Override
        public String getDisplayData() {
            return t.getAppointmentDisplayData();
        }

        @Override
        public View.OnClickListener getOnclickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(t.getStatus() == TransactionStatus.Claimed){
                        setupRecordPaymentSubTask();
                    }
                }
            };
        }

        @Override
        public View.OnLongClickListener getOnLongClickListener() {
            return new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setupAppointmentClaimSubTask();
                    return true;
                }
            };
        }

        @Override
        public int getX() {
            return hitbox.left;
        }

        @Override
        public int getY() {
            return hitbox.top;
        }

        @Override
        public int getWidth() {
            return hitbox.right - hitbox.left;
        }

        @Override
        public int getHeight() {
            return hitbox.bottom - hitbox.top;
        }

        private void setupAppointmentClaimSubTask(){
            final Displayable display = this;

            AppointmentClaimTask subTask  = new AppointmentClaimTask(host, t, new NullRetriever() {
                @Override
                public void retrieved() {
                    setupDaySelectionForm();
                    host.getBoard().invalidateData(display);
                }
            });

            subTask.start();
        }

        private void setupRecordPaymentSubTask(){
            final Displayable display = this;

            RecordPaymentTask subTask  = new RecordPaymentTask(host, t, new NullRetriever() {
                @Override
                public void retrieved() {
                    setupDaySelectionForm();
                    host.getBoard().remove(display);
                }
            });

            subTask.start();
        }
    }


    private class HourMarker extends Displayable {

        private final Rect hitbox;
        private final int hour;

        public HourMarker(int hour){
            this.hour = hour;
            int time = hour * 60;
            hitbox = new Rect(getTimeXpos(time), getRowYpos(0),
                    getTimeXpos(time + 60),
                    getRowYpos(0) + SLOT_HEIGHT);
        }

        @Nullable
        @Override
        public Drawable getBGDrawable(Rect boundingBox) {
            ShapeDrawable drawable =  new ShapeDrawable(new RectShape());
            if(hour % 2 == 0){
                drawable.getPaint().setColor(Color.LTGRAY);
            } else {
                drawable.getPaint().setColor(0xFFBBBBBB);
            }
            drawable.setBounds(boundingBox);
            return drawable;
        }

        @Override
        public String getDisplayData() {
            if(hour == 0){
                return "12 am";
            }

            if(hour == 12){
                return "12 pm";
            }

            return (hour < 12)? hour + " am" : (hour - 12) + " pm";
        }

        @Override
        public View.OnClickListener getOnclickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewAppointmentTask subTask = new NewAppointmentTask(host, hour, selectedDate, new DataRetriever<Transaction>() {
                        @Override
                        public void retrievedData(Transaction data) {
                            host.setBarText(host.getMainTaskTitle());
                            setupDaySelectionForm();

                            appointmentList.add(data);
                            displayTransactions();
                        }
                    });

                    subTask.start();
                }
            };
        }

        @Override
        public View.OnLongClickListener getOnLongClickListener() {
            return null;
        }

        @Override
        public int getX() {
            return hitbox.left;
        }

        @Override
        public int getY() {
            return hitbox.top;
        }

        @Override
        public int getWidth() {
            return hitbox.right - hitbox.left;
        }

        @Override
        public int getHeight() {
            return hitbox.bottom - hitbox.top;
        }
    }


    public static TaskSelectionButtion getMenuButton(Context context, final TaskHostestActivity host){
        return new TaskSelectionButtion(context) {
            @Override
            public Task getTask() {
                return new AppointmentViewerTask(host);
            }

            @Override
            public String getTaskName() {
                return "Appointment View";
            }
        };
    }
}
