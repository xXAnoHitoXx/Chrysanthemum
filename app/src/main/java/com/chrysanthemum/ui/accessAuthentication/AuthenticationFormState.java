package com.chrysanthemum.ui.accessAuthentication;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class AuthenticationFormState {
    @Nullable
    private final Integer usernameError;
    @Nullable
    private final Integer passwordError;
    private final boolean isDataValid;

    AuthenticationFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    AuthenticationFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
