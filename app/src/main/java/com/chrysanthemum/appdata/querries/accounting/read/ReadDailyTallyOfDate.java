package com.chrysanthemum.appdata.querries.accounting.read;

import com.chrysanthemum.appdata.dataType.DailyTally;
import com.chrysanthemum.appdata.dataType.subType.DailyClosure;
import com.chrysanthemum.appdata.querries.DBQuery;
import com.chrysanthemum.appdata.querries.accounting.read.subquery.ReadDailyClosure;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;

import java.time.LocalDate;

public class ReadDailyTallyOfDate extends DBQuery<DailyTally> {
    private final LocalDate date;

    public ReadDailyTallyOfDate(LocalDate date){
        this.date = date;
    }

    @Override
    protected void executeQuery() {
        findAccountingRecordByDate(task -> {
            if(task.isSuccessful()){
                DataSnapshot result = task.getResult();
                long preTax = result.child(DatabaseStructure.Accounting.A_AMOUNT).exists() ? result.child(DatabaseStructure.Accounting.A_AMOUNT).getValue(Long.class) : 0;
                long postTax = result.child(DatabaseStructure.Accounting.A_NO_TAX).exists() ? result.child(DatabaseStructure.Accounting.A_NO_TAX).getValue(Long.class) : 0;

                long[] amount_tip = new long[] {preTax, postTax};

                DBQuery<DailyClosure> readClosureQuery = new ReadDailyClosure(date);
                DailyClosure close = readClosureQuery.execute();

                setData(new DailyTally(close, amount_tip));
            }
        });
    }

    private void findAccountingRecordByDate(OnCompleteListener<DataSnapshot> onCompletionListener) {
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(date.getYear() + "").child(date.getMonthValue() + "").child(date.getDayOfMonth() + "")
                .child(DatabaseStructure.Accounting.BRANCH_NAME)
                .child(DatabaseStructure.Accounting.SHOP_TOTAL).get().addOnCompleteListener(onCompletionListener);
    }


}
