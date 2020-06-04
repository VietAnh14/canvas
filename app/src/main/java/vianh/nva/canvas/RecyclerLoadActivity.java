package vianh.nva.canvas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RecyclerLoadActivity extends AppCompatActivity {
    List<String> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_load_activity);
        setupRecycler();
    }


    private void setupRecycler() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        items = new ArrayList<>();
        items.addAll(generateData());
        InfinityLoadAdapter adapter = new InfinityLoadAdapter(recyclerView, items);
        adapter.setOnLoadMore(() -> {
            if(items.size() < 40) {
                Log.d("OnLoadMore", "Called");
                recyclerView.post(() -> adapter.addLast(null));
                new Handler().postDelayed(() -> {
                    adapter.removeLast();
                    adapter.addAll(generateData());
                    adapter.notifyDataSetChanged();
                    adapter.setLoaded();
                }, 3000);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private List<String> generateData() {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("aaa");
        }
        return data;
    }
}
