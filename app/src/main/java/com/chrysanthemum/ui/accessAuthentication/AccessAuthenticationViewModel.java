package com.chrysanthemum.ui.accessAuthentication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.chrysanthemum.data.LoginRepository;
import com.chrysanthemum.R;
import com.chrysanthemum.data.model.AuthenticationListener;

public class AccessAuthenticationViewModel extends ViewModel {

    private MutableLiveData<AuthenticationFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<AccessAuthenticationResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    AccessAuthenticationViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<AuthenticationFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<AccessAuthenticationResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {

        loginRepository.requestAccess(new AuthenticationListener(){

            @Override
            public void accessGranted() {
                loginResult.setValue(new AccessAuthenticationResult());
            }

            @Override
            public void accessDenied() {
                loginResult.setValue(new AccessAuthenticationResult(R.string.login_failed));
            }

        },username, password);
    }

    public void loginDataChanged(String username, String password) {
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
