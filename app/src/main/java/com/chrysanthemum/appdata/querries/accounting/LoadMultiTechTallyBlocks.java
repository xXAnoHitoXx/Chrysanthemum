package com.chrysanthemum.appdata.querries.accounting;

import com.chrysanthemum.appdata.dataType.TechTallyBlock;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.Query;
import com.chrysanthemum.appdata.querries.SubQueryManager;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.TreeMap;

public class LoadMultiTechTallyBlocks extends Query<TreeMap<Long, TechTallyBlock>> {

    final LocalDate Start;
    final LocalDate End;
    final LinkedList<Technician> techs;

    public LoadMultiTechTallyBlocks(LocalDate dayA, LocalDate dayB, LinkedList<Technician> techs, DataRetriever<TreeMap<Long, TechTallyBlock>> retriever) {
        super(retriever);

        this.Start = (dayA.compareTo(dayB) < 0)? dayA : dayB;
        this.End = (dayA.compareTo(dayB) < 0)? dayB : dayA;
        this.techs = techs;
    }

    @Override
    public void executeQuery() {

        TreeMap<Long, TechTallyBlock> m = new TreeMap<>();

        SubQueryManager<TechTallyBlock> dom = new SubQueryManager<>(techs.size(), data -> {});

        for(Technician tech : techs){
            Query<TechTallyBlock> sub = new LoadTechTallyBlockQuery(Start, End,
                    tech, data -> {
                m.put(tech.getID(), data);
                dom.retrievedData(data);
                if(dom.isCompleted()){
                    complete(m);
                }
            });

            sub.executeQuery();
        }
    }
}
