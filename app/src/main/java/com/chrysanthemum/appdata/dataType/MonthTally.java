package com.chrysanthemum.appdata.dataType;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.Util.CSVBuilder;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.subType.MonthTallyEntry;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class MonthTally {

    String title;

    Map<LocalDate, MonthTallyEntry> month;
    MonthTallyEntry firstHalf = new MonthTallyEntry("First Half");
    MonthTallyEntry secondHalf = new MonthTallyEntry("Second Half");

    public MonthTally(String title){
        this.title = title;
        month = new TreeMap<>();
    }

    public void addEntry(MonthTallyEntry m){
        month.put(TimeParser.parseDate(m.getLabel()), m);

        LocalDate date = TimeParser.parseDate(m.getLabel());

        if(date.getDayOfMonth() < 16){
            firstHalf.add(m);
        } else {
            secondHalf.add(m);
        }
    }

    public String getTitle(){
        return title;
    }

    public int size(){
        return month.size();
    }

    public String generateFileData(){
        Map<Long, Integer> techPos = allocateTechPos();

        CSVBuilder csv = new CSVBuilder(title);

        csv.append("Date");

        for(long id : techPos.keySet()){
            Technician t = DataStorageModule.getFrontEnd().getTech(id);

            csv.append(t.getName());
            csv.append("");
        }

        csv.append("Sales");

        csv.append("Total");
        csv.append("Tip");
        csv.append("Tax");

        csv.append("Cash");
        csv.append("Machine");
        csv.append("Gift");
        csv.append("Diff");

        csv.newLine();

        for(LocalDate label : month.keySet()){
            MonthTallyEntry e = month.get(label);

            if(e.hasData()){
                addEntryToCSV(csv, e, techPos);
            }
        }

        addEntryToCSV(csv, firstHalf, techPos);
        addEntryToCSV(csv, secondHalf, techPos);

        MonthTallyEntry monthTotal = new MonthTallyEntry("Month Total");
        monthTotal.add(firstHalf);
        monthTotal.add(secondHalf);

        addEntryToCSV(csv, monthTotal, techPos);

        return csv.toString();
    }

    private void addEntryToCSV(CSVBuilder csv, MonthTallyEntry e, Map<Long, Integer> techPos){

        for(String data : e.getData(techPos)){
            csv.append(data);
        }

        csv.newLine();
    }

    private Map<Long, Integer> allocateTechPos(){
        Map<Long, Integer> map = new TreeMap<>();

        for(Technician t : DataStorageModule.getFrontEnd().getTechList()){
            boolean hasTech = firstHalf.hasTech(t.getID())
                    || secondHalf.hasTech(t.getID());

            if(hasTech){
                map.put(t.getID(), (2 * map.size()) + 1);
            }
        }

        return map;
    }
}
