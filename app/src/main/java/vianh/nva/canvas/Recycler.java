package vianh.nva.canvas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Recycler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        setupRecycler();
    }

    public void setupRecycler() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("abc");
        }
        Adapter adapter = new Adapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            String a = "aaa";
            adapter.addItem(a);
        });
    }
}
