package com.chrysanthemum.ui.dataView.task.subTasks;

import android.annotation.SuppressLint;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.appointments.NewAppointmentQuery;
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
            time.append("12:00 am");
        } else if(estimatedHour == 12){
            time.append("12:00 pm");
        } else if (estimatedHour < 12) {
            time.append(estimatedHour).append(":00 am");
        } else {
            time.append(estimatedHour - 12).append(":00 pm");
        }


        final TextView TimeLabel = host.createFormLabel(1);
        TimeLabel.setText("Appointment Time:");

        final EditText appTime = host.createEditableForm(1);
        appTime.setText(time.toString());


        final TextView minuteLabel = host.createFormLabel(2);
        minuteLabel.setText("Offset: A->15 B->30 C->45");

        final EditText offSet = host.createEditableForm(2);
        offSet.setHint("A");


        Button button = host.getFormButton();
        button.setText("Create");
        button.setOnClickListener(v -> {
            appointmentTime = TimeParser.parseTime(appTime.getText().toString().replace(":", " "));

            switch (offSet.getText().toString()) {
                case "A":
                case "a":
                    appointmentTime += 15;
                    break;
                case "B":
                case "b":
                    appointmentTime += 30;
                    break;
                case "C":
                case "c":
                    appointmentTime += 45;
                default:
                    break;
            }

            if(appointmentTime < 0){
                appTime.setError("Appointment Time Example: 8:30 am");
                return;
            }

            stage_2_AppointmentDetails();
        });
    }

    @SuppressLint("SetTextI18n")
    private void stage_2_AppointmentDetails() {
        host.clearForm();

        final TextView durationLabel = host.createFormLabel(1);
        durationLabel.setText("Appointment Duration:");

        final EditText duration = host.createEditableForm(1);
        duration.setText("");

        final TextView servicesLabel = host.createFormLabel(2);
        servicesLabel.setText("Services:");

        final EditText services = host.createEditableForm(2);
        services.setText("");
        services.setHint("Waxing, etc");

        Button button = host.getFormButton();
        button.setText("Create");
        button.setOnClickListener(v -> {

            String appointmentDurationText = duration.getText().toString().replace(":", " ");
            appointmentDuration = TimeParser.parseSimpleTime(appointmentDurationText);

            if(appointmentDuration < 0){

                if(appointmentDurationText.equalsIgnoreCase("a")){
                    appointmentDuration = 30;
                } else if(appointmentDurationText.equalsIgnoreCase("aa")){
                    appointmentDuration = 45;
                } else if(appointmentDurationText.equalsIgnoreCase("b")){
                    appointmentDuration = 60;
                } else if(appointmentDurationText.equalsIgnoreCase("bb")){
                    appointmentDuration = 75;
                } else if(appointmentDurationText.equalsIgnoreCase("c")){
                    appointmentDuration = 90;
                } else if(appointmentDurationText.equalsIgnoreCase("cc")){
                    appointmentDuration = 105;
                } else if(appointmentDurationText.equalsIgnoreCase("d")){
                    appointmentDuration = 120;
                } else {
                    duration.setError("Appointment Duration Example: 1:30");
                }
            }

            service = services.getText().toString();
            stage_3_FindCustomer();
        });
    }

    private void stage_3_FindCustomer() {
        CustomerFinderTask t = new CustomerFinderTask(host, c -> {
            customer = c;
            stage_4_CreateAppointment();
        });

        t.start();
    }

    private void stage_4_CreateAppointment(){
        Transaction transaction = new NewAppointmentQuery(selectedDate, appointmentTime,
                appointmentDuration, customer, service).executeQuery();

        purpose.retrievedData(transaction);
    }
}
