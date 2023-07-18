package com.chrysanthemum.ui.dataView.task.subTasks;

import android.annotation.SuppressLint;
import android.widget.Button;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.AppointmentStatus;
import com.chrysanthemum.appdata.dataType.subType.TransactionStatus;
import com.chrysanthemum.appdata.querries._old.appointments.AttachTechnicianToAppointmentQuery;
import com.chrysanthemum.appdata.querries._old.appointments.DeleteNoshowAppointmentQuery;
import com.chrysanthemum.appdata.querries._old.appointments.MarkAppointmentAsNoShowQuery;
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
                new AttachTechnicianToAppointmentQuery(transaction, t).executeQuery();
            } else if(transaction.getAppointmentStatus() == AppointmentStatus.Claimed){

                if(t.getID() == transaction.getTech().getID()){
                    // deselect
                    new AttachTechnicianToAppointmentQuery(transaction, null).executeQuery();
                } else {
                    //change Tech
                    new AttachTechnicianToAppointmentQuery(transaction, t).executeQuery();
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
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> new MarkAppointmentAsNoShowQuery(transaction).executeQuery())
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void removeAppointmentAlert(){
        host.createAlertBox()
                .setTitle("Delete this Appointment")
                .setMessage("Delete the appointment with " + transaction.getCustomer().getName())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    new DeleteNoshowAppointmentQuery(transaction).executeQuery();
                    CustomerFinderTask.service(transaction.getCustomer());
                    retriever.retrievedData(DELETED);
                })
                .setNegativeButton(android.R.string.no, null).show();

    }
}
