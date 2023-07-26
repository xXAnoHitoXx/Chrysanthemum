package com.chrysanthemum.firebase;

import android.graphics.Color;

import com.chrysanthemum.appdata.dataType.Technician;

public class DatabaseStructure {

    public static final String CURRENTLY_ACTIVE_APP_INSTANCE = "app";
    public static final String TEST = "test";
    public static class TechnicianBranch {
        public static final String BRANCH_NAME = "technician";
        public static final String PASSWORD_STORAGE = "password";
        public static final String LIST = "technicianList";
    }

    public static class CustomerBranch {
        public static final String BRANCH_NAME = "customer";
        public static final String PHONE_NUMBER_INDEX = "phone";
        public static final String LIST = "id";

        public static final String C_NAME = "name";
        public static final String C_PHONE = "phoneNumber";
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

        public static final Technician SALE_TECH= new Technician("Sales", "", Color.LTGRAY, -32098271938L);

        public static final String A_AMOUNT = "a";
        public static final String A_NO_TAX = "t";
        public static final String A_CLOSURE = "close";
    }

    public static class Gift {
        public static final String BRANCH_NAME = "gid";
        public static final String INDEX_BRANCH = "gifts";
    }
}
