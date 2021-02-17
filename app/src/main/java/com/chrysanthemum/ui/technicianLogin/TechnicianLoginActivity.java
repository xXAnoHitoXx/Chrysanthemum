package com.chrysanthemum.ui.technicianLogin;

import android.os.Bundle;

import com.chrysanthemum.R;
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
    }

}
