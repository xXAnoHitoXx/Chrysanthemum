package com.chrysanthemum.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.chrysanthemum.data.model.AuthenticationListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class FireBaseAuthenticator {


    protected void requestAccess(AuthenticationListener al, String username, String password) {
        final AuthenticationListener auth = al;

        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            auth.accessGranted();
                        } else {
                            Log.w("signInWithEmail:failure", task.getException());
                            auth.accessDenied();
                        }

                    }
                });
    }

    protected void releaseAccess() {
        FirebaseAuth.getInstance().signOut();
    }
}
