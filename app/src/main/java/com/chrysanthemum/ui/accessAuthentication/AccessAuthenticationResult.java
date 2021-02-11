package com.chrysanthemum.ui.accessAuthentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class AccessAuthenticationResult {
    @NonNull
    private boolean success;
    @Nullable
    private Integer error;

    AccessAuthenticationResult(@Nullable Integer error) {
        this.error = error;
        this.success = false;
    }

    AccessAuthenticationResult() {
        this.success = true;
    }

    @NonNull
    boolean getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
