package com.chrysanthemum.ui.dataView.task.accounting.Monthly;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chrysanthemum.appdata.dataType.MonthTally;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.accounting.read.ReadMonthTally;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.TaskSelectionButtion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

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

            if(y < today.getYear() - 2 || y > today.getYear() || (y == today.getYear() && m > today.getMonthValue())){
                try {
                    generateFile(new MonthTally(title));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                DBReadQuery<MonthTally> query = new ReadMonthTally(title, y, m);
                MonthTally mt = query.execute();

                if(mt == null){
                    host.popMessage("Loading Month Tally Timed Out!");
                    return;
                }

                try {
                    generateFile(mt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    private void generateFile(MonthTally data) throws IOException {

        String fileData = data.generateFileData();

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File f = new File(path, data.getTitle() + ".csv");

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
