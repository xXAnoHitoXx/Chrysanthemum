package com.chrysanthemum.appdata.dataType;

import com.chrysanthemum.appdata.Tax;
import com.chrysanthemum.appdata.dataType.parsing.MoneyParser;
import com.chrysanthemum.appdata.dataType.subType.DailyClosure;

public class DailyTally {
    private DailyClosure close;
    private final long[] records;

    public DailyTally(DailyClosure close, long[] records){
        this.close = close;
        this.records = records;
    }

    public void add(DailyTally dailyTally){
        this.records[0] += dailyTally.records[0];
        this.records[1] += dailyTally.records[1];

        this.close.add(dailyTally.close);
    }

    private long getAmt(){
        return records[0];
    }

    private long getTp(){
        return records[1];
    }

    private long getTx(){
        return Tax.getTax(records[0]);
    }

    public String getAmount(){
        return MoneyParser.parseSingleAmount(getAmt());
    }

    public String getTip(){
        return MoneyParser.parseSingleAmount(getTp());
    }

    public String getTax(){
        return MoneyParser.parseSingleAmount(getTx());
    }

    public String getCash(){
        if(close == null){
            return "";
        }
        return MoneyParser.parseSingleAmount(close.getCash());
    }

    public String getMachine(){
        if(close == null){
            return "";
        }
        return MoneyParser.parseSingleAmount(close.getMachine());
    }

    public String getGift(){
        if(close == null){
            return "";
        }
        return MoneyParser.parseSingleAmount(close.getGift());
    }

    public String getDiff(){
        if(close == null){
            return "";
        }

        long diff = close.getCash() + close.getMachine() + close.getGift();
        diff = diff - getAmt() - getTp() - getTx();

        return MoneyParser.parseSingleAmount(diff);
    }

    public void updateClosure(DailyClosure closure){
        this.close = closure;
    }
}
