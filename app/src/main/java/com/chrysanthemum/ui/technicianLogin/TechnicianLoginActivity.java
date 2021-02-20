package com.chrysanthemum.ui.technicianLogin;

import android.content.Intent;
import android.os.Bundle;

import com.chrysanthemum.R;
import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.ui.accessAuthentication.AppAuthenticationActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TechnicianLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout layout = findViewById(R.id.colour_panel);

        TechnicianSelectorPanel selectorPanel = new TechnicianSelectorPanel(this,
                layout);

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

        PasswordNumberPad numPad = new PasswordNumberPad(controls, selectorPanel, message, progress, this);

        View x = findViewById(R.id.fab);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession();
            }
        });
    }

    private void endSession() {
        DataStorageModule.getFrontEnd().close();
        Intent intent = new Intent(this, AppAuthenticationActivity.class);
        startActivity(intent);
    }

}
