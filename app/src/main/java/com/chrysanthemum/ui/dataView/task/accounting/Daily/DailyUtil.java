package com.chrysanthemum.ui.dataView.task.accounting.Daily;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;

import java.util.List;

public class DailyUtil {

    public static void orderedByTime(List<Transaction> transactionList){
        transactionList.sort(TimeParser.getTimeBasedComparator());
    }

}
