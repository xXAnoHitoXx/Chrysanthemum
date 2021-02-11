package com.chrysanthemum.ui.accessAuthentication;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.chrysanthemum.data.FireBaseAuthenticator;
import com.chrysanthemum.data.LoginRepository;

/**
 * ViewModel provider factory to instantiate AccessAuthenticationViewModel.
 * Required given AccessAuthenticationViewModel has a non-empty constructor
 */
public class AccessAuthenticationViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AccessAuthenticationViewModel.class)) {
            return (T) new AccessAuthenticationViewModel(LoginRepository.getInstance(new FireBaseAuthenticator()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
