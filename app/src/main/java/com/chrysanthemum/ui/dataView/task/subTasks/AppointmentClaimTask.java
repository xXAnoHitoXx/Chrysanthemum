package com.chrysanthemum.ui.dataView.task.subTasks;

import android.annotation.SuppressLint;
import android.widget.Button;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.AppointmentStatus;
import com.chrysanthemum.appdata.dataType.subType.TransactionStatus;
import com.chrysanthemum.appdata.querries.DBDeleteQuery;
import com.chrysanthemum.appdata.querries.appointment.update.AssignTechnicianToAppointment;
import com.chrysanthemum.appdata.querries.appointment.update.DeleteNoShowAppointment;
import com.chrysanthemum.appdata.querries.appointment.update.MarkAppointmentAsNoShow;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.technicianLogin.TechnicianSelectorPanel;

/**
 * Allows technicians to claim open appointment as belonging to someone or as a no show
 */
public class AppointmentClaimTask  extends Task {

    public static final Boolean DELETED = true;

    private final Transaction transaction;
    private final DataRetriever<Boolean> retriever;

    public AppointmentClaimTask(TaskHostestActivity host, Transaction transaction, DataRetriever<Boolean> retriever) {
        super(host);
        this.transaction = transaction;
        this.retriever = retriever;
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void start() {
        host.clearForm();

        final TechnicianSelectorPanel panel = host.createTechPanel();

        Button button = host.getFormButton();
        button.setText("Select");
        button.setOnClickListener(v -> {
            Technician t = panel.getSelectedTech();

            if(t == null){
                if(transaction.getTransactionStatus() == TransactionStatus.Noshow){
                    removeAppointmentAlert();
                } else {
                    noShowAlert();
                }
            } else if(transaction.getAppointmentStatus() == AppointmentStatus.Open) {
                new AssignTechnicianToAppointment(transaction, t).execute();
            } else if(transaction.getAppointmentStatus() == AppointmentStatus.Claimed){

                if(t.getID() == transaction.getTech().getID()){
                    // deselect
                    new AssignTechnicianToAppointment(transaction, null).execute();
                } else {
                    //change Tech
                    new AssignTechnicianToAppointment(transaction, t).execute();
                }
            }

            retriever.retrievedData(!DELETED);
        });
    }

    private void noShowAlert(){
        host.createAlertBox()
                .setTitle("Mark as No show")
                .setMessage("Mark the appointment with " + transaction.getCustomer().getName()
                        + " as \"No Show\"")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> new MarkAppointmentAsNoShow(transaction).execute())
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void removeAppointmentAlert(){
        host.createAlertBox()
                .setTitle("Delete this Appointment")
                .setMessage("Delete the appointment with " + transaction.getCustomer().getName())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    DBDeleteQuery<Transaction> query = new DeleteNoShowAppointment(transaction);
                    query.deleteData();
                    CustomerFinderTask.service(transaction.getCustomer());
                    retriever.retrievedData(DELETED);
                })
                .setNegativeButton(android.R.string.no, null).show();

    }
}
