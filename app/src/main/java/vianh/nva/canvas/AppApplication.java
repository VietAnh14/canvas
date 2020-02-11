package vianh.nva.canvas;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;


public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
