package com.example.naidich.tom.tomsprojectapplication.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.naidich.tom.tomsprojectapplication.R;
import com.example.naidich.tom.tomsprojectapplication.core.helpers.LogHelper;
import com.example.naidich.tom.tomsprojectapplication.ui.handlers.MusicPlayerHandler;
import com.example.naidich.tom.tomsprojectapplication.ui.helpers.IntentFactoryHelper;
import com.example.naidich.tom.tomsprojectapplication.ui.utils.ShowGifView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final MusicPlayerHandler _musicHandler = new MusicPlayerHandler(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        LogHelper.logDebug("Application has started.");

        handleWelcomeAnimation();

        handleMainViewEvents();

        _musicHandler.playBackgroundMusic(R.raw.main_activity_background);

        setLocale(getSavedSelectedLanguage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _musicHandler.stopAllMusic();
    }

    private void handleMainViewEvents(){
        // Handle 'Play Tic-Tac-Toe' button
        Button mTicTacToeButton = findViewById(R.id.button_tic_tac_toe);
        mTicTacToeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityWrapper(IntentFactoryHelper.getIntent(MainActivity.this, IntentFactoryHelper.IntentType.TicTacToe));
            }
        });

        // Handle 'Play War Game button
        Button mWarGameButton = findViewById(R.id.button_war_game);
        mWarGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityWrapper(IntentFactoryHelper.getIntent(MainActivity.this, IntentFactoryHelper.IntentType.WarGameSelection));
            }
        });

        // Handle 'MEME' button
        Button mMemeButton = findViewById(R.id.button_catch_em_all);
        mMemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   startActivityWrapper(IntentFactoryHelper.getIntent(MainActivity.this, IntentFactoryHelper.IntentType.CatchEmAll));
             Intent startMeme = new Intent(MainActivity.this, MemeActivity.class);
             startActivity(startMeme);
            }
        });
// Handle 'CatchEmAllButton' button
        Button mCatchEmAllButton = findViewById(R.id.button_pokemon);
        mCatchEmAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   startActivityWrapper(IntentFactoryHelper.getIntent(MainActivity.this, IntentFactoryHelper.IntentType.CatchEmAll));
                Intent startMeme = new Intent(MainActivity.this, CatchEmAllActivity.class);
                startActivity(startMeme);
            }
        });
        // Handle 'Scores' button
        Button mScoresButton = findViewById(R.id.button_scoreboard);
        mScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityWrapper(IntentFactoryHelper.getIntent(MainActivity.this, IntentFactoryHelper.IntentType.Scores));
            }
        });

        // Handle 'Language' button
        Button mLangButton = findViewById(R.id.buttonLang);
        mLangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedLanguage = getSavedSelectedLanguage();
                String newLang = "";

                if(selectedLanguage.equals("he")) newLang = "en";
                else if(selectedLanguage.equals("en")) newLang = "he";

                _musicHandler.playEventSound(R.raw.click, null);

                saveSelectedLanguage(newLang);
                setLocale(newLang);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        _musicHandler.playBackgroundMusic(R.raw.main_activity_background);

        setLocale(getSavedSelectedLanguage());
    }

    private void startActivityWrapper(Intent intent){
        _musicHandler.stopAllMusic();
        startActivity(intent);
    }

    private void handleWelcomeAnimation(){
        WebView wbv = findViewById(R.id.welcome_anim_gif);
        wbv.loadUrl("file:///android_asset/welcome_anim.gif");
        wbv.getSettings().setLoadWithOverviewMode(true);
        wbv.getSettings().setUseWideViewPort(true);
    }

    private void saveSelectedLanguage(String lang){
        SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
        editor.putString("language", lang);
        editor.commit();
    }

    private String getSavedSelectedLanguage(){
        SharedPreferences prefs = getSharedPreferences("Data", MODE_PRIVATE);
        String selectedLanguage = prefs.getString("language", "en");
        return selectedLanguage;
    }

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        invalidateOptionsMenu();
        onConfigurationChanged(conf);
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ((Button)findViewById(R.id.button_war_game)).setText(R.string.button_war_game);
        ((Button)findViewById(R.id.button_tic_tac_toe)).setText(R.string.button_tic_tac_toe);
        ((Button)findViewById(R.id.button_pokemon)).setText(R.string.button_catch_em_all);
        ((Button)findViewById(R.id.button_catch_em_all)).setText(R.string.button_meme);
        ((Button)findViewById(R.id.button_scoreboard)).setText(R.string.button_scoreboard);
        ((Button)findViewById(R.id.buttonLang)).setText(R.string.button_language);
    }
    @Override
    public void onBackPressed(){}

}
