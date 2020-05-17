package vianh.nva.canvas;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoginInterceptor implements Interceptor {
    String message = "ABC";

    public LoginInterceptor(String message) {
        this.message = message;
    }

    private final String TAG = "interceptor";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d(TAG, "interceptor: " + message);
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response;
    }
}
