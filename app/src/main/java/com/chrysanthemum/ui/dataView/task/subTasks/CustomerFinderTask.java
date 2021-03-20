package com.chrysanthemum.ui.dataView.task.subTasks;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.parsing.PhoneNumberParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.Query;
import com.chrysanthemum.appdata.querries.customers.CustomerByPhoneQuery;
import com.chrysanthemum.appdata.querries.customers.NewCustomerQuery;
import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.dataView.display.Displayable;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;

import java.util.Map;

public class CustomerFinderTask extends Task {

    public static final int STANDARD_BOX_WIDTH = 210;
    public static final int STANDARD_BOX_HEIGHT = 66;

    private static Customer lastCustomer = null;

    private DisplayBoard board;
    private Map<String, Customer> customerMap;

    private long selectedPhoneNumber;
    private final DataRetriever<Customer> purpose;

    private EditText form;
    private TextView label;

    public CustomerFinderTask(TaskHostestActivity host, DataRetriever<Customer> purpose) {
        super(host);
        this.purpose = purpose;
    }

    public void start(){
        setupFindState();
    }

    @SuppressLint("SetTextI18n")
    private void setupFindState() {
        host.clearForm();
        host.setBarText(host.getMainTaskTitle());

        form = host.createEditableForm(1);
        label = host.createFormLabel(1);

        label.setText("Phone Number:");

        form.setText("");
        form.setHint("1-902-999-2703 or leave blank for last customer searched");

        Button b = host.getFormButton();
        b.setText("Search");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCustomer();
            }
        });
    }

    private void searchCustomer(){

        String searchQuery = form.getText().toString();

        if(searchQuery.equals("")){
            chooseLastCustomer();
            return;
        }

        long phoneNumber = PhoneNumberParser.parse(searchQuery);
        if (phoneNumber < 0) {
            form.setError("Example: 1-902-999-2703");
        } else {
            selectedPhoneNumber = phoneNumber;
            loadBoard();
        }
    }

    private void chooseLastCustomer(){
        if(lastCustomer == null){
            form.setError("We haven't serviced a Customer yet today");
        } else {
            host.createAlertBox()
                    .setTitle("Last Customer Serviced was:")
                    .setMessage(lastCustomer.getName() + "\n" + lastCustomer.getPhoneNumber())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            purpose.retrievedData(lastCustomer);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    private void loadBoard(){
        board = host.getBoard();
        board.clear(host.getScale());

        DataRetriever<Map<String, Customer>> retriever = new DataRetriever<Map<String, Customer>>() {
            @Override
            public void retrievedData(Map<String, Customer> data) {
                customerMap = data;
                updateBoardDisplay();
            }
        };

        Query<Map<String, Customer>> query = new CustomerByPhoneQuery(selectedPhoneNumber, retriever);
        query.executeQuery();
    }

    private void updateBoardDisplay(){
        int index = 0;

        for(String customerName : customerMap.keySet()){
            final Customer c = customerMap.get(customerName);

            board.displayData(new CustomerDisplay(c,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lastCustomer = c;
                            purpose.retrievedData(c);
                        }
                    },
                    null,
                    (index % 4) * (STANDARD_BOX_WIDTH + 1),
                    (index / 4) * (STANDARD_BOX_HEIGHT + 1)
            ));

            index++;
        }

        board.displayData(new CustomerDisplay(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setupNewCustomerState();
                    }
                },
                (index % 4) * (STANDARD_BOX_WIDTH + 5),
                (index / 4) * (STANDARD_BOX_HEIGHT + 5)
        ));

    }

    @SuppressLint("SetTextI18n")
    private void setupNewCustomerState(){
        host.setBarText(host.getMainTaskTitle() + " >>> New Customer");

        label.setText("Customer Name:");

        form.setText("");
        form.setHint("John Doe");

        Button b = host.getFormButton();
        b.setText("Create");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCustomer();
            }
        });
    }

    private void createCustomer(){
        String name = form.getText().toString();
        Customer customer;

        if(customerMap.containsKey(name)){
            customer = customerMap.get(name);
        } else {
            customer = new NewCustomerQuery(name, selectedPhoneNumber).executeQuery();
        }

        lastCustomer = customer;
        purpose.retrievedData(customer);
    }


    private class CustomerDisplay extends Displayable {

        private final String data;
        private final View.OnClickListener onclick;
        private final View.OnLongClickListener onLongClick;
        private final int colour;
        private final int x;
        private final int y;

        public CustomerDisplay(Customer c, View.OnClickListener listener, View.OnLongClickListener onLongClick, int x, int y){
            onclick = listener;
            this.onLongClick = onLongClick;
            data = "  " +
                    c.getName() +
                    "\n" +
                    host.formatPhoneNumber(c.getPhoneNumber());
            colour = Color.CYAN;
            this.x = x;
            this.y = y;
        }

        public CustomerDisplay(View.OnClickListener listener, int x, int y) {
            onclick = listener;
            onLongClick = null;
            data = "  New Customer";
            colour = Color.LTGRAY;
            this.x = x;
            this.y = y;
        }

        @Nullable
        public Drawable getBGDrawable(Rect boundingBox) {
            ShapeDrawable drawable =  new ShapeDrawable(new RectShape());
            drawable.getPaint().setColor(colour);
            drawable.setBounds(boundingBox);
            return drawable;
        }

        public String getDisplayData() {
            return data;
        }

        public View.OnClickListener getOnclickListener() {
            return onclick;
        }

        public View.OnLongClickListener getOnLongClickListener() {
            return onLongClick;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public int getWidth() {
            return STANDARD_BOX_WIDTH;
        }

        @Override
        public int getHeight() {
            return STANDARD_BOX_HEIGHT;
        }

    }
}
