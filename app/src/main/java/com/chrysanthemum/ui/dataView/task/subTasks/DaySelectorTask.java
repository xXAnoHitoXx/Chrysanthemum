package com.chrysanthemum.ui.dataView.task.subTasks;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.chrysanthemum.R;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.dataView.display.Displayable;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class DaySelectorTask extends Task {

    private static final int SLOT_WIDTH = 140;
    private static final int SLOT_HEIGHT = 90;
    private static final int MARGIN = 2;
    private static final int OFFSET_PERCENT = 7;

    private final DataRetriever<LocalDate> retriever;
    private DayDisplay selected;

    public DaySelectorTask(TaskHostestActivity host, DataRetriever<LocalDate> retriever) {
        super(host);
        this.retriever = retriever;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void start() {
        LocalDate today = LocalDate.now();
        selected = null;

        host.clearForm();

        TextView label1 = host.createFormLabel(1);
        label1.setText("Year:");

        final EditText year = host.createEditableForm(1);
        year.setText(today.getYear() + "");

        TextView label2 = host.createFormLabel(2);
        label2.setText("Month:");

        final EditText month = host.createEditableForm(2);
        month.setText(today.getMonthValue() + "");

        Button button = host.getFormButton();
        button.setText("Search");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int y = TimeParser.parseYear(year.getText().toString());
                int m = TimeParser.parseMonth(month.getText().toString());

                if(y < 0 || m < 0){
                    return;
                }

                loadBoard(y, m);
            }
        });
    }

    public void loadBoard(int year, int month) {
        DisplayBoard board = host.getBoard();
        board.clear();

        LocalDate monthStart = LocalDate.of(year, month, 1);
        int daysInMonth = monthStart.lengthOfMonth();

        for(int day = 1; day <= daysInMonth; day++){
            LocalDate date = LocalDate.of(year, month, day);

            board.displayData(new DayDisplay(date));
        }
    }

    private class DayDisplay extends Displayable {

        private final LocalDate day;

        public DayDisplay(LocalDate day){
            this.day = day;

        }

        @Nullable
        @Override
        public Drawable getBGDrawable(Rect boundingBox) {
            ShapeDrawable bgDrawable =  new ShapeDrawable(new RectShape());

            bgDrawable.getPaint().setColor(Color.LTGRAY);
            if(selected == this){
                bgDrawable.setBounds(shrink(boundingBox));
            } else {
                bgDrawable.setBounds(boundingBox);
                return bgDrawable;
            }

            Drawable checkMark = ResourcesCompat.getDrawable(host.getResources(), R.drawable.checkmark, null);

            if(checkMark == null){
                return bgDrawable;
            }

            checkMark.setBounds(boundingBox.width() - checkMark.getIntrinsicWidth(),
                    boundingBox.height() - checkMark.getIntrinsicHeight(),
                    boundingBox.width(), boundingBox.height());

            return new LayerDrawable(new Drawable[]{
                    bgDrawable,
                    checkMark
            });
        }

        @Override
        public String getDisplayData() {
            StringBuilder builder = new StringBuilder();

            builder.append(day.getDayOfMonth());

            if(day.getDayOfMonth() <= 7){
                builder.append(" (").append(day.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.CANADA)).append(")");
            }

            return builder.toString();
        }

        @Override
        public View.OnClickListener getOnclickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeSelected();
                }
            };
        }

        @Override
        public View.OnLongClickListener getOnLongClickListener() {
            return null;
        }

        @Override
        public int getX() {
            return (day.getDayOfWeek().getValue() % 7) * (SLOT_WIDTH + MARGIN);
        }

        @Override
        public int getY() {
            LocalDate first = day.withDayOfMonth(1);
            int firstOfMonthsWeekday = first.getDayOfWeek().getValue() % 7;
            int row = (day.getDayOfMonth() - 1 + firstOfMonthsWeekday) / 7;

            return row * (SLOT_HEIGHT + MARGIN);
        }

        @Override
        public int getWidth() {
            return SLOT_WIDTH;
        }

        @Override
        public int getHeight() {
            return SLOT_HEIGHT;
        }

        private void makeSelected(){
            if(selected == this){
                retriever.retrievedData(day);
            } else {
                DayDisplay prevSelect = selected;
                selected = this;

                if(prevSelect != null){
                    host.getBoard().invalidateData(prevSelect);
                }

                host.getBoard().invalidateData(this);
            }
        }

        private Rect shrink(Rect boundingBox){
            int xMargin = (boundingBox.width() * OFFSET_PERCENT) / 100;
            int yMargin = (boundingBox.height() * OFFSET_PERCENT) / 100;

            return new Rect(xMargin, yMargin,
                    boundingBox.width() - xMargin, boundingBox.height() - yMargin);
        }
    }

}
