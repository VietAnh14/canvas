package vianh.nva.canvas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CustomImage customImage =findViewById(R.id.customImage);
        Button recButton = findViewById(R.id.btnRec);
        Button polyButton = findViewById(R.id.btnPolygon);
        Button btnGetScaleFactor = findViewById(R.id.btnGetScaleFactor);
        recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customImage.setIsRectangle(true);
            }
        });

        polyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customImage.setIsRectangle(false);
            }
        });

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

        btnGetScaleFactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Current scale factor: ", String.valueOf(customImage.getScaleFactor()));
            }
        });
    }
}
