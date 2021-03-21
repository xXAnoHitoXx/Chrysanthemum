package com.chrysanthemum.ui.dataView.task;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.Util.Scaler;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.PaymentParser;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.Query;
import com.chrysanthemum.appdata.querries.appointments.NewAppointmentQuery;
import com.chrysanthemum.appdata.querries.transactions.NewSaleTransactionQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.ui.dataView.task.accounting.Daily.DailyAccountingTask_Admin;
import com.chrysanthemum.ui.dataView.task.subTasks.CustomerFinderTask;
import com.chrysanthemum.ui.dataView.task.subTasks.NewAppointmentTask;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SaleTask extends Task {

    public SaleTask(TaskHostestActivity host) {
        super(host);
    }

    @Override
    public void start() {
        Task customerSelect = new CustomerFinderTask(host, new DataRetriever<Customer>() {
            @Override
            public void retrievedData(Customer data) {
                saleForm(data);
            }
        });
        customerSelect.start();
    }

    private void saleForm(final Customer customer){
        host.getBoard().clear(new Scaler(1));
        host.clearForm();

        TextView amtLbl = host.createFormLabel(1);
        final EditText amount = host.createEditableForm(1);

        amtLbl.setText("Total Amount After Tax:");
        amount.setText("$0.00");

        TextView descLbl = host.createFormLabel(2);
        final EditText desc = host.createEditableForm(2);

        descLbl.setText("Items Descriptions:");

        Button button = host.getFormButton();
        button.setText("Finish!");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int amt = PaymentParser.parseSingleAmount(amount.getText().toString().replaceAll("[$.]", ""));
                if(amt == Integer.MIN_VALUE){
                    amount.setError("Amount Isn't Recognizable!");
                    return;
                }

                NewSaleTransactionQuery q = new NewSaleTransactionQuery(customer, desc.getText().toString(), amt);
                q.executeQuery();

                host.popMessage("Sale Record Created!");

                host.getBoard().clear(new Scaler(1));
                host.clearForm();

                start();
            }
        });

    }


    public static TaskSelectionButtion getMenuButton(Context context, final TaskHostestActivity host) {
        return new TaskSelectionButtion(context) {
            @Override
            public Task getTask() {
                return new SaleTask(host);
            }

            @Override
            public String getTaskName() {
                return "Sales";
            }
        };
    }
}
