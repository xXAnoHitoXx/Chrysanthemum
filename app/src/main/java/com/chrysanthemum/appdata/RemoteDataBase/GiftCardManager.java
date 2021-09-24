package com.chrysanthemum.appdata.RemoteDataBase;

import com.chrysanthemum.appdata.dataType.Gift;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;

public interface GiftCardManager {

    /**
     * upload a giftcard without creating accounting history
     */
    void uploadGiftCard(Gift gift);
    void findGiftCardByID(String id, DataRetriever<Gift> retriever);
    void editGift(Gift gift, String amount, String dateExpire);
}
