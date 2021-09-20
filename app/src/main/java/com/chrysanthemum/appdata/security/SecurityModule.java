package com.chrysanthemum.appdata.security;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.Util.BoolFlag;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.retreiver.NullRetriever;

public abstract class SecurityModule {

    private String UserID;

    public SecurityModule(){
        this.observeAccessToken(null, new Observer<AccessState>() {

            @Override
            public void onChanged(AccessState accessState) {
                if(accessState == AccessState.blocked){
                    DataStorageModule.getFrontEnd().close();
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

    protected void grantAccess(String UserID){
        this.UserID = UserID;
        accessToken.setValue(AccessState.hasAccess);
    }

    public void releaseAccess(){
        accessToken.setValue(null);
    }

    protected void blockAccess(){
        accessToken.setValue(AccessState.blocked);
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

    public abstract void login(Technician tech, int password);

    public Technician getLoggedinTech() {
        return status.getValue().getTech();
    }

    public void logout(){
        updateLoginStatus(LoginStatus.loggedOut);
    }

    public abstract void registerPassword(Technician tech, int password);

    public String getUserID(){
        return UserID;
    }

    //--------------------------------------------------------------------
    public abstract void enableTestMode(String uname, String pword);

    protected static boolean inTestMode = false;

    public static boolean inTestMode() {
        return inTestMode;
    }

}
