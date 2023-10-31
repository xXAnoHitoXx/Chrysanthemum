package com.chrysanthemum.ui.dataView.task.accounting.Monthly;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chrysanthemum.appdata.Util.Scaler;
import com.chrysanthemum.appdata.dataType.MonthTally;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.MoneyParser;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.subType.AppointmentStatus;
import com.chrysanthemum.appdata.dataType.subType.Colour;
import com.chrysanthemum.appdata.querries.accounting.LoadMonthTallyQuery;
import com.chrysanthemum.ui.dataView.display.Displayable;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.TaskSelectionButtion;
import com.chrysanthemum.ui.dataView.task.subTasks.AppointmentClaimTask;
import com.chrysanthemum.ui.dataView.task.subTasks.UpdateAppointmentTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

import static androidx.core.app.ActivityCompat.requestPermissions;

/***
 * admin task
 */
public class GenerateMonthlyTotalTask extends Task {

    Activity context;

    public GenerateMonthlyTotalTask(TaskHostestActivity host, Activity context) {
        super(host);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void start() {
        LocalDate today = LocalDate.now();
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
        button.setText("Select Month");
        button.setOnClickListener(v -> {
            int y = TimeParser.parseYear(year.getText().toString());
            int m = TimeParser.parseMonth(month.getText().toString());

            String title = m + "_" + y + "_Report";

            // if looking for an out of range month, don't query DB
            if(y < 2021 || y > today.getYear() || (y == today.getYear() && m > today.getMonthValue())){
                try {
                    generateFile(title, "invalid month\n");
                } catch (IOException e) {
                    //TODO
                    e.printStackTrace();
                }
            } else {
                LoadMonthTallyQuery q = new LoadMonthTallyQuery(title, y, m, data -> {
                    try {
                        String fileData = data.generateFileData();
                        generateFile(title, fileData);
                        displayTally(fileData);
                    } catch (IOException e) {
                        //TODO
                        e.printStackTrace();
                    }
                });

                q.executeQuery();
            }
        });

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {

            requestPermissions(context,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    1);

        }
    }

    private void displayTally(String data){
        host.getBoard().clear(new Scaler(1));
        Scanner scanner = new Scanner(data);

        int row = 0;

        while(scanner.hasNext()){
            String line = scanner.nextLine();
            String lineData = "";
            Scanner lineScanner = new Scanner(line);

            while(lineScanner.hasNext()){
                String word = lineScanner.next();
                lineData += String.format("%1$12s", word.substring(0, word.length() - 1));
            }

            host.getBoard().displayData(new DataDisplay(row++, 0, lineData));
        }

    }

    private static class DataDisplay extends Displayable {

        private static final int SLOT_HEIGHT = 20;
        private static final int SLOT_WIDTH = 4000;
        int rowNum; int colNum;

        private final String data;
        private final int colour = 0xFF8C8C8C;

        public DataDisplay(int row, int col, String data){
            this.data = data;
            this.rowNum = row;
            this.colNum = col;
        }

        @Nullable
        @Override
        public Drawable getBGDrawable(Rect boundingBox) {
            ShapeDrawable drawable =  new ShapeDrawable(new RectShape());
            drawable.getPaint().setColor(colour);
            drawable.setBounds(boundingBox);
            return drawable;
        }

        @Override
        public Colour getBGColour() {
            return new Colour(colour);
        }

        @Override
        public String getDisplayData() {
            return data;
        }

        @Override
        public View.OnClickListener getOnclickListener() {
            return null;
        }

        @Override
        public View.OnLongClickListener getOnLongClickListener() {
            return null;
        }

        @Override
        public int getX() { return colNum * (SLOT_WIDTH + 2); }

        @Override
        public int getY() { return rowNum * (SLOT_HEIGHT + 2); }

        @Override
        public int getWidth() { return SLOT_WIDTH; }

        @Override
        public int getHeight() {
            return SLOT_HEIGHT;
        }
    }


    private void generateFile(String title, String fileData) throws IOException {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File f = new File(path, title + ".csv");

        FileOutputStream o = new FileOutputStream(f);
        /*
        ContentResolver resolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, data.getTitle());
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);

        Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);

        OutputStream o = resolver.openOutputStream(uri);

         */
        PrintWriter writer = new PrintWriter(o);
        writer.println(fileData);
        writer.flush();

        host.popMessage("Generated Report File");
    }


    public static TaskSelectionButtion getMenuButton(Activity context, final TaskHostestActivity host) {
        return new TaskSelectionButtion(context) {
            @Override
            public Task getTask() {
                return new GenerateMonthlyTotalTask(host, context);
            }

            @Override
            public String getTaskName() {
                return "Generate Month Report";
            }
        };
    }
}
