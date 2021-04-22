package com.chrysanthemum.appdata.RemoteDataBase;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import java.util.Map;

public interface RemoteDataBase {

    //-App Setup------------------------------------------------------------------------------------

    void getTechnicianMap(DataRetriever<Map<String, Technician>> retriever);

    //-Module Retrival------------------------------------------------------------------------------

    GiftCardManager getGiftCardManager();

    AccountingManager getAccountingManager();

    TransactionManager getTransactionManager();

    CustomerManager getCustomerManager();
}
