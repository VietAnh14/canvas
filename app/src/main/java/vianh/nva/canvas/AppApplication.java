package vianh.nva.canvas;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import okhttp3.OkHttpClient;


public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
