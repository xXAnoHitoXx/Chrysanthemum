package com.chrysanthemum.appdata.querries.gift;

import com.chrysanthemum.appdata.dataType.Gift;
import com.chrysanthemum.appdata.querries.DBCreateQuery;
import com.chrysanthemum.firebase.DatabaseStructure;
import com.chrysanthemum.firebase.FireDatabase;

import java.util.Scanner;

public class CreateGiftCard extends DBCreateQuery<Gift> {

    private final Gift gift;

    public CreateGiftCard(String id, String amount, String dateIssued, String dateExpires) {
        this.gift = new Gift(id,amount,dateIssued,dateExpires);
    }

    @Override
    public Gift execute() {
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

        return gift;
    }
}
