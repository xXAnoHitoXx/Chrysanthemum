package com.chrysanthemum.appdata.RemoteDataBase;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;

import java.util.LinkedList;

public interface TransactionManager {

    void uploadSaleRecord(Transaction transaction);

    void findTransactionByID(long id, DataRetriever<TransactionFrame> retriever);

    void findOpenTransactionIDsByDate(String date, DataRetriever<LinkedList<Long>> retriever);
    void findTransactionIDsByCustomerID(long id, DataRetriever<LinkedList<Long>> retriever);
    void findTransactionIDsByDateAndTechnicianID(String date, long id, DataRetriever<LinkedList<Long>> retriever);

}
