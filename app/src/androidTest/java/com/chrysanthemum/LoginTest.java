package com.chrysanthemum;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.firebase.LoginRepository;
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
    String uname = "chrysanthemumspa@gmail.com";
    String pword = "Only4988";

    @Rule
    public ActivityScenarioRule<AppAuthenticationActivity> activityRule =
            new ActivityScenarioRule<>(AppAuthenticationActivity.class);

    @Test
    public void testTestModeInit() throws InterruptedException {
        DataStorageModule.getFrontEnd().getSecurityModule()
                .enableTestMode(uname, pword);

        assertTrue(LoginRepository.inTestMode());
    }

}
