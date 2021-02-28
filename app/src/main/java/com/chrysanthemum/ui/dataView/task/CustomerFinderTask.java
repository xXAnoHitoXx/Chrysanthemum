package com.chrysanthemum.ui.dataView.task;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.ui.dataView.display.DataDisplay;
import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.dataView.display.Displayable;
import com.chrysanthemum.ui.dataView.task.taskListener.CustomerFoundListener;

import java.util.Map;

public class CustomerFinderTask extends Task {

    private DisplayBoard board;
    private Map<String, Customer> customerMap;

    private long selectedPhoneNumber;
    private final CustomerFoundListener purpose;

    private EditText form;

    public CustomerFinderTask(TaskHostestActivity host, CustomerFoundListener purpose) {
        super(host);
        this.purpose = purpose;
    }

    public void start(){
        setupFindState();
    }

    private void setupFindState() {
        host.clearForm();
        host.setBarText(host.getTaskTitle());

        form = host.createEditableForm(1);

        form.setText("");
        form.setHint("Phone Number");

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
        long phoneNumber = checkPhoneNumber(form.getText().toString());
        if (phoneNumber < 0) {
            form.setError("example: 19029992703");
        } else {
            selectedPhoneNumber = phoneNumber;
            loadBoard();
        }
    }

    private void loadBoard(){
        board = host.getBoard();
        board.clear();

        DataStorageModule.getFrontEnd().requestCustomerByPhone(selectedPhoneNumber, new DataRetriever<Map<String, Customer>>() {
            @Override
            public void retrievedData(Map<String, Customer> data) {
                customerMap = data;
                updateBoardDisplay();
            }
        });
    }

    private void updateBoardDisplay(){
        int index = 0;

        for(String customerName : customerMap.keySet()){
            final Customer c = customerMap.get(customerName);

            board.displayData(new CustomerDisplay(c,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            purpose.found(c);
                        }
                    },
                    null,
                    (index % 4) * (DataDisplay.STANDARD_BOX_WIDTH + 1),
                    (index / 4) * (DataDisplay.STANDARD_BOX_HEIGHT + 1)
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
                (index % 4) * (DataDisplay.STANDARD_BOX_WIDTH + 5),
                (index / 4) * (DataDisplay.STANDARD_BOX_HEIGHT + 5)
        ));

    }

    private void setupNewCustomerState(){
        host.setBarText(host.getTaskTitle() + " >>> New Customer");

        form.setText("");
        form.setHint("Customer Name");

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
            customer = DataStorageModule.getFrontEnd()
                    .createNewCustomerEntry(name, selectedPhoneNumber);
        }

        purpose.found(customer);
    }

    private long checkPhoneNumber(String s){
        try {
            long i = Long.parseLong(s);
            if (i < 10000000000L || i > 19999999999L){
                return -1;
            }
            return i;
        } catch (NumberFormatException e) {
            return -1;
        }
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
            return DataDisplay.STANDARD_BOX_WIDTH;
        }

        @Override
        public int getHeight() {
            return DataDisplay.STANDARD_BOX_HEIGHT;
        }

    }

    public static TaskSelectionButtion getMenuButton(Context c, final TaskHostestActivity host, final CustomerFoundListener purpose){
        return new TaskSelectionButtion(c){
            @Override
            public Task getTask() {
                return new CustomerFinderTask(host, purpose);
            }

            @Override
            public String getTaskName() {
                return "Customer Finder";
            }
        };
    }
}
