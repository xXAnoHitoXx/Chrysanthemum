package com.chrysanthemum.appdata.querries.accounting;

import com.chrysanthemum.appdata.dataType.DailyTally;
import com.chrysanthemum.appdata.dataType.subType.DailyClosure;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.Query;

import java.time.LocalDate;

public class LoadDailyTallyOfDateQuery extends Query<DailyTally> {
    private final LocalDate date;
    private long[] at;

    public LoadDailyTallyOfDateQuery(LocalDate date, DataRetriever<DailyTally> retriever) {
        super(retriever);
        this.date = date;
    }

    @Override
    public void executeQuery() {
        loadAT();
    }

    private void loadAT(){
        getRemoteDB().getAccountingManager().findAccountingRecordByDate(date, data -> {
            at = data;
            loadClosure();
        });
    }

    private void loadClosure(){
        getRemoteDB().getAccountingManager().findClosureByDate(date, data -> complete(new DailyTally(data, at)));
    }
}
