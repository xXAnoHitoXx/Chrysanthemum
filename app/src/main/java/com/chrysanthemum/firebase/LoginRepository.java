package com.chrysanthemum.firebase;

import androidx.annotation.NonNull;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.retreiver.NullRetriever;
import com.chrysanthemum.appdata.security.LoginStatus;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        FireDatabase.getRef().child(DatabaseStructure.CURRENTLY_ACTIVE_APP_INSTANCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!appName.equalsIgnoreCase(snapshot.getValue(String.class))){
                    blockAccess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void login(final Technician tech, final int password) {

        FireDatabase.getRef().child(DatabaseStructure.TechnicianBranch.BRANCH_NAME)
                .child(DatabaseStructure.TechnicianBranch.PASSWORD_STORAGE)
                .child("" + tech.getID()).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            @SuppressWarnings("ConstantConditions")
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    int correctPass = Integer.parseInt(task.getResult().getValue(String.class));

                    if(correctPass < 0 || correctPass > 9999){
                        LoginStatus.noPass.setTech(tech);
                        updateLoginStatus(LoginStatus.noPass);
                    } else if(correctPass == password) {
                        LoginStatus.loggedIn.setTech(tech);
                        updateLoginStatus(LoginStatus.loggedIn);
                    } else {
                        LoginStatus.loggedOut.setTech(tech);
                        updateLoginStatus(LoginStatus.loggedOut);
                    }
                }
            }
        });
    }

    @Override
    public void registerPassword(Technician tech, int password) {
        FireDatabase.getRef().child(DatabaseStructure.TechnicianBranch.BRANCH_NAME)
                .child(DatabaseStructure.TechnicianBranch.PASSWORD_STORAGE)
                .child("" + tech.getID()).setValue( password + "");
    }

    @Override
    public void requestAccess(String username, String password) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadInitData();
                        } else {
                            releaseAccess();
                        }

                    }
                });

    }

    private void loadInitData(){
        DataStorageModule.getBackEnd().loadTechMap(new NullRetriever() {
            @Override
            public void retrieved() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                grantAccess(currentUser.getUid());
            }
        });
    }

    @Override
    public void releaseAccess() {
        super.releaseAccess();
        FirebaseAuth.getInstance().signOut();
    }
}
