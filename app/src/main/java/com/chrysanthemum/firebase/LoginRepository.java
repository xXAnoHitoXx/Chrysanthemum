package com.chrysanthemum.firebase;

import androidx.annotation.NonNull;

import com.chrysanthemum.data.SecurityModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository extends SecurityModule {

    private static final String appName = "BubbleGum";

    public LoginRepository(){
        FireDatabase.getRef().child("app").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.getValue(String.class).equalsIgnoreCase(appName)){
                    blockAccess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void login(int colour, String password) {
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
        super.releaseAccess();
        FirebaseAuth.getInstance().signOut();
    }
}
