package com.chrysanthemum.ui.dataView.task.subTasks;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.Util.AppUtil;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;

public class NewAppointmentTask extends Task {

    private final DataRetriever<Transaction> purpose;
    private final int estimatedHour;

    private final String selectedDate;

    private int appointmentTime;
    private int appointmentDuration;
    private String service;
    private Customer customer;

    public NewAppointmentTask(TaskHostestActivity host, int estimatedHour, String selectedDate, DataRetriever<Transaction> purpose) {
        super(host);
        this.purpose = purpose;
        this.estimatedHour = estimatedHour;
        this.selectedDate = selectedDate;
    }

    @Override
    public void start() {
        stage_1_AppointmentTime();
    }

    @SuppressLint("SetTextI18n")
    private void stage_1_AppointmentTime() {

        host.setBarText(host.getMainTaskTitle() + " >>> New Appointment");
        host.clearForm();

        StringBuilder time = new StringBuilder();
        if(estimatedHour == 0){
            time.append("12:0 am");
        } else if(estimatedHour == 12){
            time.append("12:0 pm");
        } else if (estimatedHour < 12) {
            time.append(estimatedHour).append(":0 am");
        } else {
            time.append(estimatedHour - 12).append(":0 pm");
        }


        final TextView dateLabel = host.createFormLabel(1);
        dateLabel.setText("Appointment Time:");

        final EditText appTime = host.createEditableForm(1);
        appTime.setText(time.toString());

        Button button = host.getFormButton();
        button.setText("Create");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentTime = TimeParser.parseTime(appTime.getText().toString().replace(":", " "));

                if(appointmentTime < 0){
                    appTime.setError("Appointment Time Example: 8:30 am");
                    return;
                }

                stage_2_AppointmentDetails();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void stage_2_AppointmentDetails() {
        host.clearForm();

        final TextView durationLabel = host.createFormLabel(1);
        durationLabel.setText("Appointment Duration:");

        final EditText duration = host.createEditableForm(1);
        duration.setText("0:30");

        final TextView servicesLabel = host.createFormLabel(2);
        servicesLabel.setText("Services:");

        final EditText services = host.createEditableForm(2);
        services.setText("");
        services.setHint("Waxing, etc");

        Button button = host.getFormButton();
        button.setText("Create");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appointmentDuration = TimeParser.parseSimpleTime(duration.getText().toString().replace(":", " "));

                if(appointmentDuration < 0){
                    duration.setError("Appointment Duration Example: 1:30");
                    return;
                }

                service = services.getText().toString();
                stage_3_FindCustomer();
            }
        });
    }

    private void stage_3_FindCustomer() {
        CustomerFinderTask t = new CustomerFinderTask(host, new DataRetriever<Customer>() {
            @Override
            public void retrievedData(Customer c) {
                customer = c;
                stage_4_CreateAppointment();
            }
        });

        t.start();
    }

    private void stage_4_CreateAppointment(){
        Transaction transaction =
                DataStorageModule.getFrontEnd().createAppointment(selectedDate, appointmentTime,
                        appointmentDuration, customer, service);


        purpose.retrievedData(transaction);
    }
}
