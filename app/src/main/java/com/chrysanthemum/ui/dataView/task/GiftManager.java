package com.chrysanthemum.ui.dataView.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.Util.Scaler;
import com.chrysanthemum.appdata.dataType.Gift;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.gift.EditGiftDataQuery;
import com.chrysanthemum.appdata.querries.gift.FindGiftCardByIDQuery;
import com.chrysanthemum.appdata.querries.gift.NewGiftCardQuery;
import com.chrysanthemum.ui.dataView.task.display.LineDisplayLayoutTask;
import com.chrysanthemum.ui.dataView.task.subTasks.DaySelectorTask;

import java.time.LocalDate;

public class GiftManager extends LineDisplayLayoutTask {

    private static final float TASK_SCALE = 2f;

    public GiftManager(TaskHostestActivity host) {
        super(host);
    }

    @Override
    public void start() {
        host.getBoard().clear(new Scaler(TASK_SCALE));
        giftSearch();
    }

    @SuppressLint("SetTextI18n")
    private void giftSearch(){
        host.clearForm();

        TextView lbl = host.createFormLabel(1);
        final EditText code = host.createEditableForm(1);

        lbl.setText("Gift code:");

        Button button = host.getFormButton();
        button.setText("Search");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindGiftCardByIDQuery q = new FindGiftCardByIDQuery(code.getText().toString(), new DataRetriever<Gift>() {
                    @Override
                    public void retrievedData(Gift data) {
                        if(data == null){
                            LocalDate expire = LocalDate.now().plusYears(3);
                            createGift(code.getText().toString(), TimeParser.parseDateDisplayDay(expire));
                        } else {
                            displayGiftData(data);
                        }
                    }
                });

                q.executeQuery();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void createGift(final String code, final String expire){
        host.clearForm();

        TextView amtLbl = host.createFormLabel(1);
        final EditText amount = host.createEditableForm(1);

        amtLbl.setText("Amount:");
        amount.setText("$50.00");

        TextView expireDateLbl = host.createFormLabel(2);
        final EditText expireDate = host.createEditableForm(2);

        expireDateLbl.setText("Expires Date");
        expireDate.setText(expire);
        expireDate.setFocusable(false);
        expireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DaySelectorTask(host, new DataRetriever<LocalDate>() {
                    @Override
                    public void retrievedData(LocalDate data) {
                        host.getBoard().clear(new Scaler(TASK_SCALE));
                        createGift(code, TimeParser.parseDateDisplayDay(data));
                    }
                }).start();
            }
        });

        Button button = host.getFormButton();
        button.setText("Create Gift");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateIssued = TimeParser.parseDateData(LocalDate.now());

                NewGiftCardQuery q = new NewGiftCardQuery(code, formatAmount(amount.getText().toString()),
                        dateIssued, expireDate.getText().toString().replaceAll("/", " "),
                        new DataRetriever<Gift>() {
                            @Override
                            public void retrievedData(Gift data) {
                                giftSearch();
                                displayGiftData(data);
                            }
                        });

                q.executeQuery();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void editGift(final Gift gift, String expDate){
        host.clearForm();

        TextView amtLbl = host.createFormLabel(1);
        final EditText amount = host.createEditableForm(1);

        amtLbl.setText("Amount:");
        amount.setText(gift.getAmount());

        TextView expireDateLbl = host.createFormLabel(2);
        final EditText expireDate = host.createEditableForm(2);

        expireDateLbl.setText("Expires Date");
        expireDate.setText(expDate);
        expireDate.setFocusable(false);
        expireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DaySelectorTask(host, new DataRetriever<LocalDate>() {
                    @Override
                    public void retrievedData(LocalDate data) {
                        editGift(gift, TimeParser.parseDateDisplayDay(data));
                        displayGiftData(gift);
                    }
                }).start();
            }
        });

        Button button = host.getFormButton();
        button.setText("Edit Gift");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditGiftDataQuery q = new EditGiftDataQuery(gift, formatAmount(amount.getText().toString()),
                        expireDate.getText().toString().replaceAll("/", " "));
                displayGiftData(q.executeQuery());
            }
        });
    }

    private String formatAmount(String amount){
        StringBuilder builder = new StringBuilder();

        if(!amount.substring(0, 1).equalsIgnoreCase("$")){
            builder.append("$");
        }

        if(!amount.contains(".")){
            builder.append(amount.substring(0, amount.length() - 2));
            builder.append(".");
            builder.append(amount.substring(amount.length() - 2));
        } else {
            builder.append(amount);
        }

        return builder.toString();
    }

    private void displayGiftData(final Gift gift){
        host.getBoard().clear(new Scaler(TASK_SCALE));

        String[] labels = new String[]{
               "Code:", "Amount:", "Date Issued:", "Date Expired:"
        };

        displayLine(labels, Color.LTGRAY, 0, null);

        String[] data = new String[]{
                gift.getId(), gift.getAmount(), gift.getDateIssued().replaceAll(" ", "/"), gift.getDateExpires().replaceAll(" ", "/")
        };

        displayLine(data, Color.LTGRAY, 1, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editGift(gift, gift.getDateExpires().replaceAll(" ", "/"));
                return true;
            }
        });

    }


    public static TaskSelectionButtion getMenuButton(Context context, final TaskHostestActivity host){
        return new TaskSelectionButtion(context){
            @Override
            public Task getTask() {
                return new GiftManager(host);
            }

            @Override
            public String getTaskName() {
                return "Gift Cards Manager";
            }
        };
    }
}
