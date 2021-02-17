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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class FireDatabase {

    public void initialization(Context context){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        observeTechnicianList();
    }

    public SecurityModule generateSecurityModule(){
        return new LoginRepository();
    }

    private void observeTechnicianList(){
        getRef().child("technician").child("technicianList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                LinkedList<TechnicianIdentifier> techList = new LinkedList<>();

                for(DataSnapshot child : snapshot.getChildren()){
                    techList.add(child.getValue(TechnicianIdentifier.class));
                }

                DataStorageModule.getBackEnd().storeTechList(techList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    static DatabaseReference getRef(){
        return FirebaseDatabase.getInstance().getReference();
    }
}
