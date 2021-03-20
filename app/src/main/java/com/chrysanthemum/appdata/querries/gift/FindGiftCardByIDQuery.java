package com.chrysanthemum.appdata.querries.gift;

import com.chrysanthemum.appdata.dataType.Gift;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.Query;

public class FindGiftCardByIDQuery extends Query<Gift> {

    private final String id;
    private final DataRetriever<Gift> retriever;

    public FindGiftCardByIDQuery(String id, DataRetriever<Gift> retriever) {
        super(retriever);
        this.retriever = retriever;
        this.id = id;
    }

    @Override
    public void executeQuery() {
        getRemoteDB().findGiftCardByID(id, retriever);
    }
}
