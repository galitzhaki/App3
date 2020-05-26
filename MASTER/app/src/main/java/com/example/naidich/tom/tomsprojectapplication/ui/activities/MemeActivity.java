package com.example.naidich.tom.tomsprojectapplication.ui.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naidich.tom.tomsprojectapplication.R;
import com.example.naidich.tom.tomsprojectapplication.core.enums.Game;
import com.example.naidich.tom.tomsprojectapplication.core.providers.ScoreModelProvider;


import java.io.File;
import java.io.FileOutputStream;

public class MemeActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;
    private static final int RESULT_LOAD_IMGE = 2;


    public static Button loadFromPhone, load, save, go;
    private EditText editText1, editText2;
    private TextView textView1, textView2;
    public static ImageView imageView;
    public static ProgressBar progressBar;

    private String currentImage = "";
    private SharedPreferences _sharedPreferences;
     private ScoreModelProvider _scoreProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme);
        requestAppPermissions();
        initView();

    }
    // INIT VIEW
    private void initView(){
        _sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        _scoreProvider = new ScoreModelProvider(_sharedPreferences);

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        save = findViewById(R.id.save_btn);
        save.setEnabled(false);
        save.setVisibility(View.GONE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
               View content = findViewById(R.id.lay);
               Bitmap bitmap = getScreenShot(content);
               currentImage = "meme" + System.currentTimeMillis() + ".png";
               store(bitmap, currentImage);
                final String scoreMessage = getResources().getString(R.string.meme_score_message).replace("{COUNT}", 1 + "");
                _scoreProvider.addScore(Game.GameType.Meme, scoreMessage);


            }
        });
        // START ON LOAD FROM GALLERY IMAGE PIC
        load = findViewById(R.id.load_btn);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pick, RESULT_LOAD_IMGE);

            }
        });
        // START ACTIVITY TO PIC IMAGE FROM APPLICATION  ImagePickerActivity.java
        loadFromPhone = findViewById(R.id.load_from_drw_btn);
        loadFromPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loadImages = new Intent(MemeActivity.this, ImagePickerActivity.class);
                startActivity(loadImages);

            }
        });


        go = findViewById(R.id.go_btn);
        go.setVisibility(View.GONE);
       // START ADD TEXT OVER IMAGE
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView1.setText(editText1.getText().toString());
                textView2.setText(editText2.getText().toString());

                editText1.setText("");
                editText2.setText("");

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);

    }
    // GET EDITED IMAGE WITH TEXT FROM PHONE SCREEN
    public static Bitmap getScreenShot(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }
    // SAVE EDITED IMAGE TO DIRECTORY MEME ON PHONE SD CARD
    public void store(Bitmap bm, String fileName){
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MEME";
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdir();
        }
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(MemeActivity.this, getString(R.string.fos_text), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            imageView.setImageDrawable(null);
            go.setVisibility(View.GONE);
            load.setVisibility(View.VISIBLE);
            loadFromPhone.setVisibility(View.VISIBLE);
            save.setEnabled(false);
            save.setVisibility(View.GONE);
            textView2.setText("");
            textView1.setText("");

        }
        catch (Exception e){
            Toast.makeText(MemeActivity.this, getString(R.string.fos_error), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    // GET IMAGE FROM GALLERY TO EDIT IT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == RESULT_LOAD_IMGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            go.setVisibility(View.VISIBLE);
            load.setVisibility(View.GONE);
            loadFromPhone.setVisibility(View.GONE);
            save.setEnabled(true);
            save.setVisibility(View.VISIBLE);

        }

    }
   // ASK FOR PHONE PERMISSIONS TO ACCESS TO PHONE PHOTOS IN GALLERY
    private void requestAppPermissions() {

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, MY_PERMISSION_REQUEST); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}

