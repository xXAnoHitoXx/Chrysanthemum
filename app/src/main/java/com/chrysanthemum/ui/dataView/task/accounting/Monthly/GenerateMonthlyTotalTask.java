package com.chrysanthemum.ui.dataView.task.accounting.Monthly;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.MonthTally;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.accounting.LoadMonthTallyQuery;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.TaskSelectionButtion;
import com.chrysanthemum.ui.dataView.task.accounting.Daily.DailyAccountingTask_Admin;
import com.chrysanthemum.ui.dataView.task.display.LineDisplayLayoutTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Map;

/***
 * admin task
 */
public class GenerateMonthlyTotalTask extends Task {

    Context context;

    public GenerateMonthlyTotalTask(TaskHostestActivity host, Context context) {
        super(host);
        this.context = context;
    }

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
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                int y = TimeParser.parseYear(year.getText().toString());
                int m = TimeParser.parseMonth(month.getText().toString());

                String title = m + "_" + y + "_Report";

                if(y < today.getYear() - 2 || y > today.getYear() || (y == today.getYear() && m > today.getMonthValue())){
                    try {
                        generateFile(new MonthTally(title));
                    } catch (FileNotFoundException e) {
                        //TODO
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    LoadMonthTallyQuery q = new LoadMonthTallyQuery(title, y, m, new DataRetriever<MonthTally>() {
                        @RequiresApi(api = Build.VERSION_CODES.Q)
                        @Override
                        public void retrievedData(MonthTally data) {
                            try {
                                generateFile(data);
                            } catch (FileNotFoundException e) {
                                //TODO
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    q.executeQuery();
                }
            }
        });
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


    public static TaskSelectionButtion getMenuButton(Context context, final TaskHostestActivity host) {
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
