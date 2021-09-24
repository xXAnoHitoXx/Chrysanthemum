package com.chrysanthemum.ui.dataView.task;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.Util.Scaler;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.parsing.MoneyParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.transactions.NewSaleTransactionQuery;
import com.chrysanthemum.ui.dataView.task.subTasks.CustomerFinderTask;
import com.chrysanthemum.ui.dataView.task.subTasks.DaySelectorTask;

import java.time.LocalDate;

public class SaleTask extends Task {

    private Customer customer;
    private LocalDate date;

    public SaleTask(TaskHostestActivity host) {
        super(host);
    }

    @Override
    public void start() {
        Task customerSelect = new CustomerFinderTask(host, new DataRetriever<Customer>() {
            @Override
            public void retrievedData(Customer data) {
                customer = data;
                taskSetup();
            }
        });
        customerSelect.start();
    }

    private void taskSetup(){
        if(DataStorageModule.getFrontEnd().getSecurityModule().getLoggedinTech().getRole().equalsIgnoreCase("admin")){
            DaySelectorTask t = new DaySelectorTask(host, new DataRetriever<LocalDate>() {
                @Override
                public void retrievedData(LocalDate data) {
                    date = data;
                    saleForm();
                }
            });
            t.start();
        } else {
            date = LocalDate.now();
            saleForm();
        }
    }

    private void saleForm(){
        final Customer customer = this.customer;

        host.getBoard().clear(new Scaler(1));
        host.clearForm();

        TextView amtLbl = host.createFormLabel(1);
        final EditText amount = host.createEditableForm(1);

        amtLbl.setText("Total Amount Before Tax:");
        amount.setText("$0.00");

        TextView descLbl = host.createFormLabel(2);
        final EditText desc = host.createEditableForm(2);

        descLbl.setText("Items Descriptions:");

        Button button = host.getFormButton();
        button.setText("Finish!");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int amt = MoneyParser.parseSingleAmount(amount.getText().toString());
                if(amt == Integer.MIN_VALUE){
                    amount.setError("Amount Isn't Recognizable!");
                    return;
                }

                NewSaleTransactionQuery q = new NewSaleTransactionQuery(date, customer, desc.getText().toString(), amt);
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
