package com.chrysanthemum.appdata.querries._old.accounting;

import com.chrysanthemum.appdata.dataType.MonthTally;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.MonthTallyEntry;
import com.chrysanthemum.appdata.querries._old.Query;

import java.time.LocalDate;

public class LoadMonthTallyQuery extends Query<MonthTally> {

    private final LocalDate date;
    private final MonthTally mt;

    public LoadMonthTallyQuery(String title, int year, int month, DataRetriever<MonthTally> retriever) {
        super(retriever);

        date = LocalDate.of(year, month, 1);

        mt = new MonthTally(title);
    }

    @Override
    public void executeQuery() {
        retrieveEntrySubQuery(date);
    }

    private void retrieveEntrySubQuery(LocalDate date){
        LoadMonthTallyEntryQuery q = new LoadMonthTallyEntryQuery(date, data -> retrievedSubQueryData(date, data));

        q.executeQuery();
    }

    private synchronized void retrievedSubQueryData(LocalDate date, MonthTallyEntry entry){
        mt.addEntry(entry);

        if(date.getDayOfMonth() < date.lengthOfMonth()){
            retrieveEntrySubQuery(date.plusDays(1));
        } else if(date.getDayOfMonth() == date.lengthOfMonth()){
            complete(mt);
        }
    }
}
