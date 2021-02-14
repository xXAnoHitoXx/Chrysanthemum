package com.chrysanthemum.firebase;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseInitializer {
    public void initialization(Context context){
        FirebaseOptions.Builder builder = new FirebaseOptions.Builder()
                .setApplicationId("1:151385711114:android:bd433037e98f367fcef5f3")
                .setApiKey(" AIzaSyDZq3TRh2t366xuuE4tMW9fIDIMTN0bQIs")
                .setDatabaseUrl("https://chrysanthemumdb-default-rtdb.firebaseio.com/")
                .setStorageBucket("gs://chrysanthemumdb.appspot.com");

        FirebaseApp.initializeApp(context, builder.build());
    }
}
