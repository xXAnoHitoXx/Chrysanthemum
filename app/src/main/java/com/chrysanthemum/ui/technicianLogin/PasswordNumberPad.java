package com.chrysanthemum.ui.technicianLogin;

import android.widget.Button;
import android.widget.TextView;

import com.chrysanthemum.R;
import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.security.SecurityModule;

import androidx.lifecycle.LifecycleOwner;

public class PasswordNumberPad {

    private enum Mode {
        Login, CreatePassword
    }

    private int value = 0;
    private int savedValue = 0;
    private int digitCount = 0;
    private Mode mode = Mode.Login;
    private boolean locked = false;

    private void enter(int number){
        if(digitCount < 4){
            value *= 10;
            value += number;
            digitCount++;
            displayProgress();
        }
    }

    private void bkspc(){
        if(digitCount > 0){
            value /= 10;
            digitCount--;
            displayProgress();
        }
    }

    private void clear(){
        value = 0;
        digitCount = 0;
        displayProgress();
    }

    //-------------------------------------------------------------------------------------

    private final TechnicianSelectorPanel panel;
    private final TextView message, progress;

    public PasswordNumberPad(Button[] controls, TechnicianSelectorPanel panel,
                             TextView message, TextView progress, LifecycleOwner owner){
        this.panel = panel;
        this.message = message;
        this.progress = progress;

        for(final Button b : controls){
            if(getRes(R.string.bkspc).equals(b.getText() + "")){
                setBkspcButton(b);
            } else if(getRes(R.string.go).equals(b.getText() + "")){
                setGoButton(b);
            } else {
                setNumberButton(b);
            }
        }
        

        observeAndDisplayStatus(owner);
    }

    private void setNumberButton(final Button b){
        b.setOnClickListener(v -> {
            if(!locked){
                int i = Integer.parseInt(b.getText() + "");
                enter(i);
            }
        });
    }

    private void setBkspcButton(final Button b){
        b.setOnClickListener(v -> {
            if(!locked) {
                bkspc();
            }
        });
    }

    private void setGoButton(final Button b){
        b.setOnClickListener(v -> {
            if(!locked) {
                Technician tech = panel.getSelectedTech();

                if (mode == Mode.Login) {

                    if(tech == null){
                        if(digitCount == 0) {
                            SecurityModule sm = DataStorageModule.getFrontEnd().getSecurityModule();
                            sm.dummyLogin();
                        } else {
                            setMessage("Please chose a colour!");
                        }
                    } else {
                        lock();
                        SecurityModule sm = DataStorageModule.getFrontEnd().getSecurityModule();
                        sm.login(panel.getSelectedTech(), value);
                    }

                }else if (tech != null) {
                    mode = Mode.Login;

                    if (value == savedValue) {
                        DataStorageModule.getFrontEnd().getSecurityModule()
                                .registerPassword(panel.getSelectedTech(), value);
                        setMessage("New password registered!");
                        clear();
                    } else {
                        setMessage("Passwords did not match!");
                        clear();
                    }

                    panel.unlock();
                }
            }
        });
    }

    private void displayProgress() {
        StringBuilder currentProgress = new StringBuilder();

        for(int i = 0; i < digitCount; i++){
            currentProgress.append("* ");
        }

        progress.setText(currentProgress.toString());
        progress.invalidate();
    }

    private void observeAndDisplayStatus(LifecycleOwner owner) {
        SecurityModule sm = DataStorageModule.getFrontEnd().getSecurityModule();
        sm.observeLoginStatus(owner, loginStatus -> {
            unlock();
            switch(loginStatus){
                case noPass: {
                    panel.lock();
                    setMessage("Enter again to verify new password!!!");
                    savedValue = value;
                    clear();
                    mode = Mode.CreatePassword;
                    break;
                }
                case loggedOut: {
                    setMessage("Wrong password!");
                    clear();
                    break;
                }
            }
        });
    }

    private void setMessage(String m){
        message.setText(m);
        message.invalidate();
    }

    private String getRes(int resID){
        return message.getContext().getResources().getString(resID);
    }

    private void lock(){
        locked = true;
    }

    private void unlock(){
        locked = false;
    }


}
