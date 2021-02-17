package com.chrysanthemum;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.chrysanthemum.data.DataStorageModule;
import com.chrysanthemum.data.SecurityModule;
import com.chrysanthemum.ui.accessAuthentication.AppAuthenticationActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * testing login functionality
 */
@RunWith(JUnit4.class)
public class LoginTest {


    @Rule
    public ActivityScenarioRule<AppAuthenticationActivity> activityRule =
            new ActivityScenarioRule<>(AppAuthenticationActivity.class);

    @Test
    public void testAccessPermissionCorrectPassword() throws InterruptedException {
        SecurityModule sm = DataStorageModule.getFrontEnd().getSecurityModule();

        sm.requestAccess("chrysanthemumspa@gmail.com", "Only4988");

        Thread.sleep(2000);

        assertTrue(sm.hasDBAccess());
    }

    @Test
    public void testAccessPermissionWrongPassword() throws InterruptedException {
        SecurityModule sm = DataStorageModule.getFrontEnd().getSecurityModule();

        sm.requestAccess("chrysanthemumspa@gmail.com", "On8");

        Thread.sleep(2000);

        assertFalse(sm.hasDBAccess());
    }
}
