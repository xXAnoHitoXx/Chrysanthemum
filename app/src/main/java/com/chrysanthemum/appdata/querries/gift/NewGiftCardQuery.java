package com.chrysanthemum.appdata.querries.gift;

import com.chrysanthemum.appdata.dataType.Gift;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.Query;

public class NewGiftCardQuery extends Query<Gift> {
   private Gift gift;

    public NewGiftCardQuery(String id, String amount, String dateIssued, String dateExpires, DataRetriever<Gift> retriever) {
        super(retriever);
        gift = new Gift(id, amount, dateIssued, dateExpires);
    }

    @Override
    public void executeQuery() {
        getRemoteDB().getGiftCardManager().uploadGiftCard(gift);
        complete(gift);
    }
}
