package com.chrysanthemum.firebase;

import com.chrysanthemum.appdata.security.SecurityModule;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * provide connection to firebase database
 * handle data query
 */
public class FireDatabase {

    public void initialization(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public SecurityModule generateSecurityModule(){
        return new LoginRepository();
    }

    public static DatabaseReference getRef(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        return (LoginRepository.inTestMode())?
                ref.child(DatabaseStructure.TEST) : ref;
    }

    public static void clearTestEnvironment(){
        if(LoginRepository.inTestMode()){
            getRef().removeValue();
        }
    }
}
