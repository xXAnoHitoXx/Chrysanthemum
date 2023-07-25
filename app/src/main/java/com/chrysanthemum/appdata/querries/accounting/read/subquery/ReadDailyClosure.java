package com.chrysanthemum.appdata.querries.accounting.read.subquery;

import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.DailyClosure;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.util.SubQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;

import java.time.LocalDate;

public class ReadDailyClosure implements SubQuery<DailyClosure> {

    private final LocalDate date;

    public ReadDailyClosure(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(DataRetriever<DailyClosure> retriever) {
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(date.getYear() + "").child(date.getMonthValue() + "").child(date.getDayOfMonth() + "")
                .child(DatabaseStructure.Accounting.BRANCH_NAME)
                .child(DatabaseStructure.Accounting.A_CLOSURE).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        retriever.retrievedData(task.getResult().getValue(DailyClosure.class));
                    }
                });
    }
}
