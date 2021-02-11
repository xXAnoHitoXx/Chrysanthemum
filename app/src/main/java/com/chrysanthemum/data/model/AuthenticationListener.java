package com.chrysanthemum.data.model;

public interface AuthenticationListener {
    public void accessGranted();
    public void accessDenied();
}
