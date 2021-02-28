package com.chrysanthemum.ui.dataView.task;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.Util.AppUtil;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.dataView.display.Displayable;

import java.util.LinkedList;

public class AppointmentViewerTask extends Task {

    private static final int SLOT_HEIGHT = 50;
    private static final int MINUTE_WIDTH = 2;

    public AppointmentViewerTask(TaskHostestActivity host) {
        super(host);
    }

    @Override
    public void start() {
        setupDefaultStateForm();
        setupDefaultStateBoard();
    }

    @SuppressLint("SetTextI18n")
    private void setupDefaultStateForm() {
        host.clearForm();

        final EditText dateform = host.createEditableForm(1);
        dateform.setText(AppUtil.today());

        Button button = host.getFormButton();
        button.setText("Search");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateform.getText().toString();
                if(AppUtil.isDate(date)){
                    loadAppointment(date);
                } else {
                    dateform.setError("Date: 25 12 2025");
                }
            }
        });

    }

    private void setupDefaultStateBoard(){
        loadAppointment(AppUtil.today());
    }

    LinkedList<LinkedList<TransactionDisplay>> todayAppointments = new LinkedList<>();

    private void loadAppointment(String date){
        DataStorageModule.getFrontEnd().loadAppointmentList(date, new DataRetriever<LinkedList<Transaction>>() {
            @Override
            public void retrievedData(LinkedList<Transaction> data) {
                for(Transaction transaction : data){

                    DisplayBoard board = host.getBoard();
                    board.clear();

                    // add hour markers
                    for(int hour = 8; hour < 21; hour ++){
                        board.displayData(new HourMarker(hour));
                    }

                    loadAppointmentDisplay(transaction);
                }
            }
        });

    }

    private void loadAppointmentDisplay(Transaction transaction){
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
        int top = getRowYpos(rowNum);
        int bottom = top + SLOT_HEIGHT;

        return new Rect(left, top, right, bottom);
    }


    private static int getRowYpos(int row){
        return row * (SLOT_HEIGHT + 2);
    }

    private static int getTimeXpos(int time){
        int hourSlot = (time / 60) - 8;
        return time * MINUTE_WIDTH + (hourSlot * 10);
    }

    private static class TransactionDisplay extends Displayable {

        private final Rect hitbox;
        private final Transaction t;

        public TransactionDisplay(Rect hitbox, Transaction t){
            this.hitbox = hitbox;
            this.t = t;
        }

        @Nullable
        @Override
        public Drawable getBGDrawable(Rect boundingBox) {
            return null;
        }

        @Override
        public String getDisplayData() {
            return t.getAppointmentDisplayData();
        }

        @Override
        public View.OnClickListener getOnclickListener() {
            //TODO closing
            return null;
        }

        @Override
        public View.OnLongClickListener getOnLongClickListener() {
            //TODO change color
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


    private static class HourMarker extends Displayable {

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
            drawable.getPaint().setColor(Color.CYAN);
            drawable.setBounds(boundingBox);
            return drawable;
        }

        @Override
        public String getDisplayData() {
            return (hour < 12)? hour + " am" : (hour - 12) + " pm";
        }

        @Override
        public View.OnClickListener getOnclickListener() {
            //TODO new appointment
            return null;
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

    private void setupNewAppointmentState(){

    }
}
