package com.chrysanthemum.ui.dataView.task.subTasks;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.NullRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionStatus;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.technicianLogin.TechnicianSelectorPanel;

/**
 * Allows technicians to claim open appointment as belonging to someone or as a no show
 */
public class AppointmentClaimTask  extends Task {

    private final Transaction transaction;
    private final NullRetriever retriever;

    public AppointmentClaimTask(TaskHostestActivity host, Transaction transaction, NullRetriever retriever) {
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Technician t = panel.getSelectedTech();

                if(transaction.getStatus() == TransactionStatus.Open
                        && t != null){
                    //claim
                    DataStorageModule.getFrontEnd().attachTransactionTech(transaction, t);
                } else if(transaction.getStatus() == TransactionStatus.Claimed
                        && t != null && transaction.getTech().getID() != t.getID()){
                    //change Tech
                    DataStorageModule.getFrontEnd().attachTransactionTech(transaction, t);
                } else {
                    noSHowAlert();
                }

                retriever.retrieved();
            }
        });
    }


    private void noSHowAlert(){
        host.createAlertBox()
                .setTitle("Mark as No show")
                .setMessage("Mark the appointment with " + transaction.getCustomer().getName()
                        + " as \"No Show\"")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DataStorageModule.getFrontEnd().markAsNoShow(transaction);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }


}
