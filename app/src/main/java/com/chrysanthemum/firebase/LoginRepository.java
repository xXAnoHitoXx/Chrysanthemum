package com.chrysanthemum.firebase;

import androidx.annotation.NonNull;

import com.chrysanthemum.data.SecurityModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository extends SecurityModule {

    @Override
    public void login(String email, String password) {
        //TODO
    }

    @Override
    public void logout() {
        //TODO
    }

    public void requestAccess(String username, String password) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            grantAccess();
                        } else {
                            releaseAccess();
                        }

                    }
                });
    }

    public void releaseAccess() {
        FirebaseAuth.getInstance().signOut();
    }
}
