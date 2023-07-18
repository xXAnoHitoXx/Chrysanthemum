package com.chrysanthemum.appdata.querries.accounting.update;

import com.chrysanthemum.appdata.dataType.DailyTally;
import com.chrysanthemum.appdata.dataType.subType.DailyClosure;
import com.chrysanthemum.appdata.querries.DBWriteQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;

import java.time.LocalDate;

public class UpdateClosingData extends DBWriteQuery<DailyTally> {

    private final DailyTally tally;
    private final long cash;
    private final long machine;
    private final long gift;
    private final long discount;
    private final LocalDate date;

    public UpdateClosingData(long cash, long machine, long gift, long discount, LocalDate date, DailyTally tally){
        this.cash = cash;
        this.machine = machine;
        this.tally = tally;
        this.date = date;
        this.gift = gift;
        this.discount = discount;
    }

    @Override
    public DailyTally execute() {
        DailyClosure closure = new DailyClosure(cash, machine, gift, discount);

        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(date.getYear() + "").child(date.getMonthValue() + "").child(date.getDayOfMonth() + "")
                .child(DatabaseStructure.Accounting.BRANCH_NAME)
                .child(DatabaseStructure.Accounting.A_CLOSURE).setValue(closure);

        tally.updateClosure(closure);
        return tally;
    }
}
