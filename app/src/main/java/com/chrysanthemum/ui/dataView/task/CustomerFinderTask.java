package com.chrysanthemum.ui.dataView.task;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.dataView.display.Displayable;

import java.util.Map;

public class CustomerFinderTask extends Task {

    private DisplayBoard board;
    private Map<String, Customer> customerMap;

    private long selectedPhoneNumber;
    private Customer selectedCustomer;

    public CustomerFinderTask(TaskHostestActivity host) {
        super(host);
    }

    public void start(){
        board = host.createBoard(4);

        LinearLayout list = host.getTechList();
        list.removeAllViews();
        list.setVisibility(View.INVISIBLE);

        setupFindState();
    }

    private void setupFindState() {
        host.setBarText(host.getTaskTitle());

        EditText form1 = host.getForm(1);
        form1.setText("");
        form1.setHint("Phone Number");
        form1.setVisibility(View.VISIBLE);

        host.getForm(2).setVisibility(View.INVISIBLE);

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
        EditText form = host.getForm(1);
        long phoneNumber = checkPhoneNumber(form.getText().toString());
        if (phoneNumber < 0) {
            form.setError("example: 19029992703");
        } else {
            selectedPhoneNumber = phoneNumber;
            loadBoard();
        }
    }

    private void loadBoard(){
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

            board.displayData(index % 4, index / 4, new CustomerDisplay(c,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedCustomer = c;
                            setupNewAppointmentState();
                        }
                    },
                    new View.OnLongClickListener(){

                        @Override
                        public boolean onLongClick(View v) {
                            selectedCustomer = c;
                            setupDetailedCustomerViewState();
                            return true;
                        }
                    }
            ));

            index++;
        }

        board.displayData(index % 4, index / 4, new CustomerDisplay(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setupNewCustomerState();
                    }
                }));

    }

    private void setupNewCustomerState(){
        host.setBarText(host.getTaskTitle() + " >>> New Customer");

        EditText form1 = host.getForm(1);
        form1.setText("");
        form1.setHint("Customer Name");
        form1.setVisibility(View.VISIBLE);

        host.getForm(2).setVisibility(View.INVISIBLE);

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
        EditText form = host.getForm(1);
        String name = form.getText().toString();
        selectedCustomer = DataStorageModule.getFrontEnd()
                .createNewCustomerEntry(name, selectedPhoneNumber);
        setupNewAppointmentState();
    }

    private void setupNewAppointmentState(){
        host.setBarText(host.getTaskTitle() + " >>> New Appointment");

        EditText form1 = host.getForm(1);
        form1.setText("");
        form1.setHint("Date");
        form1.setVisibility(View.VISIBLE);

        EditText form2 = host.getForm(2);
        form2.setText("");
        form2.setHint("Time");
        form2.setVisibility(View.VISIBLE);

        Button b = host.getFormButton();
        b.setText("Create");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAppointment();
            }
        });
    }

    private void createAppointment(){

    }

    private void setupDetailedCustomerViewState(){
        Log.w("Entered Method", "Detailed view");
        Log.w("Customer Name", selectedCustomer.getName());
        Log.w("Customer Number", selectedCustomer.getPhoneNumber() + "");
        //TODO
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

    private class CustomerDisplay implements Displayable {

        private final String data;
        private final View.OnClickListener onclick;
        private final View.OnLongClickListener onLongClick;
        private final int colour;

        public CustomerDisplay(Customer c, View.OnClickListener listener, View.OnLongClickListener onLongClick){
            onclick = listener;
            this.onLongClick = onLongClick;
            data = c.getName() +
                    "\n" +
                    host.formatPhoneNumber(c.getPhoneNumber());
            colour = Color.GRAY;
        }

        public CustomerDisplay(View.OnClickListener listener) {
            onclick = listener;
            onLongClick = null;
            data = "New Customer";
            colour = Color.LTGRAY;
        }

        @Nullable
        @Override
        public Drawable getBGDrawable(Rect boundingBox) {
            ShapeDrawable drawable =  new ShapeDrawable(new RectShape());
            drawable.getPaint().setColor(colour);
            drawable.setBounds(boundingBox);
            return drawable;
        }

        @Override
        public String getDisplayData() {
            return data;
        }

        @Override
        public View.OnClickListener getOnclickListener() {
            return onclick;
        }

        @Override
        public View.OnLongClickListener getOnLongClickListener() {
            return onLongClick;
        }
    }
}
