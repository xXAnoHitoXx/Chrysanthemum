package com.chrysanthemum.appdata;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Gift;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;

import java.util.LinkedList;
import java.util.Map;

public interface RemoteDataBase {

    //-App Setup----------------------------------------------------------------------------------

    void getTechnicianMap(DataRetriever<Map<String, Technician>> retriever);

    //-Create New Entries---------------------------------------------------------------------------

    void uploadCustomer(Customer c);

    void uploadTransaction(Transaction transaction);

    /**
     * upload a giftcard without creating accounting history
     */
    void uploadGiftCard(Gift gift);

    void uploadSaleRecord(Transaction transaction);

    // void uploadSale();

    //-Entries Search-------------------------------------------------------------------------------

    void findCustomerByID(long id, DataRetriever<Customer> retriever);

    void findTransactionByID(long id, DataRetriever<TransactionFrame> retriever);

    void findGiftCardByID(String id, DataRetriever<Gift> retriever);

    //-Edit Entries---------------------------------------------------------------------------------

    void changeCustomerName(Customer customer, String name);
    void changeCustomerPhoneNumber(Customer customer, long phone);

    void markNoShow(Transaction transaction);
    void attachTransactionTech(Transaction transaction);
    void closeTransaction(Transaction transaction);
    void editRecord(Transaction transaction, int amount, int tip, String service);

    void editGift(Gift gift, String amount, String dateExpire);

    //-Index Search---------------------------------------------------------------------------------

    void findCustomerIDsByPhone(long phoneNumber, DataRetriever<LinkedList<Long>> retriever);

    void findOpenTransactionIDsByDate(String date, DataRetriever<LinkedList<Long>> retriever);
    void findTransactionIDsByCustomerID(long id, DataRetriever<LinkedList<Long>> retriever);
    void findTransactionIDsByDateAndTechnicianID(String date, long id, DataRetriever<LinkedList<Long>> retriever);

}
