package com.chrysanthemum.ui.dataView.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chrysanthemum.appdata.Util.Scaler;
import com.chrysanthemum.appdata.dataType.Gift;
import com.chrysanthemum.appdata.dataType.parsing.MoneyParser;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.querries.DBCreateQuery;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.DBUpdateQuery;
import com.chrysanthemum.appdata.querries.TimedOutException;
import com.chrysanthemum.appdata.querries.gift.CreateGiftCard;
import com.chrysanthemum.appdata.querries.gift.FindGiftCardByID;
import com.chrysanthemum.appdata.querries.gift.UpdateGiftCardAmount;
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
        button.setOnClickListener(v -> {
            try {
                String id = code.getText().toString();

                DBReadQuery<Gift> q = new FindGiftCardByID(id);
                Gift gift = q.execute();

                if(gift == null){
                    LocalDate expire = LocalDate.now().plusYears(3);
                    createGift(code.getText().toString(), TimeParser.parseDateDisplayDay(expire));
                } else {
                    displayGiftData(gift);
                }
            } catch (TimedOutException e) {
                host.popMessage("Gift Card Loading Timed Out");
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
        expireDate.setOnClickListener(v -> new DaySelectorTask(host, data -> {
            host.getBoard().clear(new Scaler(TASK_SCALE));
            createGift(code, TimeParser.parseDateDisplayDay(data));
        }).start());

        Button button = host.getFormButton();
        button.setText("Create Gift");
        button.setOnClickListener(v -> {
            String dateIssued = TimeParser.parseDateData(LocalDate.now());

            int amt = MoneyParser.parseSingleAmount(amount.getText().toString());
            if(amt == Integer.MIN_VALUE){
                amount.setError("Amount Isn't Recognizable!");
                return;
            }

            DBCreateQuery<Gift> query = new CreateGiftCard(code, MoneyParser.parseSingleAmount(amt),
                    dateIssued, expireDate.getText().toString().replaceAll("/", " "));
            Gift gift = query.execute();

            giftSearch();
            displayGiftData(gift);
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
        expireDate.setOnClickListener(v -> new DaySelectorTask(host, data -> {
            editGift(gift, TimeParser.parseDateDisplayDay(data));
            displayGiftData(gift);
        }).start());

        Button button = host.getFormButton();
        button.setText("Edit Gift");
        button.setOnClickListener(v -> {


            int amt = MoneyParser.parseSingleAmount(amount.getText().toString());
            if(amt == Integer.MIN_VALUE){
                amount.setError("Amount Isn't Recognizable!");
                return;
            }

            DBUpdateQuery<Gift> q = new UpdateGiftCardAmount(gift, MoneyParser.parseSingleAmount(amt));
            q.execute();

            displayGiftData(gift);
        });
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

        displayLine(data, Color.LTGRAY, 1, v -> {
            editGift(gift, gift.getDateExpires().replaceAll(" ", "/"));
            return true;
        });

    }


    public static TaskSelectionButton getMenuButton(Context context, final TaskHostestActivity host){
        return new TaskSelectionButton(context){
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
