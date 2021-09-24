package com.chrysanthemum.ui.dataView.task.subTasks;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.MoneyParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.appointments.CloseAppointmentQuery;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;

public class RecordPaymentTask extends Task {

    private final DataRetriever<Transaction> retriever;
    private final Transaction transaction;

    public RecordPaymentTask(TaskHostestActivity host, Transaction transaction, DataRetriever<Transaction> retriever) {
        super(host);

        this.retriever = retriever;
        this.transaction = transaction;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void start() {
        host.clearForm();

        final TextView paymentLabel = host.createFormLabel(1);
        final EditText payment = host.createEditableForm(1);

        paymentLabel.setText("Amount (Tip):");
        payment.setHint(MoneyParser.reverseParse(5750, 500));

        final TextView serviceLabel = host.createFormLabel(2);
        final EditText service = host.createEditableForm(2);

        serviceLabel.setText("Services:");
        service.setText(transaction.getServices());


        Button button = host.getFormButton();
        button.setText("Close Transaction");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = payment.getText().toString();

                money = money.replaceAll("\\(" , " ");
                money = money.replaceAll("[$).]", "");
                money = money.replaceAll("[ ]+", " ");

                int[] pay = MoneyParser.parsePayment(money);

                if(pay == null){
                    payment.setError("example: $57.50 ($2.00)");
                    return;
                }

                new CloseAppointmentQuery(transaction, pay[0], pay[1], service.getText().toString()).executeQuery();

                CustomerFinderTask.service(transaction.getCustomer());

                retriever.retrievedData(transaction);
            }
        });
    }
}
