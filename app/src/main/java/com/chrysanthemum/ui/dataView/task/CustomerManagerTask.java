package com.chrysanthemum.ui.dataView.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.PhoneNumberParser;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.Colour;
import com.chrysanthemum.appdata.querries.Query;
import com.chrysanthemum.appdata.querries.customers.UpdateCustomerInfoQuery;
import com.chrysanthemum.appdata.querries.transactions.TransactionsByCustomerIDQuery;
import com.chrysanthemum.ui.dataView.display.Displayable;
import com.chrysanthemum.ui.dataView.task.display.PhoneNumberBar;
import com.chrysanthemum.ui.dataView.task.subTasks.CustomerFinderTask;

import java.util.LinkedList;

public class CustomerManagerTask extends Task {

    private static final int SLOT_HEIGHT = 100;
    private static final int SLOT_WIDTH = 60;
    private static final int MARGIN = 2;
    private static final int[] BOX_SLOT = new int[] {0, 3,  4,  6,  8, 12};

    public CustomerManagerTask(TaskHostestActivity host) {
        super(host);
    }

    @Override
    public void start() {
        host.getBoard().clear(host.getScale());

        CustomerFinderTask t = new CustomerFinderTask(host, customer -> {
            setupCustomerInfoAdjustmentForm(customer);
            loadTransactions(customer);
        });

        t.start();
    }

    @SuppressLint("SetTextI18n")
    public void setupCustomerInfoAdjustmentForm(final Customer customer) {
        host.setBarText(host.getMainTaskTitle());
        host.clearForm();

        TextView label1 = host.createFormLabel(1);
        EditText phone = host.createEditableForm(1);
        final PhoneNumberBar phoneBar = new PhoneNumberBar(phone);

        label1.setText("Phone:");
        phone.setText(PhoneNumberParser.revParse(customer.getPhoneNumber()));

        TextView label2 = host.createFormLabel(2);
        final EditText name = host.createEditableForm(2);

        label2.setText("Customer Name:");
        name.setText(customer.getName());

        Button b = host.getFormButton();
        b.setText("Update Customer Info");
        b.setOnClickListener(v -> {
            if (phoneBar.hasPhoneNumber()) {
                new UpdateCustomerInfoQuery(customer, name.getText().toString(), phoneBar.getPhoneNumber()).executeQuery();
                host.popMessage("Updated Customer Info!");
                setupCustomerInfoAdjustmentForm(customer);
            }
        });
    }

    private void loadTransactions(Customer customer){
        host.getBoard().clear(host.getScale());

        DataRetriever<LinkedList<Transaction>> retriever = data -> {

            // sort data by appointment time then by id
            data.sort((o1, o2) -> {
                long comp = o2.getLocalDateAppointmentDate().compareTo(o1.getLocalDateAppointmentDate());

                if(comp == 0){
                    comp = o1.getID() - o2.getID();
                }

                return (comp == 0)? 0 : (int) (comp / Math.abs(comp));
            });

            // display each transaction in a row
            int row = 0;

            for(Transaction transaction : data){
                displayTransaction(row++, transaction);
            }
        };

        Query<LinkedList<Transaction>> query = new TransactionsByCustomerIDQuery(customer, retriever);
        query.executeQuery();
    }

    private void displayTransaction(int row, Transaction transaction){
        String[] boxData = new String[] {
                transaction.getDate().replaceAll(" ", "/"),
                TimeParser.reverseParse(transaction.getAppointmentTime()),
                (transaction.getTech() == null)? "" : transaction.getTech().getName(),
                transaction.transactionAmountStatusDisplay(),
                transaction.getServices()
        };

        for (int box  = 0; box < boxData.length; box ++){
            int colour = getBoxColour(box, transaction);

            displayBox(getBox(row, box), colour, boxData[box]);
        }
    }

    private int getBoxColour(int box, Transaction transaction){
        if(box == 2 && transaction.getTech() != null){
            return transaction.getTech().getColour();
        }

        if(box == 3){
            switch (transaction.getTransactionStatus()){
                case Open:
                    return Color.YELLOW;
                case Noshow:
                    return Color.RED;
                case Closed:
                    return Color.GREEN;
            }
        }

        return Color.LTGRAY;
    }

    private Rect getBox(int row, int box){
        int left = BOX_SLOT[box] * (SLOT_WIDTH + MARGIN);
        int top = row * (SLOT_HEIGHT + MARGIN);
        int right = BOX_SLOT[box + 1] * (SLOT_WIDTH + MARGIN) - MARGIN;
        int bot = top + SLOT_HEIGHT;

        return new Rect(left, top, right, bot);
    }

    private void displayBox(final Rect box, final int colour, final String data){
        host.getBoard().displayData(new Displayable() {

            @Override
            public Drawable getBGDrawable(Rect boundingBox) {
                ShapeDrawable drawable =  new ShapeDrawable(new RectShape());
                drawable.getPaint().setColor(colour);
                drawable.setBounds(boundingBox);
                return drawable;
            }

            @Override
            public Colour getBGColour() {
                return new Colour(colour);
            }

            @Override
            public String getDisplayData() {
                return data;
            }

            @Override
            public View.OnClickListener getOnclickListener() {
                return null;
            }

            @Override
            public View.OnLongClickListener getOnLongClickListener() {
                return null;
            }

            @Override
            public int getX() {
                return box.left;
            }

            @Override
            public int getY() {
                return box.top;
            }

            @Override
            public int getWidth() {
                return box.width();
            }

            @Override
            public int getHeight() {
                return box.height();
            }
        });
    }


    public static TaskSelectionButtion getMenuButton(Context context, final TaskHostestActivity host){
        return new TaskSelectionButtion(context){
            @Override
            public Task getTask() {
                return new CustomerManagerTask(host);
            }

            @Override
            public String getTaskName() {
                return "Customer Finder";
            }
        };
    }
}
