package com.chrysanthemum.appdata.security;

import com.chrysanthemum.appdata.dataType.Technician;

public enum LoginStatus {
    loggedOut, loggedIn, noPass;

    private Technician tech;

    public void setTech(Technician tech) {
        this.tech = tech;
    }

    public Technician getTech() {
        return tech;
    }
}
