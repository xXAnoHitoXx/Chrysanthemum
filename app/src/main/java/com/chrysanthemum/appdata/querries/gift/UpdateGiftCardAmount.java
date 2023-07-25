package com.chrysanthemum.appdata.querries.gift;

import com.chrysanthemum.appdata.dataType.Gift;
import com.chrysanthemum.appdata.querries.DBUpdateQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;

public class UpdateGiftCardAmount extends DBUpdateQuery<Gift> {

    private final Gift gift;
    private final String amount;

    public UpdateGiftCardAmount(Gift gift, String amount) {
        this.gift = gift;
        this.amount = amount;
    }

    @Override
    public void execute() {
        // store by id
        FireDatabase.getRef().child(DatabaseStructure.Gift.BRANCH_NAME).child(gift.getId()).setValue(gift);
        gift.setAmount(amount);
    }
}
