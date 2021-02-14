package com.chrysanthemum;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.chrysanthemum.ui.accessAuthentication.AppAuthenticationActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4ClassRunner.class)
public class EspressoVerificationTest {

    @Rule
    public ActivityScenarioRule<AppAuthenticationActivity> activityRule =
            new ActivityScenarioRule<>(AppAuthenticationActivity.class);

    @Test
    public void useAppContext() {


    }
}
