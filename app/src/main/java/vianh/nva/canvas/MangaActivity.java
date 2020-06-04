package vianh.nva.canvas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import java.util.ArrayList;

public class MangaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga);
        setupAdapter();
    }

    private void setupAdapter() {
        ArrayList<String> listUrl = new ArrayList<>();
        listUrl.add("https://s8.mkklcdnv8.com/mangakakalot/k1/kaguyasama_wa_kokurasetai_tensaitachi_no_renai_zunousen/chapter_188/2.jpg");
        listUrl.add("https://s8.mkklcdnv8.com/mangakakalot/k1/kaguyasama_wa_kokurasetai_tensaitachi_no_renai_zunousen/chapter_188/1.jpg");
        listUrl.add("https://s8.mkklcdnv8.com/mangakakalot/k1/kaguyasama_wa_kokurasetai_tensaitachi_no_renai_zunousen/chapter_188/3.jpg");
        listUrl.add("https://s8.mkklcdnv8.com/mangakakalot/k1/kaguyasama_wa_kokurasetai_tensaitachi_no_renai_zunousen/chapter_188/4.jpg");
        MangaAdapter adapter = new MangaAdapter(listUrl);
        SnapHelper snapHelper = new LinearSnapHelper();
        RecyclerView recyclerView = findViewById(R.id.mangaRecycler);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MangaActivity.class);
    }
}
