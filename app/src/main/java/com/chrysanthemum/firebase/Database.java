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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;

public class Database {

    private DatabaseReference mDatabase;

    public void initialization(Context context){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        retrieveTechnicianList();
    }

    public SecurityModule generateSecurityModule(){
        return new LoginRepository();
    }

    private void retrieveTechnicianList(){
        mDatabase.child("technician").child("technicianList").get().addOnCompleteListener(
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
