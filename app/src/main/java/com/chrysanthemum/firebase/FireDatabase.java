package com.chrysanthemum.firebase;

import com.chrysanthemum.appdata.RemoteDataBase.AccountingManager;
import com.chrysanthemum.appdata.RemoteDataBase.RemoteDataBase;
import com.chrysanthemum.appdata.RemoteDataBase.TransactionManager;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.firebase.subModules.AccountingManagerModule;
import com.chrysanthemum.firebase.subModules.TransactionManagerModule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.TreeMap;

/**
 * provide connection to firebase database
 * handle data query
 */
public class FireDatabase implements RemoteDataBase {

    public void initialization(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public SecurityModule generateSecurityModule(){
        return new LoginRepository();
    }


    //-App Setup------------------------------------------------------------------------------------

    public void getTechnicianMap(final DataRetriever<Map<String, Technician>> retriever){
        getRef().child(DatabaseStructure.TechnicianBranch.BRANCH_NAME)
                .child(DatabaseStructure.TechnicianBranch.LIST).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();

                        Map<String, Technician> m = new TreeMap<>();

                        for(DataSnapshot child : snapshot.getChildren()){
                            Technician i = child.getValue(Technician.class);
                            m.put(i.getID()+ "", i);
                        }

                        retriever.retrievedData(m);
                    }
                });
    }

    //-Module Retrival------------------------------------------------------------------------------

    @Override
    public AccountingManager getAccountingManager() {
        return new AccountingManagerModule();
    }

    @Override
    public TransactionManager getTransactionManager() {
        return new TransactionManagerModule(getAccountingManager());
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
