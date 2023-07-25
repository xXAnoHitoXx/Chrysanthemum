package com.chrysanthemum.ui.technicianLogin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chrysanthemum.R;
import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.security.LoginStatus;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.ui.accessAuthentication.AppAuthenticationActivity;
import com.chrysanthemum.ui.dataView.DataDisplayActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TechnicianLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout layout = findViewById(R.id.colour_panel);

        TechnicianSelectorPanel selectorPanel = new TechnicianSelectorPanel(this,
                layout, true);

        Button[] controls = {
                findViewById(R.id.button0),
                findViewById(R.id.button1),
                findViewById(R.id.button2),
                findViewById(R.id.button3),
                findViewById(R.id.button4),
                findViewById(R.id.button5),
                findViewById(R.id.button6),
                findViewById(R.id.button7),
                findViewById(R.id.button8),
                findViewById(R.id.button9),
                findViewById(R.id.buttonDel),
                findViewById(R.id.buttonGo)
        };

        TextView message = findViewById(R.id.statusMessage);
        TextView progress = findViewById(R.id.passwordProgress);

        new PasswordNumberPad(controls, selectorPanel, message, progress, this);

        View x = findViewById(R.id.fab);
        x.setOnClickListener(v -> logoutAlert());

        SecurityModule sm = DataStorageModule.getModule().getSecurityModule();
        sm.observeLoginStatus(this, loginStatus -> {
            if(loginStatus == LoginStatus.loggedIn){
                onLoginSuccess();
            }
        });
    }

    @Override
    public void onBackPressed(){

    }


    private void onLoginSuccess(){
        Intent intent = new Intent(this, DataDisplayActivity.class);
        startActivity(intent);
    }


    private void logoutAlert(){
        new AlertDialog.Builder(this)
                .setTitle("Close App?")
                .setMessage("If ur not Tinn, say no xD")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> endSession())
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void endSession() {
        DataStorageModule.getModule().close();
        Intent intent = new Intent(this, AppAuthenticationActivity.class);
        startActivity(intent);
    }

}
