package com.chrysanthemum.firebase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chrysanthemum.data.DataStorageModule;
import com.chrysanthemum.data.SecurityModule;
import com.chrysanthemum.data.TechnicianIdentifier;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;

public class Database {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    public void initialization(Context context){
        FirebaseOptions.Builder builder = new FirebaseOptions.Builder()
                .setApplicationId("1:151385711114:android:bd433037e98f367fcef5f3")
                .setApiKey(" AIzaSyDZq3TRh2t366xuuE4tMW9fIDIMTN0bQIs")
                .setDatabaseUrl("https://chrysanthemumdb-default-rtdb.firebaseio.com/")
                .setStorageBucket("gs://chrysanthemumdb.appspot.com");

        FirebaseApp.initializeApp(context, builder.build());

        retrieveTechnicianList();
    }

    public SecurityModule generateSecurityModule(){
        return new LoginRepository();
    }

    private void retrieveTechnicianList(){
        mDatabase.child("technicianList").get().addOnCompleteListener(
                new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){

                            DataSnapshot res = task.getResult();

                            LinkedList<TechnicianIdentifier> techList = new LinkedList<>();

                            for(DataSnapshot child : res.getChildren()){
                                techList.add(child.getValue(TechnicianIdentifier.class));
                            }

                            DataStorageModule.getBackEnd().storeTechList(techList);

                        } else {
                            Log.w("failed to retrive technician list", "boo");
                        }
                    }
                }
        );
    }
}
