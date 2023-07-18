package com.chrysanthemum.appdata.querries._old.accounting;

import com.chrysanthemum.appdata.dataType.DailyTally;
import com.chrysanthemum.appdata.dataType.subType.DailyClosure;
import com.chrysanthemum.appdata.querries._old.InstantQuery;

import java.time.LocalDate;

public class UpdateClosingDataQuery extends InstantQuery<DailyTally> {

    private final DailyTally tally;
    private final long cash;
    private final long machine;
    private final long gift;
    private final long discount;
    private final LocalDate date;

    public UpdateClosingDataQuery(long cash, long machine, long gift, long discount, LocalDate date, DailyTally tally){
        this.cash = cash;
        this.machine = machine;
        this.tally = tally;
        this.date = date;
        this.gift = gift;
        this.discount = discount;
    }

    @Override
    public DailyTally executeQuery() {
        DailyClosure closure = new DailyClosure(cash, machine, gift, discount);
        getRemoteDB().getAccountingManager().closeDate(date, closure);
        tally.updateClosure(closure);
        return tally;
    }
}
