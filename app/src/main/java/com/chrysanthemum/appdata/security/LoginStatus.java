package com.chrysanthemum.appdata.security;

import com.chrysanthemum.appdata.dataType.TechnicianIdentifier;

public enum LoginStatus {
    loggedOut, loggedIn, noPass;

    private TechnicianIdentifier tech;

    public void setTech(TechnicianIdentifier tech) {
        this.tech = tech;
    }

    public TechnicianIdentifier getTech() {
        return tech;
    }
}
