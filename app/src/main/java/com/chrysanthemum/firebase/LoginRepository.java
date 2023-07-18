package com.chrysanthemum.firebase;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.Util.BoolFlag;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.security.LoginStatus;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

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
                .addOnCompleteListener(task -> {
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
                });
    }

    @Override
    public void dummyLogin() {
        Technician dummy = new Technician(
                Technician.Dummy, Technician.Dummy, 0,
                DataStorageModule.generateID());

        LoginStatus.loggedIn.setTech(dummy);
        updateLoginStatus(LoginStatus.loggedIn);
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
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loadInitData();
                    } else {
                        releaseAccess();
                    }

                });

    }

    private void loadInitData(){
        DataStorageModule.getBackEnd().loadTechMap(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            grantAccess(currentUser.getUid());
        });
    }

    @Override
    public void releaseAccess() {
        super.releaseAccess();
        FirebaseAuth.getInstance().signOut();
    }

    public void enableTestMode(String uname, String pword){

        BoolFlag step1 = new BoolFlag();

        testModeAccess(uname, pword, step1);

        while (!step1.read()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        BoolFlag step2 = new BoolFlag();

        DataStorageModule.getBackEnd().loadTechMap(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            grantAccess(currentUser.getUid());

            step2.set();
        });

        while (!step2.read()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        BoolFlag step3 = new BoolFlag();

        testModeLogin(step3);

        while (!step3.read()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        inTestMode = true;
    }

    private void testModeAccess(String uname, String pword, BoolFlag f){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(uname, pword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        f.set();
                    } else {
                        releaseAccess();
                    }

                });
    }

    private void testModeLogin(BoolFlag complete){
        Technician admin = DataStorageModule.getFrontEnd().getActiveTechList().iterator().next();

        FireDatabase.getRef().child(DatabaseStructure.TechnicianBranch.BRANCH_NAME)
                .child(DatabaseStructure.TechnicianBranch.PASSWORD_STORAGE)
                .child("" + admin.getID()).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        LoginStatus.loggedIn.setTech(admin);
                        updateLoginStatus(LoginStatus.loggedIn);
                        complete.set();
                    }
                });
    }
}
