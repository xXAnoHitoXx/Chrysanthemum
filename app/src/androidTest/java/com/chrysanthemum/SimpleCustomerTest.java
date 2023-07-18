package com.chrysanthemum;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.Util.BoolFlag;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.querries._old.customers.CustomerByNameQuery;
import com.chrysanthemum.appdata.querries._old.customers.CustomerByPhoneQuery;
import com.chrysanthemum.appdata.querries._old.customers.NewCustomerQuery;
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

        DataStorageModule.getFrontEnd().getSecurityModule()
                .enableTestMode(uname, pword);
    }

    @ClassRule
    public static ActivityScenarioRule<AppAuthenticationActivity> activityRule =
            new ActivityScenarioRule<>(AppAuthenticationActivity.class);

    // Customer 1
    String c1n = "Ano Hito";
    String c1f = "Ano";
    String c1l = "Hito";
    long c1p = 19024206969L;

    @Before
    public void beforeTest(){
        FireDatabase.clearTestEnvironment();
    }

    /**
     * test creating a new customer
     * test the retrieval of a customer that exists in the system
     */
    @Test public void testCustomerRetrieval(){
        NewCustomerQuery q = new NewCustomerQuery(c1n, c1p);
        q.executeQuery();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BoolFlag f = new BoolFlag();
        BoolFlag failed = new BoolFlag();

        CustomerByNameQuery nq = new CustomerByNameQuery(c1f, data -> {
            verifyCreateOneCustomer(data, failed);
            f.set();
        });
        nq.executeQuery();

        while(!f.read()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        f.reset();

        assertFalse(failed.read());

        CustomerByPhoneQuery pq = new CustomerByPhoneQuery(c1p, data -> {
            verifyCreateOneCustomer(data, failed);
            f.set();
        });

        pq.executeQuery();

        while(!f.read()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertFalse(failed.read());
    }

    /**
     * test the retrieval of a non existent customer
     */
    @Test public void testEmptyCustomerRetrieval(){

        BoolFlag f = new BoolFlag();
        BoolFlag failed = new BoolFlag();

        CustomerByNameQuery nq = new CustomerByNameQuery(c1f, data -> {
            verifyCreateOneCustomer(data, failed);
            f.set();
        });
        nq.executeQuery();

        while(!f.read()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertTrue(failed.read());

        f.reset();

        CustomerByPhoneQuery pq = new CustomerByPhoneQuery(c1p, data -> {
            verifyCreateOneCustomer(data, failed);
            f.set();
        });

        pq.executeQuery();

        while(!f.read()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
