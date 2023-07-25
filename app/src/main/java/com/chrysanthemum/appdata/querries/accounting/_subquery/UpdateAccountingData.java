package com.chrysanthemum.appdata.querries.accounting._subquery;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.DBUpdateQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.time.LocalDate;

public class UpdateAccountingData extends DBUpdateQuery<Void> {
    private final LocalDate date;
    private final String techID;
    private final int preT;
    private final int postT;

    public UpdateAccountingData(Transaction diff) {
        this.date = diff.getLocalDateAppointmentDate();
        this.techID = diff.getTech().getID() + "";
        this.preT = diff.getAmount();
        this.postT = diff.getTip();
    }

    @Override
    public void execute() {
        // yearly accounting
        DatabaseReference timeIndexRef = FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME).child(date.getYear() + "");
        updateStandardAccountingData(timeIndexRef);

        // monthly accounting
        timeIndexRef = timeIndexRef.child(date.getMonthValue() + "");
        updateStandardAccountingData(timeIndexRef);

        // daily accounting
        timeIndexRef = timeIndexRef.child(date.getDayOfMonth() + "");
        updateStandardAccountingData(timeIndexRef);
    }

    /**
     * update accounting data under the Accounting branch name
     */
    private void updateStandardAccountingData(DatabaseReference timeIndexRef) {
        // shop total
        updateAccountingData(timeIndexRef.child(DatabaseStructure.Accounting.SHOP_TOTAL));

        // technician earnings
        updateAccountingData(timeIndexRef.child(techID));
    }

    /**
     * update the Account amount at a location
     */
    private void updateAccountingData(DatabaseReference location){
        location.child(DatabaseStructure.Accounting.A_AMOUNT).setValue(ServerValue.increment(preT));
        location.child(DatabaseStructure.Accounting.A_NO_TAX).setValue(ServerValue.increment(postT));
    }

}
