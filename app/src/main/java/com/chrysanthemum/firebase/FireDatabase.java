package com.chrysanthemum.firebase;

import androidx.annotation.NonNull;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.TreeMap;

/**
 * provide connection to firebase database
 */
public class FireDatabase {

    public void initialization(){
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

                Map<String, Technician> m = new TreeMap<>();

                for(DataSnapshot child : snapshot.getChildren()){
                    Technician i = child.getValue(Technician.class);
                    m.put(i.getID()+ "", i);
                }

                DataStorageModule.getBackEnd().storeTechMap(m);
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
