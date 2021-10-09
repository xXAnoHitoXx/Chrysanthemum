package com.chrysanthemum.appdata.dataType.subType;

import com.chrysanthemum.appdata.Tax;
import com.chrysanthemum.appdata.dataType.DailyTally;
import com.chrysanthemum.appdata.dataType.parsing.MoneyParser;
import com.chrysanthemum.firebase.DatabaseStructure;

import java.util.Map;
import java.util.TreeMap;

public class MonthTallyEntry {
    String label;
    Map<Long, long[]> techsPay;
    DailyTally tally;

    public MonthTallyEntry(String label) {
        this.label = label;
        techsPay = new TreeMap<>();
        tally = new DailyTally(new DailyClosure(0, 0, 0), new long[]{0, 0});
    }

    public boolean hasData(){
        return !techsPay.isEmpty();
    }

    public void addPay(long id, long[] techPay){
        techsPay.put(id, techPay);
        if(id == DatabaseStructure.Accounting.SALE_TECH.getID()){

            long saleAmount = Tax.getPreTax(techPay[1]);
            saleAmount += techPay[0];

            long[] salesFix = new long[]{saleAmount, 0};

            tally.add(new DailyTally(new DailyClosure(), salesFix));
        } else {
            tally.add(new DailyTally(new DailyClosure(), techPay));
        }
    }

    public void attachClosing(DailyClosure close){
        if(close != null){
            tally.add(new DailyTally(close, new long[]{0, 0}));
        }
    }

    public void add(MonthTallyEntry e){
        for(long id : e.techsPay.keySet()){
            long[] pays = new long[]{0, 0};

            if(this.techsPay.containsKey(id)){
                long[] tpays = this.techsPay.get(id);
                long[] epays = e.techsPay.get(id);

                pays[0] = tpays[0] + epays[0];
                pays[1] = tpays[1] +  epays[1];
            } else {
                long[] epays = e.techsPay.get(id);

                pays[0] = epays[0];
                pays[1] = epays[1];
            }

            this.techsPay.put(id, pays);
        }

        this.tally.add(e.tally);
    }

    public String getLabel(){
        return label;
    }

    public boolean hasTech(long id){
        return techsPay.containsKey(id);
    }

    public String[] getData(Map<Long, Integer> techPos){
        String[] data = new String[2 * techPos.size() + 9];

        data[0] = label;
        data[data.length - 1] = tally.getDiff();
        data[data.length - 2] = tally.getGift();
        data[data.length - 3] = tally.getMachine();
        data[data.length - 4] = tally.getCash();
        data[data.length - 5] = tally.getTax();
        data[data.length - 6] = tally.getTip();
        data[data.length - 7] = tally.getAmount();

        if(techsPay.containsKey(DatabaseStructure.Accounting.SALE_TECH.getID())) {
            long[] sales = techsPay.get(DatabaseStructure.Accounting.SALE_TECH.getID());
            data[data.length - 8] = (sales[0] + sales[1]) + "";
        } else {
            data[data.length - 8] = "0";
        }

        for(long id : techPos.keySet()){
            if(id != DatabaseStructure.Accounting.SALE_TECH.getID()){
                long[] pays;

                if(techsPay.containsKey(id)){
                    pays = techsPay.get(id);
                } else {
                    pays = new long[]{0,0};
                }

                int pos = techPos.get(id);

                data[pos] = MoneyParser.parseSingleAmount(pays[0]);
                data[pos + 1] = MoneyParser.parseSingleAmount(pays[1]);
            }
        }

        return data;
    }
}
