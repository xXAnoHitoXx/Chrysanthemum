package com.chrysanthemum.appdata.querries._old.accounting;

import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.MonthTallyEntry;
import com.chrysanthemum.appdata.querries._old.Query;

import java.time.LocalDate;

public class LoadMonthTallyEntryQuery extends Query<MonthTallyEntry> {
    private final LocalDate date;

    public LoadMonthTallyEntryQuery(LocalDate date, DataRetriever<MonthTallyEntry> retriever) {
        super(retriever);
        this.date = date;
    }

    @Override
    public void executeQuery() {
        getRemoteDB().getAccountingManager().findClosurelessMonthTallyEntryByDate(date, monthTallyEntry -> {

            if(monthTallyEntry != null) {
                getRemoteDB().getAccountingManager().findClosureByDate(date, dailyClosure -> {

                    monthTallyEntry.attachClosing(dailyClosure);
                    complete(monthTallyEntry);
                });
            }
        });
    }
}
