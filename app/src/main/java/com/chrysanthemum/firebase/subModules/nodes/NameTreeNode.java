package com.chrysanthemum.firebase.subModules.nodes;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.LinkedList;

public class NameTreeNode {
    private final long id;
    private NameTreeNodeRef[] childs = new NameTreeNodeRef[10];
    private int count = 0;

    public NameTreeNode(long id){
        this.id = id;
    }

    public void insert(String token){

    }

    public LinkedList<Long> retrieve(String token){
        return null;
    }

    private void insert(NameTreeNodeRef ref){
        if(count < 10){
            childs[count++] = ref;
        }
    }

    public static void loadNode(final long id, DataRetriever<NameTreeNode> retriever){
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.C_NAME).child("" + id).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();

                NameTreeNode n = new NameTreeNode(id);

                if(snapshot.exists()){
                    for(DataSnapshot child : snapshot.getChildren()){
                        NameTreeNodeRef ref = child.getValue(NameTreeNodeRef.class);

                        n.insert(ref);
                    }
                }

                retriever.retrievedData(n);
            }
        });
    }
}
