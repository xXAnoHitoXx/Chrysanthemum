package com.chrysanthemum.appdata.querries.accounting.read;

import com.chrysanthemum.appdata.dataType.MonthTally;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.subType.DailyClosure;
import com.chrysanthemum.appdata.dataType.subType.MonthTallyEntry;
import com.chrysanthemum.appdata.querries.DBQuery;
import com.chrysanthemum.appdata.querries.accounting.read.subquery.ReadDailyClosure;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;

import java.time.LocalDate;

public class ReadMonthTally extends DBQuery<MonthTally> {

    private final LocalDate startOfMonth;
    private final MonthTally mt;

    public ReadMonthTally(String title, int year, int month) {
        this.startOfMonth = LocalDate.of(year, month, 1);
        this.mt = new MonthTally(title);

        setTimeOutInMilliseconds(20000);
    }

    @Override
    protected void executeQuery() {
        for(int i = 0; i < startOfMonth.lengthOfMonth(); i++){
            readEntryOfDate(startOfMonth.plusDays(i));
        }
    }

    private void readEntryOfDate(LocalDate date){

        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(date.getYear() + "").child(date.getMonthValue() + "").child(date.getDayOfMonth() + "")
                .child(DatabaseStructure.Accounting.BRANCH_NAME).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot result = task.getResult();

                        MonthTallyEntry entry = new MonthTallyEntry(TimeParser.parseDateData(date));

                        for (DataSnapshot child: result.getChildren()) {
                            String key = child.getKey();

                            if(key.matches("-?\\d+(\\.\\d+)?")){
                                long techID = Long.parseLong(key);

                                long[] techPays = new long[]{
                                        child.child(DatabaseStructure.Accounting.A_AMOUNT).getValue(Long.class),
                                        child.child(DatabaseStructure.Accounting.A_NO_TAX).getValue(Long.class)
                                };

                                entry.addPay(techID, techPays);
                            }

                        }

                        DBQuery<DailyClosure> readClosureQuery = new ReadDailyClosure(date);
                        DailyClosure close = readClosureQuery.execute();

                        entry.attachClosing(close);
                        retrievedEntry(entry);
                    }
                });
    }

    private synchronized void retrievedEntry(MonthTallyEntry entry){
        mt.addEntry(entry);

        if(mt.size() >= startOfMonth.lengthOfMonth()){
            setData(mt);
        }
    }
}
