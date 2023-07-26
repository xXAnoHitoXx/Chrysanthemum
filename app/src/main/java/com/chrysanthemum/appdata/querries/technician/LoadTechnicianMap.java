package com.chrysanthemum.appdata.querries.technician;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.Map;
import java.util.TreeMap;

public class LoadTechnicianMap extends DBReadQuery<Map<String, Technician>> {

    @Override
    protected void executeQuery() {
        FireDatabase.getRef().child(DatabaseStructure.TechnicianBranch.BRANCH_NAME)
                .child(DatabaseStructure.TechnicianBranch.LIST).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();

                        Map<String, Technician> m = new TreeMap<>();

                        for(DataSnapshot child : snapshot.getChildren()){
                            Technician i = child.getValue(Technician.class);
                            m.put(i.getID()+ "", i);
                        }

                        returnQueryData(m);
                    }
                });
    }
}
