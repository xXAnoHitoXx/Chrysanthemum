package com.chrysanthemum.data;

import com.chrysanthemum.data.model.AuthenticationListener;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private FireBaseAuthenticator dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore


    // private constructor : singleton access
    private LoginRepository(FireBaseAuthenticator dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(FireBaseAuthenticator dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        //TODO
        return true;
    }

    private void setLoggedInUser() {
        //TODO
    }

    public void requestAccess(AuthenticationListener al, String username, String password) {
        dataSource.requestAccess(al, username, password);
    }

    public void releaseAccess() {
        dataSource.releaseAccess();
    }

}
