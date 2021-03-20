package com.chrysanthemum.ui.dataView.task.accounting.Daily;

import android.graphics.Color;
import android.view.View;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.PaymentParser;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.display.LineDisplayLayoutTask;

public abstract class DailyAccountingTask extends LineDisplayLayoutTask {
    public DailyAccountingTask(TaskHostestActivity host) {
        super(host);
    }


    protected void displayTotal(String name, int amount, int tip, int colour, int row){
        String[] data = new String[] {
                "Total:",
                name,
                PaymentParser.reverseParse(amount, tip)
        };

        displayLine(data, colour, row, null);
    }

    protected void displayTransaction(Transaction transaction, int row, View.OnLongClickListener listener){
        String[] data = new String[] {
                TimeParser.parseDateDisplayDay(transaction.getLocalDateAppointmentDate()),
                TimeParser.reverseParse(transaction.getAppointmentTime()),
                PaymentParser.reverseParse(transaction.getAmount(), transaction.getTip()),
                transaction.getServices()
        };

        displayLine(data, transaction.getTech().getColour(), row, listener);
    }

    protected void displayLabel(){
        String[] data = new String[] {
                "Date", "Time", "Amount (Tip)", "Services"
        };

        displayLine(data, Color.GRAY, 0, null);
    }

}
