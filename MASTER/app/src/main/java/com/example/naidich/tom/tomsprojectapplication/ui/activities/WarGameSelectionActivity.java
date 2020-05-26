package com.example.naidich.tom.tomsprojectapplication.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naidich.tom.tomsprojectapplication.R;
import com.example.naidich.tom.tomsprojectapplication.core.enums.Background;
import com.example.naidich.tom.tomsprojectapplication.core.enums.CharacterAction;
import com.example.naidich.tom.tomsprojectapplication.core.enums.CharacterSkin;
import com.example.naidich.tom.tomsprojectapplication.core.helpers.LogHelper;
import com.example.naidich.tom.tomsprojectapplication.core.models.CharacterModel;
import com.example.naidich.tom.tomsprojectapplication.core.services.CatchEmAllService;
import com.example.naidich.tom.tomsprojectapplication.ui.handlers.MusicPlayerHandler;
import com.example.naidich.tom.tomsprojectapplication.ui.helpers.CharacterAnimationHelper;
import com.example.naidich.tom.tomsprojectapplication.ui.helpers.IntentExtraHelper;
import com.example.naidich.tom.tomsprojectapplication.ui.helpers.IntentFactoryHelper;

import java.util.Locale;
import java.util.concurrent.Callable;

import static com.example.naidich.tom.tomsprojectapplication.core.enums.Background.MAX_BACKGROUNDS;
import static com.example.naidich.tom.tomsprojectapplication.core.enums.CharacterSkin.SkinType.Char1;
import static com.example.naidich.tom.tomsprojectapplication.core.enums.CharacterSkin.SkinType.Char2;
import static com.example.naidich.tom.tomsprojectapplication.core.enums.CharacterSkin.SkinType.Char3;
import static com.example.naidich.tom.tomsprojectapplication.core.enums.CharacterSkin.SkinType.Enemy1;
import static com.example.naidich.tom.tomsprojectapplication.core.enums.CharacterSkin.SkinType.Enemy2;
import static com.example.naidich.tom.tomsprojectapplication.core.enums.CharacterSkin.SkinType.Enemy3;

public class WarGameSelectionActivity extends AppCompatActivity {
    private static final int CHARACTER_ANIM_REFRESH_RATE = 100;

    private int _selectedBackgroundIndex = 1;

    private final CharacterModel _playerChar = new CharacterModel(Char1, CharacterAction.ActionType.Idle);
    private final CharacterModel _enemyChar = new CharacterModel(Enemy1, CharacterAction.ActionType.Idle);

    private final CharacterAnimationHelper _animationHelper = new CharacterAnimationHelper(WarGameSelectionActivity.this);

    private final MusicPlayerHandler _musicHandler = new MusicPlayerHandler(WarGameSelectionActivity.this);

    private final Handler _mCharacterAnimationHandler = new Handler();
    private final Runnable _mCharacterAnimate = new Runnable() {
        @Override
        public void run() {
            try {
                updateCharacterAnimation(_playerChar);
                updateCharacterAnimation(_enemyChar);
            } finally {
                _mCharacterAnimationHandler.postDelayed(_mCharacterAnimate, CHARACTER_ANIM_REFRESH_RATE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_war_game_selection);

        handleMainButtonEvents();

        setLocale(getSavedSelectedLanguage());
    }

    @Override
    protected void onPause(){
        super.onPause();
        _musicHandler.stopAllMusic();
        stopCharactersAnimation();
    }

    @Override
    protected void onResume(){
        super.onResume();
        _musicHandler.playBackgroundMusic(R.raw.character_selection);
        startCharactersAnimation();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        _musicHandler.stopAllMusic();
        stopCharactersAnimation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        _musicHandler.stopAllMusic();
        stopCharactersAnimation();
    }

    private void startCharactersAnimation(){
        _mCharacterAnimate.run();
    }

    private void stopCharactersAnimation(){
        _mCharacterAnimationHandler.removeCallbacks(_mCharacterAnimate);
    }

    private void handleMainButtonEvents(){
        // Handle 'Switch' button for Player Character
        Button mButtonSwitchPlayerChar = findViewById(R.id.buttonSwitchPlayerChar);
        mButtonSwitchPlayerChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _playerChar.setCurrentSkin(getNextCharacterSkin(_playerChar));
                _musicHandler.playEventSound(R.raw.click, null);
            }
        });

