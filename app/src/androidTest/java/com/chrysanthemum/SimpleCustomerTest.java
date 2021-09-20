package com.chrysanthemum;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.Util.BoolFlag;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.retreiver.DataRetriever;
import com.chrysanthemum.appdata.querries.customers.CustomerByNameQuery;
import com.chrysanthemum.appdata.querries.customers.CustomerByPhoneQuery;
import com.chrysanthemum.appdata.querries.customers.NewCustomerQuery;
import com.chrysanthemum.firebase.FireDatabase;
import com.chrysanthemum.firebase.LoginRepository;
import com.chrysanthemum.ui.accessAuthentication.AppAuthenticationActivity;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
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
    long c1p = 19024206969l;

    @Test public void testCreateCustomer(){

        NewCustomerQuery q = new NewCustomerQuery(c1n, c1p);
        q.executeQuery();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BoolFlag f = new BoolFlag();
        BoolFlag failed = new BoolFlag();

        CustomerByNameQuery nq = new CustomerByNameQuery(c1f, new DataRetriever<Map<String, Customer>>() {
            @Override
            public void retrievedData(Map<String, Customer> data) {
                verifyCreateOneCustomer(data, failed);
                f.set();
            }
        });
        nq.executeQuery();

        while(!f.read()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        f.reset();

        assertFalse(failed.read());

        CustomerByPhoneQuery pq = new CustomerByPhoneQuery(c1p, new DataRetriever<Map<String, Customer>>() {
            @Override
            public void retrievedData(Map<String, Customer> data) {
                verifyCreateOneCustomer(data, failed);
                f.set();
            }
        });

        pq.executeQuery();

        while(!f.read()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertFalse(failed.read());
    }

    private void verifyCreateOneCustomer(Map<String, Customer> data, BoolFlag failed) {
        if (data.size() != 1) {
            failed.set();
            return;
        }

        Customer c = data.get(data.keySet().iterator().next());

        if (c.getName() != c1n) {
            failed.set();
            return;
        }

        if (c.getPhoneNumber() != c1p) {
            failed.set();
            return;
        }
    }

}
