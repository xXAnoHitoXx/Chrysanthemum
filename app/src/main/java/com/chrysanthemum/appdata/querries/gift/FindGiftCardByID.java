package com.chrysanthemum.appdata.querries.gift;

import com.chrysanthemum.appdata.dataType.Gift;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;

public class FindGiftCardByID extends DBReadQuery<Gift> {

    private final String id;

    public FindGiftCardByID(String id) {
        this.id = id;
    }

    @Override
    protected void executeQuery() {
        FireDatabase.getRef().child(DatabaseStructure.Gift.BRANCH_NAME)
                .child(id).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Gift g = task.getResult().getValue(Gift.class);
                        returnQueryData(g);
                    }
                });
    }
}