        // Handle 'Switch' button for Enemy Character
        Button mButtonSwitchEnemyChar = findViewById(R.id.buttonSwitchEnemyChar);
        mButtonSwitchEnemyChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _enemyChar.setCurrentSkin(getNextCharacterSkin(_enemyChar));
                _musicHandler.playEventSound(R.raw.click, null);
            }
        });

        // Handle 'Switch' button for Background
        Button mButtonSwitchBackground = findViewById(R.id.buttonSwitchBackground);
        mButtonSwitchBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView backgroundImageView = findViewById(R.id.imageViewSelectedBackground);
                _selectedBackgroundIndex = _selectedBackgroundIndex == MAX_BACKGROUNDS ? 1 :_selectedBackgroundIndex + 1;
                Background.BackgroundType newBackgroundType = Background.getBackgroundTypeByIndex(_selectedBackgroundIndex);
                backgroundImageView.setImageResource(Background.getBackgroundResourceByType(newBackgroundType));
                _musicHandler.playEventSound(R.raw.click, null);
            }
        });

        // Handle 'Start Game' button
        final Button mButtonStartGame = findViewById(R.id.buttonStartGame);
        mButtonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _musicHandler.playEventSound(R.raw.click2, new Callable<Integer>() {
                    public Integer call() {
                        Intent warGameFightIntent = IntentFactoryHelper.getIntent(WarGameSelectionActivity.this, IntentFactoryHelper.IntentType.WarGameFight);


                        Background.BackgroundType bgType = Background.getBackgroundTypeByIndex(_selectedBackgroundIndex);
                        warGameFightIntent.putExtra(IntentExtraHelper.BACKGROUND_RESOURCE_ID, Background.getBackgroundResourceByType(bgType));
                        warGameFightIntent.putExtra(IntentExtraHelper.PLAYER_CHARACTER, _playerChar);
                        warGameFightIntent.putExtra(IntentExtraHelper.ENEMY_CHARACTER, _enemyChar);

                        startActivity(warGameFightIntent);

                        return 1;
                    }
                });
            }
        });
    }

    private void updateCharacterAnimation(CharacterModel character){
        character.setNextActionStep();

        final int newCharResource = _animationHelper.getCharacterResourceId(
                character.getCurrentSkin(),
                character.getCurrentActionType(),
                character.getCurrentActionStep());

        ImageView charImageView = findViewById(character == _playerChar ?
                R.id.imageViewSelectedCharacter : R.id.imageViewSelectedEnemy);

        charImageView.setImageResource(newCharResource);
    }

    private CharacterSkin.SkinType getNextCharacterSkin(CharacterModel character){
        CharacterSkin.SkinType currentSkin = character.getCurrentSkin();
        switch(currentSkin){
            case Char1: return Char2;
            case Char2: return Char3;
            case Char3: return Char1;

            case Enemy1: return Enemy2;
            case Enemy2: return Enemy3;
            case Enemy3: return Enemy1;
            default: return Char1;
        }
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
        ((TextView)findViewById(R.id.textView_charSelection)).setText(R.string.character_selection);
        ((TextView)findViewById(R.id.textView_enemySelection)).setText(R.string.enemy_selection);
        ((TextView)findViewById(R.id.textView_backgroundSelection)).setText(R.string.background_selection);

        ((Button)findViewById(R.id.buttonSwitchPlayerChar)).setText(R.string.switch_text);
        ((Button)findViewById(R.id.buttonSwitchEnemyChar)).setText(R.string.switch_text);
        ((Button)findViewById(R.id.buttonSwitchBackground)).setText(R.string.switch_text);
        ((Button)findViewById(R.id.buttonStartGame)).setText(R.string.start_game);
    }
}
