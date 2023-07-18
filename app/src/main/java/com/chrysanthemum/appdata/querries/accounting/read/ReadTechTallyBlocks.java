package com.chrysanthemum.appdata.querries.accounting.read;

import com.chrysanthemum.appdata.Util.Counter;
import com.chrysanthemum.appdata.dataType.Amount;
import com.chrysanthemum.appdata.dataType.TechTallyBlock;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.TreeMap;

public class ReadTechTallyBlocks extends DBReadQuery<TreeMap<Long, TechTallyBlock>> {

    // <tech id, tally block>
    private final TreeMap<Long, TechTallyBlock> blocks = new TreeMap<>();

    private final LocalDate start;
    private final LinkedList<Technician> techs;
    private final int dayCount;

    public ReadTechTallyBlocks(LocalDate start, LocalDate end, LinkedList<Technician> techs) {
        this.start = start;
        this.techs = techs;
        dayCount = (int) ChronoUnit.DAYS.between(start, end.plusDays(1));
    }

    @Override
    protected void executeQuery() {
        for(Technician tech : techs){
            readTallyBlockOf(tech);
        }
    }

    private void readTallyBlockOf(final Technician tech){
        final TechTallyBlock block = new TechTallyBlock(tech.getColour());
        final Counter returnedCounter = new Counter();

        for(int i = 0; i < dayCount; i++){
            LocalDate date = start.plusDays(i);
            final int dateIndex = i;

            FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                    .child(date.getYear() + "").child(date.getMonthValue() + "").child(date.getDayOfMonth() + "")
                    .child(DatabaseStructure.Accounting.BRANCH_NAME).child("" + tech.getID()).get().addOnCompleteListener(task -> {

                        Amount a = new Amount();

                        if (task.isSuccessful()) {
                            DataSnapshot result = task.getResult();

                            assert result != null;
                            if(result.exists()){
                                a.add(result.child(DatabaseStructure.Accounting.A_AMOUNT).getValue(Integer.class),
                                        result.child(DatabaseStructure.Accounting.A_NO_TAX).getValue(Integer.class));
                            }
                        }

                        synchronized (returnedCounter) {
                            returnedCounter.increment();
                            block.add(dateIndex, a);

                            if(returnedCounter.getValue() == dayCount){
                                retrieveTechTallyBlock(tech, block);
                            }
                        }
                    });
        }
    }

    private synchronized void retrieveTechTallyBlock(Technician tech, TechTallyBlock b){
        blocks.put(tech.getID(), b);

        if(blocks.size() == techs.size()){
            returnQueryData(blocks);
        }
    }
}
