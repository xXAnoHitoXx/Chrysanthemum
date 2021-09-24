package com.chrysanthemum.appdata.querries.gift;

import com.chrysanthemum.appdata.dataType.Gift;
import com.chrysanthemum.appdata.querries.InstantQuery;

public class EditGiftDataQuery extends InstantQuery<Gift> {

    private final Gift gift;
    private final String amount;
    private final String dateExpires;

    public EditGiftDataQuery(Gift gift, String amount, String dateExpires){
        this.gift = gift;
        this.amount = amount;

        this.dateExpires = dateExpires;
    }

    @Override
    public Gift executeQuery() {
        getRemoteDB().getGiftCardManager().editGift(gift, amount, dateExpires);
        return gift;
    }
}
