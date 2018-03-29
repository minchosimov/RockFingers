package com.mincho.rockfingers;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by simov on 04-Jul-17. application class for firebase
 */

public class RockFingerApplication extends Application {
    public static FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
    public static FirebaseAnalytics getFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }
}
