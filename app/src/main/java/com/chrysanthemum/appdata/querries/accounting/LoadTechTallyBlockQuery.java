package com.chrysanthemum.appdata.querries.accounting;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.TechTallyBlock;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.Query;
import com.chrysanthemum.appdata.querries.SubQueryManager;
import com.chrysanthemum.ui.dataView.task.accounting.Cal.Amount;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LoadTechTallyBlockQuery extends Query<TechTallyBlock> {

    private final LocalDate start;
    private final int dayCount;
    private final SubQueryManager<Amount> dom;
    private final Technician tech;

    public LoadTechTallyBlockQuery(LocalDate start, LocalDate end, Technician tech, DataRetriever<TechTallyBlock> retriever) {
        super(retriever);

        this.start = start;
        this.tech = tech;
        dayCount = (int) ChronoUnit.DAYS.between(start, end.plusDays(1));

        dom = new SubQueryManager<>(dayCount, data -> {});
    }

    @Override
    public void executeQuery() {
        final TechTallyBlock block = new TechTallyBlock(tech.getColour());

        for(int i = 0; i < dayCount; i++){
            LocalDate date = start.plusDays(i);

            final int fini = i;

            DataStorageModule.getRemoteDataBaseModule()
                    .getAccountingManager().findTechTally(date, tech.getID(), data -> {
                        if(data != null){
                            synchronized(block){
                                block.add(fini, data);
                            }
                        }

                        dom.retrievedData(data);

                        if(dom.isCompleted()){
                            complete(block);
                        }
                    });
        }
    }
}
