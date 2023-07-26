package com.chrysanthemum.ui.accessAuthentication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.R;

class AccessAuthenticationViewModel extends ViewModel {

    private final MutableLiveData<AuthenticationFormState> loginFormState = new MutableLiveData<>();

    AccessAuthenticationViewModel() {}

    LiveData<AuthenticationFormState> getLoginFormState() {
        return loginFormState;
    }

    void login(String username, String password) {
        DataStorageModule.getModule().getSecurityModule().requestAccess(username, password);
    }

    void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new AuthenticationFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new AuthenticationFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new AuthenticationFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return false;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
