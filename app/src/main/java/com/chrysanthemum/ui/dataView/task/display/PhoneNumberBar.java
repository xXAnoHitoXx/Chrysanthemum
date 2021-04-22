package com.chrysanthemum.ui.dataView.task.display;

import android.widget.EditText;

import com.chrysanthemum.appdata.dataType.parsing.PhoneNumberParser;

public class PhoneNumberBar {
    public EditText bar;
    private long phoneNumber;

    public PhoneNumberBar(final EditText bar){
        this.bar = bar;
    }

    public boolean hasPhoneNumber(){

        String searchQuery = bar.getText().toString();

        phoneNumber = PhoneNumberParser.parse(searchQuery);

        if (!PhoneNumberParser.isValid(phoneNumber)) {
            bar.setError("Example: 1-902-999-2703");
            return false;
        } else {
            return true;
        }
    }

    public long getPhoneNumber(){
        return phoneNumber;
    }
}
