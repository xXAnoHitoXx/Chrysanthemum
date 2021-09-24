package com.chrysanthemum.appdata.RemoteDataBase;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;

import java.util.LinkedList;

public interface TransactionManager {

    void uploadTransaction(Transaction transaction);
    void uploadSaleRecord(Transaction transaction);

    void findTransactionByID(long id, DataRetriever<TransactionFrame> retriever);

    void findOpenTransactionIDsByDate(String date, DataRetriever<LinkedList<Long>> retriever);
    void findTransactionIDsByCustomerID(long id, DataRetriever<LinkedList<Long>> retriever);
    void findTransactionIDsByDateAndTechnicianID(String date, long id, DataRetriever<LinkedList<Long>> retriever);

    void markNoShow(Transaction transaction);
    void attachTransactionTech(Transaction transaction);
    void closeTransaction(Transaction transaction);
    void editRecord(Transaction transaction, int amount, int tip, String service);

    void removeAppointment(Transaction e);
}
