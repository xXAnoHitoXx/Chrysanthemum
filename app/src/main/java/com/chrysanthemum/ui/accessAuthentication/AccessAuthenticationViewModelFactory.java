package com.chrysanthemum.ui.accessAuthentication;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

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
            return (T) new AccessAuthenticationViewModel();
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
