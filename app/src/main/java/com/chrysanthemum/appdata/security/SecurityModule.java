package com.chrysanthemum.appdata.security;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public abstract class SecurityModule {

    public SecurityModule(){
        this.observeAccessToken(null, new Observer<AccessState>() {

            @Override
            public void onChanged(AccessState accessState) {
                if(accessState == AccessState.blocked){
                    logout();
                    releaseAccess();
                    System.exit(0);
                }
            }
        });
    }

    //--------------------------------------------------------------------------------------------
    public abstract void requestAccess(String email, String password);

    /**
     * token representing the app's access to the database
     */
    private final MutableLiveData<AccessState> accessToken = new MutableLiveData<>();

    public void observeAccessToken(LifecycleOwner owner, Observer<AccessState> observer){
        if(owner == null){
            accessToken.observeForever(observer);
        } else {
            accessToken.observe(owner, observer);
        }
    }

    protected void grantAccess(){
        accessToken.setValue(AccessState.hasAccess);
    }

    public void releaseAccess(){
        accessToken.setValue(AccessState.noAccess);
    }

    protected void blockAccess(){
        accessToken.setValue(AccessState.blocked);
    }

    public boolean hasDBAccess(){
        return accessToken.getValue() == AccessState.hasAccess;
    }

    //----------------------------------------------------------------------------------------------
    private final MutableLiveData<LoginStatus> status = new MutableLiveData<>();

    public void observeLoginStatus(LifecycleOwner owner, Observer<LoginStatus> observer){
        if(owner == null){
            status.observeForever(observer);
        } else {
            status.observe(owner, observer);
        }
    }

    protected void updateLoginStatus(LoginStatus newStatus){
        status.setValue(newStatus);
    }

    public abstract void login(TechnicianIdentifier tech, int password);

    public void logout(){
        updateLoginStatus(LoginStatus.loggedOut);
    }
}
