package com.chrysanthemum.firebase.subModules;

import com.chrysanthemum.appdata.RemoteDataBase.GiftCardManager;
import com.chrysanthemum.appdata.dataType.Gift;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;

import java.util.Scanner;

public class GiftCardManagerModule implements GiftCardManager {

    @Override
    public void uploadGiftCard(Gift gift) {

        // store by id
        FireDatabase.getRef().child(DatabaseStructure.Gift.BRANCH_NAME).child(gift.getId()).setValue(gift);

        //indexed by expire date
        Scanner scanner = new Scanner(gift.getDateExpires());
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(year).child(month).child(day)
                .child(DatabaseStructure.Gift.INDEX_BRANCH)
                .child(gift.getId()).setValue(gift.getId());
    }

    @Override
    public void findGiftCardByID(String id, final DataRetriever<Gift> retriever) {
        FireDatabase.getRef().child(DatabaseStructure.Gift.BRANCH_NAME)
                .child(id).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Gift g = task.getResult().getValue(Gift.class);
                        retriever.retrievedData(g);
                    }
                });
    }

    @Override
    public void editGift(Gift gift, String amount, String dateExpire) {
        if(!gift.getDateExpires().equalsIgnoreCase(dateExpire)){
            //indexed by expire date
            Scanner scanner = new Scanner(gift.getDateExpires());
            String day = scanner.next();
            String month = scanner.next();
            String year = scanner.next();
            scanner.close();

            FireDatabase.getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                    .child(year).child(month).child(day)
                    .child(DatabaseStructure.Gift.INDEX_BRANCH)
                    .child(gift.getId()).removeValue();
        }

        gift.setAmount(amount);
        gift.setDateExpires(dateExpire);

        uploadGiftCard(gift);
    }

}
