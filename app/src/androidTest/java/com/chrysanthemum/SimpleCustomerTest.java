package com.chrysanthemum;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.Util.BoolFlag;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.querries.DBReadQuery;
import com.chrysanthemum.appdata.querries.TimedOutException;
import com.chrysanthemum.appdata.querries.customer.CreateCustomer;
import com.chrysanthemum.appdata.querries.customer.read.FindCustomersByName;
import com.chrysanthemum.appdata.querries.customer.read.FindCustomersByPhone;
import com.chrysanthemum.firebase.FireDatabase;
import com.chrysanthemum.ui.accessAuthentication.AppAuthenticationActivity;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Map;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SimpleCustomerTest {

    @BeforeClass
    public static void setUpClass(){
        String uname = "chrysanthemumspa@gmail.com";
        String pword = "Only4988";

        DataStorageModule.getModule().getSecurityModule()
                .enableTestMode(uname, pword);
    }

    @ClassRule
    public static ActivityScenarioRule<AppAuthenticationActivity> activityRule =
            new ActivityScenarioRule<>(AppAuthenticationActivity.class);

    // Customer 1
    final String c1n = "Ano Hito";
    final String c1f = "Ano";
    String c1l = "Hito";
    final long c1p = 19024206969L;

    @Before
    public void beforeTest(){
        FireDatabase.clearTestEnvironment();
    }

    /**
     * test creating a new customer
     * test the retrieval of a customer that exists in the system
     */
    @Test public void testCustomerRetrieval(){
        CreateCustomer q = new CreateCustomer(c1n, c1p);
        q.execute();

        BoolFlag failed = new BoolFlag();

        DBReadQuery<Map<String, Customer>> nq = new FindCustomersByName(c1f);
        try {
            verifyCreateOneCustomer(nq.execute(), failed);
        } catch (TimedOutException e) {
            assertTrue(false);
        }

        assertFalse(failed.read());

        DBReadQuery<Map<String, Customer>> pq = new FindCustomersByPhone(c1p);
        try {
            verifyCreateOneCustomer(pq.execute(), failed);
        } catch (TimedOutException e) {
            assertTrue(false);
        }

        assertTrue(failed.read());
    }

    /**
     * test the retrieval of a non existent customer
     */
    @Test public void testEmptyCustomerRetrieval(){
        BoolFlag failed = new BoolFlag();

        DBReadQuery<Map<String, Customer>> nq = new FindCustomersByName(c1f);
        try {
            verifyCreateOneCustomer(nq.execute(), failed);
        } catch (TimedOutException e) {
            assertTrue(false);
        }

        assertTrue(failed.read());

        DBReadQuery<Map<String, Customer>> pq = new FindCustomersByPhone(c1p);
        try {
            verifyCreateOneCustomer(pq.execute(), failed);
        } catch (TimedOutException e) {
            assertTrue(false);
        }

        assertTrue(failed.read());
    }

    private void verifyCreateOneCustomer(Map<String, Customer> data, BoolFlag failed) {
        failed.reset();
        if (data.size() != 1) {
            failed.set();
            return;
        }

        Customer c = data.get(data.keySet().iterator().next());

        if (!c.getName().equals(c1n)) {
            failed.set();
            return;
        }

        if (c.getPhoneNumber() != c1p) {
            failed.set();
        }
    }

}
