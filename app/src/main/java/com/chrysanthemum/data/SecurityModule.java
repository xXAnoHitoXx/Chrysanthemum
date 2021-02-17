package com.chrysanthemum.data;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public abstract class SecurityModule {

    public enum AccessState {
        hasAccess, noAccess, blocked
    }

    public abstract void login(int colour, String password);
    public abstract void logout();
    public abstract void requestAccess(String email, String password);

    public SecurityModule(){
        this.observeAccessToken(null, new Observer<SecurityModule.AccessState>() {

            @Override
            public void onChanged(SecurityModule.AccessState accessState) {
                if(accessState == SecurityModule.AccessState.blocked){
                    logout();
                    releaseAccess();
                    System.exit(0);
                }
            }
        });
    }

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
}
