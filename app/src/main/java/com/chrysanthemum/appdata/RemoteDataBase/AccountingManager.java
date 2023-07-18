package com.chrysanthemum.appdata.RemoteDataBase;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.DailyClosure;
import com.chrysanthemum.appdata.dataType.subType.MonthTallyEntry;
import com.chrysanthemum.ui.dataView.task.accounting.Cal.Amount;

import java.time.LocalDate;

public interface AccountingManager {

    void closeDate(LocalDate date, DailyClosure closure);
    void updateAccountingData(Transaction transaction);

    void findTechTally(LocalDate date, long techID, DataRetriever<Amount> retriever);
}
