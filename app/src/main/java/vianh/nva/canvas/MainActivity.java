package vianh.nva.canvas;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.internal.InternalNetworking;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vianh.nva.canvas.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    OkHttpClient client = new OkHttpClient();

    void run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("OKHTTP", response.body().string());
                    setString(result);
                }
            }
        });
    }
    private String result;
    void setString(String str) {
        result = str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewModel viewModel = new MainViewModel();
        binding.setViewModel(viewModel);
        binding.executePendingBindings();
//        final CustomImage customImage = findViewById(R.id.customImage);
        Button recButton = findViewById(R.id.btnRec);
        Button polyButton = findViewById(R.id.btnPolygon);
        Button btnGetScaleFactor = findViewById(R.id.btnGetScaleFactor);
        recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest();
            }
        });

        polyButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SceneActivity.class);
            startActivity(intent);
        });

        btnGetScaleFactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeInterceptor("Button scale factor");
            }
        });
//
//        try {
//            run("https://reqres.in/api/users/{page}");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        findViewById(R.id.recyclerBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, RecyclerLoadActivity.class);
            startActivity(intent);
        });

        setupTransition();
        setupAnimate();
    }

    public void changeInterceptor(String message) {
        LoginInterceptor loginInterceptor = new LoginInterceptor(message);
        OkHttpClient.Builder okHttpClient = InternalNetworking.getClient().newBuilder();
        okHttpClient.addInterceptor(loginInterceptor);
        okHttpClient.interceptors().clear();
        okHttpClient.addInterceptor(loginInterceptor);
        InternalNetworking.setClient(okHttpClient.build());
    }

    public void makeRequest() {
        AndroidNetworking.get("https://reqres.in/api/users/{page}")
                .addPathParameter("page", "2")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test android networking", response.toString());
                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("test android networking", "Get json failed");
                    }
                });
    }

    ImageView imageView;
    private void setupTransition() {
        Button btnTransition = findViewById(R.id.btnTransition);
        imageView = findViewById(R.id.headerImage);
        btnTransition.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShareElementActivity.class);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, imageView, getString(R.string.image));
            startActivity(intent, options.toBundle());
        });
    }

    public void toMangaActivity(View view) {
        startActivity(MangaActivity.newIntent(this));
    }

    private void setupAnimate() {
        RoundProgressBar progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.btnAnimate).setOnClickListener(v -> {
            progressBar.animateToCurrent(1500);
        });
    }
}
