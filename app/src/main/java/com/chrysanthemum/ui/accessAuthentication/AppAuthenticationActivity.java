package com.chrysanthemum.ui.accessAuthentication;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chrysanthemum.R;
import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.security.AccessState;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.ui.technicianLogin.TechnicianLoginActivity;

public class AppAuthenticationActivity extends AppCompatActivity {

    private AccessAuthenticationViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataStorageModule.init();

        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new AccessAuthenticationViewModelFactory()).get(AccessAuthenticationViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<AuthenticationFormState>() {
            @Override
            public void onChanged(AuthenticationFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        DataStorageModule.getFrontEnd().getSecurityModule().observeAccessToken(this, new Observer<AccessState>() {
            @Override
            public void onChanged(AccessState loginResult) {

                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult == AccessState.noAccess) {
                    showLoginFailed();
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                if (loginResult == AccessState.hasAccess) {
                    updateUiWithUser();
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

    }

    private void updateUiWithUser() {
        String welcome = getString(R.string.welcome);
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, TechnicianLoginActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed() {
        Toast.makeText(getApplicationContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
    }
}
