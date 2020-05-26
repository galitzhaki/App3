package com.example.naidich.tom.tomsprojectapplication.ui.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.naidich.tom.tomsprojectapplication.R;
import com.example.naidich.tom.tomsprojectapplication.core.enums.Game;
import com.example.naidich.tom.tomsprojectapplication.core.enums.MoveDirection;
import com.example.naidich.tom.tomsprojectapplication.core.helpers.LogHelper;
import com.example.naidich.tom.tomsprojectapplication.core.models.CharacterModel;
import com.example.naidich.tom.tomsprojectapplication.core.providers.ScoreModelProvider;
import com.example.naidich.tom.tomsprojectapplication.ui.handlers.MusicPlayerHandler;
import com.example.naidich.tom.tomsprojectapplication.ui.helpers.CharacterAnimationHelper;
import com.example.naidich.tom.tomsprojectapplication.ui.helpers.IntentExtraHelper;

public class WarGameFightActivity extends AppCompatActivity {
    private enum IntroStage{
        Blank,
        PlayerCharacterTooltipDisplay,
        EnemyCharacterTooltipDisplay,
        FightAnimation,
        IntoEnded
    }
    private CharacterModel _playerChar,
            _enemyChar;

    private boolean _isFighting = false;
    private IntroStage _gameIntroStage = IntroStage.Blank;

    private final static float BUTTON_CLICKED_ALPHA = 0.2f;
    private final static float BUTTON_UNCLICKED_ALPHA = 0.3f;
    private final static int CHARACTER_ANIM_REFRESH_RATE = 100;
    private final static int CHARACTER_MOVE_REFRESH_RATE = 100;
    private final static int MOVE_DISTANCE = 100;
    private final static int TOOLTIPS_DISPLAY_RATE = 3500;

    private SharedPreferences _sharedPreferences;
    private ScoreModelProvider _scoreProvider;

    private final CharacterAnimationHelper _animationHelper = new CharacterAnimationHelper(WarGameFightActivity.this);
    private final MusicPlayerHandler _musicHandler = new MusicPlayerHandler(WarGameFightActivity.this);

    private final Handler _mCharacterAnimationHandler = new Handler();
    private final Runnable _mCharacterAnimate = new Runnable() {
        @Override
        public void run() {
            try {
                updatePlayerCharacterAnimation();
                updateEnemyCharacterAnimation();
            } finally {
                if(!_enemyChar.isDead())
                    _mCharacterAnimationHandler .postDelayed(_mCharacterAnimate, CHARACTER_ANIM_REFRESH_RATE);
            }
        }
    };

    private final Handler _mCharacterMoveHandler = new Handler();
    private final Runnable _mCharacterMover = new Runnable() {
        @Override
        public void run() {
            try {
                if(!_isFighting) return;
                moveCharacter(_playerChar, findViewById(R.id.imageViewSelectedCharacter));
                moveCharacter(_enemyChar, findViewById(R.id.imageViewSelectedEnemy));
            } finally {
                _mCharacterMoveHandler .postDelayed(_mCharacterMover, CHARACTER_MOVE_REFRESH_RATE);
            }
        }
    };

