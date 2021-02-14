package com.chrysanthemum.data;

import android.content.Context;

import androidx.lifecycle.Observer;

import com.chrysanthemum.firebase.FirebaseInitializer;
import com.chrysanthemum.firebase.LoginRepository;

/**
 * the database as percived by the logic system
 */
public class DatabaseModule {

    private static volatile DatabaseModule database;

    public static void init(Context context){
        if(database == null){
            new FirebaseInitializer().initialization(context);

            database = new DatabaseModule();

            database.setSecurityModule(new LoginRepository());
        }
    }

    public static DatabaseModule getInstance() {
        return database;
    }

    private DatabaseModule(){}

    public void close(){
        logRepo.logout();
        logRepo.releaseAccess();
    }

    //-----------------------------------------------------------------------------------------------

    private SecurityModule logRepo;

    private void setSecurityModule(SecurityModule repo){
        logRepo = repo;
    }

    public SecurityModule getSecurityModule(){
        return logRepo;
    }

    //-----------------------------------------------------------------------------------------------
}
