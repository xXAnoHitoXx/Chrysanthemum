package com.chrysanthemum.firebase;

import androidx.annotation.NonNull;

import com.chrysanthemum.appdata.RemoteDataBase;
import com.chrysanthemum.appdata.Util.AppUtil;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Gift;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.dataType.subType.TransactionFrame;
import com.chrysanthemum.appdata.security.SecurityModule;
import com.chrysanthemum.appdata.dataType.Technician;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * provide connection to firebase database
 * handle data query
 */
public class FireDatabase implements RemoteDataBase {

    public void initialization(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public SecurityModule generateSecurityModule(){
        return new LoginRepository();
    }

    public void getTechnicianMap(final DataRetriever<Map<String, Technician>> retriever){
        getRef().child(DatabaseStructure.TechnicianBranch.BRANCH_NAME)
                .child(DatabaseStructure.TechnicianBranch.LIST).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    Map<String, Technician> m = new TreeMap<>();

                    for(DataSnapshot child : snapshot.getChildren()){
                        Technician i = child.getValue(Technician.class);
                        m.put(i.getID()+ "", i);
                    }

                    retriever.retrievedData(m);
                }
            }
        });

    }

    @Override
    public void changeCustomerName(Customer customer, String name) {
        getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + customer.getID()).child(DatabaseStructure.CustomerBranch.C_NAME).setValue(name);
    }

    @Override
    public void changeCustomerPhoneNumber(Customer customer, long phone) {

        //customer data
        getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + customer.getID()).child(DatabaseStructure.CustomerBranch.C_PHONE);


        // index
        // common ref
        DatabaseReference ref = getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.PHONE_NUMBER_INDEX);

        // remove the index from old phone number
        // add index from new phone number
        ref.child(customer.getPhoneNumber() + "").removeValue();
        ref.child(phone + "").child(customer.getID() + "").setValue(customer.getID());
    }

    public void findCustomerIDsByPhone(long phoneNumber, final DataRetriever<LinkedList<Long>> retriever){
        getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.PHONE_NUMBER_INDEX)
                .child(phoneNumber + "")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();
                    LinkedList<Long> IDList = new LinkedList<>();

                    if(snapshot.exists()){
                        for(DataSnapshot child : snapshot.getChildren()){
                            long id = child.getValue(Long.class);
                            IDList.add(id);
                        }
                    }

                    retriever.retrievedData(IDList);
                }
            }
        });
    }

    public void findCustomerByID(long id, final DataRetriever<Customer> retriever){
        getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Customer c = task.getResult().getValue(Customer.class);
                    retriever.retrievedData(c);
                }
            }
        });
    }

    public void uploadCustomer(final Customer c){
        //indexed by phone number
        getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.PHONE_NUMBER_INDEX)
                .child("" + c.getPhoneNumber())
                .child("" + c.getID()).setValue(c.getID());

        // store the customer by id
        getRef().child(DatabaseStructure.CustomerBranch.BRANCH_NAME)
                .child(DatabaseStructure.CustomerBranch.LIST)
                .child("" + c.getID()).setValue(c);
    }

    @Override
    public void findOpenTransactionIDsByDate(String date, final DataRetriever<LinkedList<Long>> retriever) {
        Scanner scanner = new Scanner(date);
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(year).child(month).child(day)
                .child(DatabaseStructure.TransactionBranch.OPEN_APPOINTMENT)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();
                    LinkedList<Long> IDList = new LinkedList<>();

                    if(snapshot.exists()){
                        for(DataSnapshot child : snapshot.getChildren()){
                            long id = child.getValue(Long.class);
                            IDList.add(id);
                        }
                    }

                    retriever.retrievedData(IDList);
                }
            }
        });
    }

    @Override
    public void findTransactionIDsByCustomerID(long customerID, final DataRetriever<LinkedList<Long>> retriever) {
        getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.CUSTOMER_ID_INDEX)
                .child(customerID + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();
                    LinkedList<Long> IDList = new LinkedList<>();

                    if(snapshot.exists()){
                        for(DataSnapshot child : snapshot.getChildren()){
                            long id = child.getValue(Long.class);
                            IDList.add(id);
                        }
                    }

                    retriever.retrievedData(IDList);
                }
            }
        });
    }

    @Override
    public void findTransactionIDsByDateAndTechnicianID(String date, long technicianId, final DataRetriever<LinkedList<Long>> retriever) {
        Scanner scanner = new Scanner(date);
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        final LinkedList<Long> ids = new LinkedList<>();

        getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(year).child(month).child(day)
                .child("" + technicianId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    for(DataSnapshot child : snapshot.getChildren()){
                        ids.add(child.getValue(Long.class));
                    }
                }

                retriever.retrievedData(ids);
            }
        });
    }

    @Override
    public void findTransactionByID(final long id, final DataRetriever<TransactionFrame> retriever) {
        getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    TransactionFrame c = task.getResult().getValue(TransactionFrame.class);
                    c.setID(id);
                    retriever.retrievedData(c);
                }
            }
        });
    }

    @Override
    public void findGiftCardByID(String id, final DataRetriever<Gift> retriever) {
        getRef().child(DatabaseStructure.Gift.BRANCH_NAME)
                .child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Gift g = task.getResult().getValue(Gift.class);
                    retriever.retrievedData(g);
                }
            }
        });
    }

    @Override
    public void uploadTransaction(Transaction transaction) {
        DatabaseReference commonRef = getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME);

        // store by id
        TransactionFrame frame = new TransactionFrame(transaction);
        commonRef.child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID()).setValue(frame);

        //indexed by date
        Scanner scanner = new Scanner(transaction.getDate());
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        commonRef.child(year).child(month).child(day)
                .child(DatabaseStructure.TransactionBranch.OPEN_APPOINTMENT)
                .child("" + transaction.getID()).setValue(transaction.getID());

        // indexed by customer
        commonRef.child(DatabaseStructure.TransactionBranch.CUSTOMER_ID_INDEX)
                .child("" + transaction.getCustomer().getID())
                .child(transaction.getID() + "").setValue(transaction.getID());
    }

    @Override
    public void uploadGiftCard(Gift gift) {

        // store by id
        getRef().child(DatabaseStructure.Gift.BRANCH_NAME).child(gift.getId()).setValue(gift);

        //indexed by expire date
        Scanner scanner = new Scanner(gift.getDateExpires());
        String day = scanner.next();
        String month = scanner.next();
        String year = scanner.next();
        scanner.close();

        getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(year).child(month).child(day)
                .child(DatabaseStructure.Gift.INDEX_BRANCH)
                .child(gift.getId()).setValue(gift.getId());
    }

    @Override
    public void markNoShow(Transaction transaction) {
        getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID())
                .child(DatabaseStructure.TransactionBranch.T_AMOUNT).setValue("" + transaction.getAmount());
    }

    public void attachTransactionTech(Transaction transaction){
        //update technicianID
        getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID())
                .child(DatabaseStructure.TransactionBranch.T_TECH_ID).setValue(transaction.getTech().getID());
    }

    public void closeTransaction(Transaction transaction){
        DatabaseReference commonRef = getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID());

        //update amount
        commonRef.child(DatabaseStructure.TransactionBranch.T_AMOUNT)
                .setValue(transaction.getAmount() + "");
        //update tip
        commonRef.child(DatabaseStructure.TransactionBranch.T_TIP).setValue(transaction.getTip() + "");
        //update services
        commonRef.child(DatabaseStructure.TransactionBranch.T_SERVICES).setValue(transaction.getServices());

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // accounting
        updateWeeklyAccountingData(transaction.getLocalDateAppointmentDate(), transaction.getTech().getID()+ "", transaction.getAmount(), transaction.getTip());
        updateMonthlyAccountingData(transaction.getLocalDateAppointmentDate(), transaction.getTech().getID()+ "", transaction.getAmount(), transaction.getTip());

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // close transaction
        LocalDate date = transaction.getLocalDateAppointmentDate();

        // yearly accounting
        DatabaseReference timeIndexRef = getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(date.getYear() + "")
                .child(date.getMonthValue() + "")
                .child(date.getDayOfMonth() + "");

        //move from open appointment to closed
        timeIndexRef.child(DatabaseStructure.TransactionBranch.OPEN_APPOINTMENT)
                .child("" + transaction.getID()).removeValue();

        timeIndexRef.child(transaction.getTech().getID() + "")
                .child("" + transaction.getID()).setValue(transaction.getID());
    }

    @Override
    public void editRecord(Transaction transaction, int amount, int tip, String service) {
        int amountDif = amount - transaction.getAmount();
        int tipDif = tip - transaction.getTip();

        DatabaseReference commonRef = getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(DatabaseStructure.TransactionBranch.LIST)
                .child("" + transaction.getID());

        //update amount
        commonRef.child(DatabaseStructure.TransactionBranch.T_AMOUNT)
                .setValue(amount + "");
        //update tip
        commonRef.child(DatabaseStructure.TransactionBranch.T_TIP).setValue(tip + "");
        //update services
        commonRef.child(DatabaseStructure.TransactionBranch.T_SERVICES).setValue(service);

        // accounting
        updateWeeklyAccountingData(transaction.getLocalDateAppointmentDate(), transaction.getTech().getID()+ "", amountDif, tipDif);
        updateMonthlyAccountingData(transaction.getLocalDateAppointmentDate(), transaction.getTech().getID()+ "", amountDif, tipDif);
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

            getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                    .child(year).child(month).child(day)
                    .child(DatabaseStructure.Gift.INDEX_BRANCH)
                    .child(gift.getId()).removeValue();
        }

        gift.setAmount(amount);
        gift.setDateExpires(dateExpire);

        uploadGiftCard(gift);
    }

    /**
     * update monthly accounting data for the average item
     */
    private void updateMonthlyAccountingData(LocalDate date, String subBranch, int pretax, int postTax){

        // yearly accounting
        DatabaseReference timeIndexRef = getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME).child(date.getYear() + "");
        updateStandardAccountingData(timeIndexRef, subBranch, pretax, postTax);

        // monthly accounting
        timeIndexRef = timeIndexRef.child(date.getMonthValue() + "");
        updateStandardAccountingData(timeIndexRef, subBranch, pretax, postTax);

        // daily accounting
        timeIndexRef = timeIndexRef.child(date.getDayOfMonth() + "");
        updateStandardAccountingData(timeIndexRef, subBranch, pretax, postTax);
    }


    /**
     * update weekly accounting data
     */
    private void updateWeeklyAccountingData(LocalDate date, String subBranch, int pretax, int postTax){

        LocalDate startOfWeek = AppUtil.getMonday(date);

        DatabaseReference weekRef = getRef().child(DatabaseStructure.TransactionBranch.BRANCH_NAME)
                .child(startOfWeek.getYear() + "").child(startOfWeek.getMonthValue() + "").child(startOfWeek.getDayOfMonth() + "")
                .child(DatabaseStructure.Accounting.BRANCH_NAME).child(DatabaseStructure.Accounting.WEEKLY_TOTALS);

        updateSubAccountingData(weekRef, subBranch, pretax, postTax);

    }

    /**
     * update accounting data under the Accounting branch name
     */
    private void updateStandardAccountingData(DatabaseReference timeIndexRef, String subBranch, int pretax, int postTax) {
        updateSubAccountingData(timeIndexRef.child(DatabaseStructure.Accounting.BRANCH_NAME),
                subBranch, pretax, postTax);
    }

    /**
     * update the shop total and a sub branch (tech id or sale)
     */
    private void updateSubAccountingData(DatabaseReference timeIndexRef, String subBranch, int pretax, int postTax){
        // shop total
        updateAccountingData(timeIndexRef.child(DatabaseStructure.Accounting.SHOP_TOTAL), pretax, postTax);

        // technician earnings
        updateAccountingData(timeIndexRef.child(subBranch), pretax, postTax);
    }

    /**
     * update the Account amount at a location
     */
    private void updateAccountingData(DatabaseReference location, int pretax, int postTax){
        location.child(DatabaseStructure.Accounting.A_AMOUNT).setValue(ServerValue.increment(pretax));
        location.child(DatabaseStructure.Accounting.A_NO_TAX).setValue(ServerValue.increment(postTax));
    }


    static DatabaseReference getRef(){
        return FirebaseDatabase.getInstance().getReference();
    }
}
