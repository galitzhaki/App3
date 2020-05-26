package com.example.naidich.tom.tomsprojectapplication.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.naidich.tom.tomsprojectapplication.R;
import com.example.naidich.tom.tomsprojectapplication.core.enums.Game;
import com.example.naidich.tom.tomsprojectapplication.core.providers.ScoreModelProvider;
import com.example.naidich.tom.tomsprojectapplication.core.services.TicTacToeBoardService;
import com.example.naidich.tom.tomsprojectapplication.ui.handlers.MusicPlayerHandler;
import com.example.naidich.tom.tomsprojectapplication.ui.utils.BoardView;

public class TicTacToeActivity extends AppCompatActivity {

    private BoardView boardView;
    private TicTacToeBoardService gameEngine;
    private final MusicPlayerHandler _musicHandler = new MusicPlayerHandler(TicTacToeActivity.this);
    private SharedPreferences _sharedPreferences;
    private ScoreModelProvider _scoreProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        // Init board and its features
        boardView = findViewById(R.id.board);
        gameEngine = new TicTacToeBoardService();
        boardView.setGameEngine(gameEngine);
        boardView.setMusicHandler(_musicHandler);
        boardView.setMainActivity(this);

        // Handle Start New Game button
        Button mNewGameButton = findViewById(R.id.button_new_game);
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });

        // Handle 'Back' button
        Button mBackButton = findViewById(R.id.button_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        _sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        _scoreProvider = new ScoreModelProvider(_sharedPreferences);
    }

    @Override
    protected void onPause(){
        super.onPause();
        _musicHandler.stopAllMusic();
    }

    @Override
    protected void onResume(){
        super.onResume();
        _musicHandler.playBackgroundMusic(R.raw.tictactoe_music);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        _musicHandler.stopAllMusic();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _musicHandler.stopAllMusic();
    }

    public void gameEnded(char c) {
        String resultMessage = getResources().getString(isTie(c) ?
                R.string.game_ended_tie : R.string.game_ended_win);

        final String playerText = getResources().getString(R.string.player);
        final String computerText = getResources().getString(R.string.computer);

        if(!isTie(c))
            resultMessage = resultMessage.replace("{PLAYER}",
                    c + (c == TicTacToeBoardService.X_PLAYER_MARK ? (" ("+playerText+")") : (" ("+computerText+")")));

        _scoreProvider.addScore(Game.GameType.TicTacToe, resultMessage);

        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.tic_tac_toe)).
                setMessage(resultMessage).
                setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        newGame();
                    }
                }).show();
    }



    private boolean isTie(char c){
        return c == TicTacToeBoardService.T_TIE_MARK;
    }

    private void newGame() {
        gameEngine.newGame();
        boardView.invalidate();
    }
}