    private final Handler _mIntroHandler = new Handler();
    private final Runnable _mIntroRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                switch(_gameIntroStage){
                    case Blank:
                        // Display player character tooltips.

                        findViewById(R.id.imageViewPlayerCharTooltip).setVisibility(View.VISIBLE);
                        findViewById(R.id.textViewPlayerCharTooltip).setVisibility(View.VISIBLE);

                        _gameIntroStage = IntroStage.PlayerCharacterTooltipDisplay;
                        break;

                    case PlayerCharacterTooltipDisplay:
                        // Display enemy character tooltips.

                        findViewById(R.id.imageViewPlayerCharTooltip).setVisibility(View.GONE);
                        findViewById(R.id.textViewPlayerCharTooltip).setVisibility(View.GONE);

                        findViewById(R.id.imageViewEnemyCharTooltip).setVisibility(View.VISIBLE);
                        findViewById(R.id.textViewEnemyCharTooltip).setVisibility(View.VISIBLE);

                        _gameIntroStage = IntroStage.EnemyCharacterTooltipDisplay;
                        break;

                    case EnemyCharacterTooltipDisplay:
                        // Display fight image.
                        findViewById(R.id.imageViewEnemyCharTooltip).setVisibility(View.GONE);
                        findViewById(R.id.textViewEnemyCharTooltip).setVisibility(View.GONE);

                        findViewById(R.id.imageViewFight).setVisibility(View.VISIBLE);
                        _musicHandler.playEventSound(R.raw.fight, null);

                        _gameIntroStage = IntroStage.FightAnimation;
                        break;

                    case FightAnimation:
                        findViewById(R.id.imageViewFight).setVisibility(View.GONE);

                        _gameIntroStage = IntroStage.IntoEnded;
                        break;
                }
            } finally {
                if(_gameIntroStage != IntroStage.IntoEnded)
                    _mIntroHandler .postDelayed(_mIntroRunnable, TOOLTIPS_DISPLAY_RATE);
                else
                    _isFighting = true;
            }
        }
    };

    private void updateCharacterAnimation(CharacterModel character){
        if(!character.isDead())
            character.setNextActionStep();

        final int newCharResource = _animationHelper.getCharacterResourceId(
                character.getCurrentSkin(),
                character.getCurrentActionType(),
                character.getCurrentActionStep());

        ImageView charImageView = findViewById(character == _playerChar ?
                R.id.imageViewSelectedCharacter : R.id.imageViewSelectedEnemy);

        charImageView.setImageResource(newCharResource);
    }

    private void updatePlayerCharacterAnimation(){
        updateCharacterAnimation(_playerChar);
    }
    private void updateEnemyCharacterAnimation(){
        updateCharacterAnimation(_enemyChar);

        if(_isFighting && _playerChar.isAttacking() && validateEnemyInDamageRange()){

            if(!_enemyChar.isDying())
                _enemyChar.startDying();

            _musicHandler.playEventSound(R.raw.attack_hit, null);

            _enemyChar.getAttacked();
            LogHelper.logDebug("Enemy char is getting attacked. Current health: " + _enemyChar.getCurrentHealth());

            if(_enemyChar.getCurrentHealth() == 0){
                _isFighting = false;

                _playerChar.startIdling();
                _enemyChar.die();

                // Display alert message for player.
                final String youWinMessage = getResources().getString(R.string.enemy_is_dead);
                final String dialogTitle = getResources().getString(R.string.war_game_you_win_title);

                _scoreProvider.addScore(Game.GameType.WarGame, getResources().getString(R.string.war_game_score_msg));

                _musicHandler.playEventSound(R.raw.game_over, null);

                new AlertDialog.Builder(this)
                        .setTitle(dialogTitle)
                        .setMessage(youWinMessage)
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                onBackPressed();
                            }
                        }).show();
            }
        }
        else {
            if(_playerChar.isAttacking())
                _musicHandler.playEventSound(R.raw.attack, null);

            if(!_enemyChar.isIdling())
                _enemyChar.startIdling();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_war_game_fight);

        _sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        _scoreProvider = new ScoreModelProvider(_sharedPreferences);

        // Set selected background.
        int backgroundResourceId = getIntent().getIntExtra(IntentExtraHelper.BACKGROUND_RESOURCE_ID, -1);
        findViewById(R.id.war_game_fight_background).setBackgroundResource(backgroundResourceId);

        _playerChar = (CharacterModel)getIntent().getSerializableExtra(IntentExtraHelper.PLAYER_CHARACTER);
        _enemyChar = (CharacterModel)getIntent().getSerializableExtra(IntentExtraHelper.ENEMY_CHARACTER);

        hideSubViews();
        handleMainButtonEvents();
    }

    @Override
    protected void onPause(){
        super.onPause();
        _musicHandler.stopAllMusic();
        stopCharactersAnimation();
        stopCharacterMoving();
        stopAnimationIntro();
    }

    @Override
    protected void onResume(){
        super.onResume();
        _musicHandler.playBackgroundMusic(R.raw.battle);
        startCharactersAnimation();
        startCharacterMoving();
        startAnimationIntro();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        _musicHandler.stopAllMusic();
        stopCharactersAnimation();
        stopCharacterMoving();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        _musicHandler.stopAllMusic();
        stopCharactersAnimation();
        stopCharacterMoving();
    }

    private void startCharactersAnimation(){
        _mCharacterAnimate.run();
    }

    private void startAnimationIntro(){
        _mIntroRunnable.run();
    }
    private void stopAnimationIntro(){
        _mIntroHandler.removeCallbacks(_mIntroRunnable);
    }

    private void startCharacterMoving(){
        _mCharacterMover.run();
    }

    private void stopCharactersAnimation(){
        _mCharacterAnimationHandler.removeCallbacks(_mCharacterAnimate);
    }
    private void stopCharacterMoving(){
        _mCharacterMoveHandler.removeCallbacks(_mCharacterMover);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void handleMainButtonEvents(){
        // Handle 'RIGHT' button
        ImageButton mButtonRight = findViewById(R.id.imageButtonRight);
        mButtonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!_isFighting) return false;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LogHelper.logDebug("ACTION DOWN");
                        if(!_playerChar.isIdling()) return false;

                        _playerChar.setMovingDirection(MoveDirection.RIGHT);
                        _playerChar.startWalking();

                        findViewById(R.id.imageButtonRight).setAlpha(BUTTON_CLICKED_ALPHA);
                        return true;

                    case MotionEvent.ACTION_UP:
                        LogHelper.logDebug("ACTION UP");
                        _playerChar.setMovingDirection(MoveDirection.STILL);
                        _playerChar.startIdling();

                        findViewById(R.id.imageButtonRight).setAlpha(BUTTON_UNCLICKED_ALPHA);
                        return true;
                }
                return false;
            }
        });

        // Handle 'LEFT' button
        ImageButton mButtonLeft = findViewById(R.id.imageButtonLeft);
        mButtonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!_isFighting) return false;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LogHelper.logDebug("ACTION DOWN");
                        if(!_playerChar.isIdling()) return false;

                        _playerChar.setMovingDirection(MoveDirection.LEFT);
                        _playerChar.startWalking();

                        findViewById(R.id.imageButtonLeft).setAlpha(BUTTON_CLICKED_ALPHA);
                        return true;

                    case MotionEvent.ACTION_UP:
                        LogHelper.logDebug("ACTION UP");
                        _playerChar.setMovingDirection(MoveDirection.STILL);
                        _playerChar.startIdling();

                        findViewById(R.id.imageButtonLeft).setAlpha(BUTTON_UNCLICKED_ALPHA);
                        return true;
                }
                return false;
            }
        });

        // Handle 'ATTACK' button
        ImageButton mButtonAttack = findViewById(R.id.imageButtonAttack);
        mButtonAttack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!_isFighting) return false;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LogHelper.logDebug("ACTION DOWN");
                        if(!_playerChar.isIdling()) return false;

                        _playerChar.startAttacking();

                        findViewById(R.id.imageButtonAttack).setAlpha(BUTTON_CLICKED_ALPHA);
                        return true;

                    case MotionEvent.ACTION_UP:
                        LogHelper.logDebug("ACTION UP");
                        _playerChar.startIdling();

                        findViewById(R.id.imageButtonAttack).setAlpha(BUTTON_UNCLICKED_ALPHA);
                        return true;
                }
                return false;
            }
        });
    }

    private void moveCharacter(CharacterModel character, View charView){
        int moveDir = character.getMovingDirection();

        if(moveDir == MoveDirection.STILL)
            return;

        final float curViewX = charView.getX();
        final int MIN_SCREEN_WIDTH = 0;
        final int MAX_SCREEN_WIDTH = findViewById(R.id.war_game_fight_background).getWidth() - 400;
        LogHelper.logDebug("Layout width: " + MAX_SCREEN_WIDTH + " | Player character position: " + curViewX);

        if(moveDir == MoveDirection.RIGHT) {
            if(curViewX + MOVE_DISTANCE > MAX_SCREEN_WIDTH) return;

            charView.setX(curViewX + MOVE_DISTANCE);
            charView.setScaleX(1);
        } else{
            if(curViewX - MOVE_DISTANCE < MIN_SCREEN_WIDTH) return;

            charView.setX(curViewX - MOVE_DISTANCE);
            charView.setScaleX(-1);
        }
    }

    private void hideSubViews(){
        findViewById(R.id.imageViewPlayerCharTooltip).setVisibility(View.INVISIBLE);
        findViewById(R.id.textViewPlayerCharTooltip).setVisibility(View.INVISIBLE);
        findViewById(R.id.imageViewEnemyCharTooltip).setVisibility(View.INVISIBLE);
        findViewById(R.id.textViewEnemyCharTooltip).setVisibility(View.INVISIBLE);
        findViewById(R.id.imageViewFight).setVisibility(View.INVISIBLE);
    }

    private boolean validateEnemyInDamageRange(){
        final ImageView playerCharView = findViewById(R.id.imageViewSelectedCharacter);
        final ImageView enemyCharView = findViewById(R.id.imageViewSelectedEnemy);
        final int MAX_DAMAGE_DISTANCE = 300;

        final boolean isFacingEnemy = playerCharView.getScaleX() != enemyCharView.getScaleX();
        final boolean isCloseToEnemy = Math.abs(playerCharView.getX() - enemyCharView.getX()) <= MAX_DAMAGE_DISTANCE;

        LogHelper.logDebug("validateEnemyInDamageRange:: isFacingEnemy:" + isFacingEnemy + ", isCloseToEnemy: " + isCloseToEnemy);

        return isFacingEnemy && isCloseToEnemy;
    }
}
