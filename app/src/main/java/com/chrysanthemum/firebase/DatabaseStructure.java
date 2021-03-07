package com.chrysanthemum.firebase;

public class DatabaseStructure {

    public static final String CURRENTLY_ACTIVE_APP_INSTANCE = "app";

    public static class TechnicianBranch {
        public static final String BRANCH_NAME = "technician";
        public static final String PASSWORD_STORAGE = "password";
        public static final String LIST = "technicianList";
    }

    public static class CustomerBranch {
        public static final String BRANCH_NAME = "customer";
        public static final String PHONE_NUMBER_INDEX = "phone";
        public static final String LIST = "id";
    }

    public static class TransactionBranch {
        public static final String BRANCH_NAME = "transaction";
        public static final String OPEN_APPOINTMENT = "Open";
        public static final String LIST = "id";
        public static final String CUSTOMER_ID_INDEX = "customer";


        public static final String T_AMOUNT = "amount";
        public static final String T_TIP = "tip";
        public static final String T_SERVICES = "services";
        public static final String T_TECH_ID = "technicianID";
    }

    public static class Accounting {
        public static final String BRANCH_NAME = "Acc";
        public static final String SHOP_TOTAL = "ST";
        public static final String WEEKLY_TOTALS = "WTs";
    }
}
