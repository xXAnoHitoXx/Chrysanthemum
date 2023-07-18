package com.chrysanthemum.firebase.subModules;

import com.chrysanthemum.appdata.RemoteDataBase.AccountingManager;
import com.chrysanthemum.appdata.Util.AppUtil;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.DailyClosure;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.chrysanthemum.ui.dataView.task.accounting.Cal.Amount;
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
    public void findTechTally(LocalDate date, long techID, DataRetriever<Amount> retriever) {
        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(date.getYear() + "").child(date.getMonthValue() + "").child(date.getDayOfMonth() + "")
                .child(DatabaseStructure.Accounting.BRANCH_NAME).child("" + techID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot result = task.getResult();

                Amount a = new Amount();
                assert result != null;
                if(result.exists()){
                    a.add(result.child(DatabaseStructure.Accounting.A_AMOUNT).getValue(Integer.class),
                            result.child(DatabaseStructure.Accounting.A_NO_TAX).getValue(Integer.class));
                }
                retriever.retrievedData(a);
            } else {
                retriever.retrievedData(null);
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
