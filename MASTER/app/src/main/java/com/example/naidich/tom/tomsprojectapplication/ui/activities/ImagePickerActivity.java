package com.example.naidich.tom.tomsprojectapplication.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.naidich.tom.tomsprojectapplication.R;
import com.example.naidich.tom.tomsprojectapplication.ui.helpers.ImagePickerAdapter;

import java.util.ArrayList;

import static com.example.naidich.tom.tomsprojectapplication.ui.activities.MemeActivity.go;
import static com.example.naidich.tom.tomsprojectapplication.ui.activities.MemeActivity.load;
import static com.example.naidich.tom.tomsprojectapplication.ui.activities.MemeActivity.loadFromPhone;
import static com.example.naidich.tom.tomsprojectapplication.ui.activities.MemeActivity.save;

public class ImagePickerActivity extends AppCompatActivity implements ImagePickerAdapter.ItemClickListener {
    private ImagePickerAdapter adapter;
    private ArrayList<Integer> memePics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        // ADD IMAGES FROM DRAWABLE TO ARRAY
        memePics = new ArrayList<>();
        memePics.add(R.drawable.pic_1);
        memePics.add(R.drawable.pic_2);
        memePics.add(R.drawable.pic_3);
        memePics.add(R.drawable.pic_4);
        memePics.add(R.drawable.pic_5);
        memePics.add(R.drawable.pic_6);

// set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new ImagePickerAdapter(this, memePics);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    // ON CLICK ON IMAGE IN RECYCLE VIEW
    @Override
    public void onItemClick(View view, int position) {
        MemeActivity.imageView.setImageResource(memePics.get(position));
        loadFromPhone.setVisibility(View.INVISIBLE);
        load.setVisibility(View.GONE);
        go.setVisibility(View.VISIBLE);
        save.setEnabled(true);
        save.setVisibility(View.VISIBLE);
        finish();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
