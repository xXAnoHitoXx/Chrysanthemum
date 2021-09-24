package com.chrysanthemum.firebase.subModules;

import com.chrysanthemum.appdata.RemoteDataBase.CustomerManager;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import org.apache.commons.codec.language.Metaphone;

import java.util.LinkedList;
import java.util.Scanner;

public class MetaphoneCustomerManagerModule extends CustomerManagerModule {

    @Override
    public void uploadCustomer(Customer c){
        super.uploadCustomer(c);

        for(String token : tokenize(c.getName())){
            DatabaseReference ref = FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                    .child(DatabaseStructure.CustomerBranch.C_NAME).child(token);

        }
    }


    @Override
    public void findCustomersByName(String name, DataRetriever<LinkedList<Customer>> retriever) {
        FireDatabase.getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .orderByChild(DatabaseStructure.CustomerBranch.C_NAME)
                .startAt(name).endAt(name + " Z").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DataSnapshot snapshot = task.getResult();
                LinkedList<Customer> IDList = new LinkedList<>();

                if(snapshot.exists()){
                    for(DataSnapshot child : snapshot.getChildren()){
                        Customer c = child.getValue(Customer.class);
                        IDList.add(c);
                    }
                }

                retriever.retrievedData(IDList);
            }
        });
    }

    private LinkedList<String> tokenize(String name){
        Scanner scanner = new Scanner(name);

        LinkedList<String> tokens = new LinkedList<>();

        Metaphone codex = new Metaphone();

        while(scanner.hasNext()){
            tokens.add(codex.encode(scanner.next()));
        }

        return tokens;
    }
}
