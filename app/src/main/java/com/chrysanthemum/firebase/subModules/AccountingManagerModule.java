package com.chrysanthemum.firebase.subModules;

import com.chrysanthemum.appdata.RemoteDataBase.AccountingManager;
import com.chrysanthemum.appdata.Util.AppUtil;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.DailyClosure;
import com.chrysanthemum.appdata.dataType.subType.MonthTallyEntry;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.time.LocalDate;

public class AccountingManagerModule implements AccountingManager {

    @Override
    public void closeDate(LocalDate date, DailyClosure closure) {
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(date.getYear() + "").child(date.getMonthValue() + "").child(date.getDayOfMonth() + "")
                .child(DatabaseStructure.Accounting.BRANCH_NAME)
                .child(DatabaseStructure.Accounting.A_CLOSURE).setValue(closure);
    }

    @Override
    public void updateAccountingData(Transaction transaction) {
        updateWeeklyAccountingData(transaction.getLocalDateAppointmentDate(), transaction.getTech().getID()+ "", transaction.getAmount(), transaction.getTip());
        updateMonthlyAccountingData(transaction.getLocalDateAppointmentDate(), transaction.getTech().getID()+ "", transaction.getAmount(), transaction.getTip());
    }


    @Override
    public void findClosureByDate(LocalDate date, DataRetriever<DailyClosure> retriever) {
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(date.getYear() + "").child(date.getMonthValue() + "").child(date.getDayOfMonth() + "")
                .child(DatabaseStructure.Accounting.BRANCH_NAME)
                .child(DatabaseStructure.Accounting.A_CLOSURE).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        retriever.retrievedData(task.getResult().getValue(DailyClosure.class));
                    }
                });
    }

    @Override
    public void findAccountingRecordByDate(LocalDate date, DataRetriever<long[]> retriever) {
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(date.getYear() + "").child(date.getMonthValue() + "").child(date.getDayOfMonth() + "")
                .child(DatabaseStructure.Accounting.BRANCH_NAME)
                .child(DatabaseStructure.Accounting.SHOP_TOTAL).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot result = task.getResult();
                        long preTax = result.child(DatabaseStructure.Accounting.A_AMOUNT).exists() ? result.child(DatabaseStructure.Accounting.A_AMOUNT).getValue(Long.class) : 0;
                        long postTax = result.child(DatabaseStructure.Accounting.A_NO_TAX).exists() ? result.child(DatabaseStructure.Accounting.A_NO_TAX).getValue(Long.class) : 0;

                        retriever.retrievedData(new long[] {preTax, postTax});
                    }
                });
    }

    @Override
    public void findClosurelessMonthTallyEntryByDate(LocalDate date, DataRetriever<MonthTallyEntry> retriever) {
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

                        retriever.retrievedData(entry);
                    }
                });
    }

    /**
     * update monthly accounting data for the average item
     */
    private void updateMonthlyAccountingData(LocalDate date, String subBranch, int pretax, int postTax){

        // yearly accounting
        DatabaseReference timeIndexRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME).child(date.getYear() + "");
        updateStandardAccountingData(timeIndexRef, subBranch, pretax, postTax);

        // monthly accounting
        timeIndexRef = timeIndexRef.child(date.getMonthValue() + "");
        updateStandardAccountingData(timeIndexRef, subBranch, pretax, postTax);

        // daily accounting
        timeIndexRef = timeIndexRef.child(date.getDayOfMonth() + "");
        updateStandardAccountingData(timeIndexRef, subBranch, pretax, postTax);
    }


    /**
     * update weekly accounting data
     */
    private void updateWeeklyAccountingData(LocalDate date, String subBranch, int pretax, int postTax){

        LocalDate startOfWeek = AppUtil.getMonday(date);

        DatabaseReference weekRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(startOfWeek.getYear() + "").child(startOfWeek.getMonthValue() + "").child(startOfWeek.getDayOfMonth() + "")
                .child(DatabaseStructure.Accounting.BRANCH_NAME).child(DatabaseStructure.Accounting.WEEKLY_TOTALS);

        updateSubAccountingData(weekRef, subBranch, pretax, postTax);

    }
    /**
     * update accounting data under the Accounting branch name
     */
    private void updateStandardAccountingData(DatabaseReference timeIndexRef, String subBranch, int pretax, int postTax) {
        updateSubAccountingData(timeIndexRef.child(DatabaseStructure.Accounting.BRANCH_NAME),
                subBranch, pretax, postTax);
    }

    /**
     * update the shop total and a sub branch (tech id or sale)
     */
    private void updateSubAccountingData(DatabaseReference timeIndexRef, String subBranch, int pretax, int postTax){
        // shop total
        updateAccountingData(timeIndexRef.child(DatabaseStructure.Accounting.SHOP_TOTAL), pretax, postTax);

        // technician earnings
        updateAccountingData(timeIndexRef.child(subBranch), pretax, postTax);
    }

    /**
     * update the Account amount at a location
     */
    private void updateAccountingData(DatabaseReference location, int pretax, int postTax){
        location.child(DatabaseStructure.Accounting.A_AMOUNT).setValue(ServerValue.increment(pretax));
        location.child(DatabaseStructure.Accounting.A_NO_TAX).setValue(ServerValue.increment(postTax));
    }

}
