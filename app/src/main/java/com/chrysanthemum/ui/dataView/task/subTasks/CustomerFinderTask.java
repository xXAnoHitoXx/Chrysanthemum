package com.chrysanthemum.ui.dataView.task.subTasks;

import android.annotation.SuppressLint;
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
import com.chrysanthemum.appdata.dataType.subType.Colour;
import com.chrysanthemum.appdata.querries.DBCreateQuery;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.TimedOutException;
import com.chrysanthemum.appdata.querries.customer.CreateCustomer;
import com.chrysanthemum.appdata.querries.customer.read.FindCustomersByName;
import com.chrysanthemum.appdata.querries.customer.read.FindCustomersByPhone;
import com.chrysanthemum.ui.dataView.display.DisplayBoard;
import com.chrysanthemum.ui.dataView.display.Displayable;
import com.chrysanthemum.ui.dataView.task.Task;
import com.chrysanthemum.ui.dataView.task.TaskHostestActivity;
import com.chrysanthemum.ui.dataView.task.display.PhoneNumberBar;

import java.util.Collection;
import java.util.Map;

public class CustomerFinderTask extends Task {

    public static final int STANDARD_BOX_WIDTH = 210;
    public static final int STANDARD_BOX_HEIGHT = 66;

    private static Customer lastCustomer = null;

    private DisplayBoard board;
    private Collection<Customer> customerMap;

    private long selectedPhoneNumber;
    private String selectedName = "";

    private final DataRetriever<Customer> purpose;

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

        TextView label = host.createFormLabel(1);
        final EditText form = host.createEditableForm(1);
        final PhoneNumberBar phoneBar = new PhoneNumberBar(form);

        label.setText("Name / Phone Number:");

        form.setText("");
        form.setHint("Sophia, 1-902-999-2703, or leave blank for last customer serviced");

        Button b = host.getFormButton();
        b.setText("Search");
        b.setOnClickListener(v -> {

            String searchQuery = form.getText().toString();

            if(searchQuery.equals("")){
                if(lastCustomer != null){
                    chooseLastCustomer();
                } else {
                    form.setError("We haven't serviced a Customer yet today");
                }
                return;
            }

            if(searchQuery.matches(".*\\d.*")){

                if(phoneBar.hasPhoneNumber()){
                    selectedPhoneNumber = phoneBar.getPhoneNumber();
                    selectedName = "";
                    loadBoard();
                }
            } else {

                if(searchQuery.length() > 1){
                    selectedName = searchQuery;
                    selectedPhoneNumber = 0;
                    loadBoard();
                }
            }

        });
    }

    private void chooseLastCustomer(){
        host.createAlertBox()
                .setTitle("Last Customer Serviced was:")
                .setMessage(lastCustomer.getName() + "\n" + lastCustomer.getPhoneNumber())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> purpose.retrievedData(lastCustomer))
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void loadBoard(){
        board = host.getBoard();
        board.clear(host.getScale());

        try{
            DBReadQuery<Map<String, Customer>> query = (selectedPhoneNumber > 0)?
                    new FindCustomersByPhone(selectedPhoneNumber) :
                    new FindCustomersByName(selectedName);

            updateBoardDisplay(query.execute());
        } catch (TimedOutException e) {
            host.popMessage("Customer Search Time Out");
        }
    }

    private void updateBoardDisplay(Map<String, Customer> data){
        customerMap = data.values();

        int index = 0;

        for(Customer c : customerMap){

            board.displayData(new CustomerDisplay(c,
                    v -> {
                        lastCustomer = c;
                        purpose.retrievedData(c);
                    },
                    null,
                    (index % 4) * (STANDARD_BOX_WIDTH + 1),
                    (index / 4) * (STANDARD_BOX_HEIGHT + 1)
            ));

            index++;
        }

        board.displayData(new CustomerDisplay(
                v -> setupNewCustomerState(),
                (index % 4) * (STANDARD_BOX_WIDTH + 1),
                (index / 4) * (STANDARD_BOX_HEIGHT + 1)
        ));

    }

    @SuppressLint("SetTextI18n")
    private void setupNewCustomerState(){
        host.setBarText(host.getMainTaskTitle() + " >>> New Customer");
        host.clearForm();

        final EditText nameForm = host.createEditableForm(1);
        TextView nameLabel = host.createFormLabel(1);

        nameLabel.setText("Customer Name:");

        nameForm.setText(selectedName);
        nameForm.setHint("John Doe");

        final EditText phoneForm = host.createEditableForm(2);
        TextView phoneLabel = host.createFormLabel(2);

        phoneLabel.setText("Phone Number:");

        final PhoneNumberBar phoneBar = new PhoneNumberBar(phoneForm);

        if(selectedPhoneNumber > 0){
            phoneForm.setText(PhoneNumberParser.revParse(selectedPhoneNumber));
        } else {
            phoneForm.setText(PhoneNumberParser.revParse(19024054988L));
        }

        Button b = host.getFormButton();
        b.setText("Create");
        b.setOnClickListener(v -> {
            if(phoneBar.hasPhoneNumber()){
                selectedPhoneNumber = phoneBar.getPhoneNumber();
                selectedName = nameForm.getText().toString();
                createCustomer();
            }
        });
    }

    private void createCustomer(){

        for(Customer c : customerMap){
            if(c.getPhoneNumber() == selectedPhoneNumber && c.getName().equals(selectedName)){
                purpose.retrievedData(c);
            }
        }

        DBCreateQuery<Customer> query = new CreateCustomer(selectedName, selectedPhoneNumber);
        Customer customer = query.execute();

        lastCustomer = customer;
        purpose.retrievedData(customer);
    }

    public static void service(Customer customer){
        lastCustomer = customer;
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

        @Override
        public Colour getBGColour() {
            return new Colour(colour);
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
