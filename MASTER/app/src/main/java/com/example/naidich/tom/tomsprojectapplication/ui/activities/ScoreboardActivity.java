package com.example.naidich.tom.tomsprojectapplication.ui.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naidich.tom.tomsprojectapplication.R;
import com.example.naidich.tom.tomsprojectapplication.core.enums.Game;
import com.example.naidich.tom.tomsprojectapplication.core.models.ScoreModel;
import com.example.naidich.tom.tomsprojectapplication.core.providers.ScoreModelProvider;
import com.example.naidich.tom.tomsprojectapplication.ui.handlers.MusicPlayerHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardActivity extends AppCompatActivity {
    private String _selectedGameId = Game.getGameIdByType(Game.GameType.TicTacToe);
    private final MusicPlayerHandler _musicHandler = new MusicPlayerHandler(ScoreboardActivity.this);
    private SharedPreferences _sharedPreferences;
    private ScoreModelProvider _scoreProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        _sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        _scoreProvider = new ScoreModelProvider(_sharedPreferences);

        handleUIEvents();
        populateScoreListView();
    }

    private void populateScoreListView(){
        String selectedGameId = getSelectedGameId();

        List<ScoreModel> filteredScores = new ArrayList<>();
        for(ScoreModel score: _scoreProvider.getScores())
            if(score.getGameId().equals(selectedGameId)) filteredScores.add(score);

        Collections.reverse(filteredScores);

        ListView listView = findViewById(R.id.listViewScoreboard);
        CustomScoreListViewAdapter adapter = new CustomScoreListViewAdapter(filteredScores);
        listView.setAdapter(adapter);
    }

    private String getSelectedGameId(){
        RadioButton rb_ticTacToe = findViewById(R.id.radioButton_ticTacToe);
        RadioButton rb_catchEmAll = findViewById(R.id.radioButton_catchEmAll);
        RadioButton rb_warGame = findViewById(R.id.radioButton_warGame);
        RadioButton rb_pokemonGame = findViewById(R.id.radioButton_pokemon);

        return  rb_ticTacToe.isChecked() ? Game.getGameIdByType(Game.GameType.TicTacToe) :
                rb_catchEmAll.isChecked() ? Game.getGameIdByType(Game.GameType.Meme) :
                rb_warGame.isChecked() ? Game.getGameIdByType(Game.GameType.WarGame) :
                        rb_pokemonGame.isChecked() ? Game.getGameIdByType(Game.GameType.CatchEmAll) :
                "";
    }

    @Override
    protected void onPause(){
        super.onPause();
        _musicHandler.stopAllMusic();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        _musicHandler.stopAllMusic();
     //  onDestroy();
    //    finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        _musicHandler.stopAllMusic();

        finish();
    }

    class CustomScoreListViewAdapter extends BaseAdapter
    {
        private final List<ScoreModel> items;

        public CustomScoreListViewAdapter(List<ScoreModel> items) { this.items = items; }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ScoreModel score = (ScoreModel) getItem(position);

            convertView = getLayoutInflater().inflate(R.layout.scoreboard_item, null);

            ImageView gameImageView = convertView.findViewById(R.id.imageView_gameIcon);
            TextView gameTitleTextView = convertView.findViewById(R.id.textView_gameTitle);
            TextView dateTextView = convertView.findViewById(R.id.textView_date);
            TextView messageTextView = convertView.findViewById(R.id.textView_message);

            Game.GameType gameType = Game.getGameTypeById(score.getGameId());

            gameImageView.setImageResource(Game.getGameImageByType(gameType));
            gameTitleTextView.setText(Game.getGameTitleByType(gameType));
            dateTextView.setText(score.getDateTime());
            messageTextView.setText(score.getMessage());

            return convertView;
        }
    }

    private void handleUIEvents(){
        RadioButton radioButton_catchEmAll = findViewById(R.id.radioButton_catchEmAll);
        radioButton_catchEmAll.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                _musicHandler.playEventSound(R.raw.click, null);
                _selectedGameId = Game.getGameIdByType(Game.GameType.Meme);
                populateScoreListView();
            }
        });

        RadioButton radioButton_warGame = findViewById(R.id.radioButton_warGame);
        radioButton_warGame.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                _musicHandler.playEventSound(R.raw.click, null);
                _selectedGameId = Game.getGameIdByType(Game.GameType.WarGame);
                populateScoreListView();
            }
        });

        RadioButton radioButton_ticTacToe = findViewById(R.id.radioButton_ticTacToe);
        radioButton_ticTacToe.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                _musicHandler.playEventSound(R.raw.click, null);
                _selectedGameId = Game.getGameIdByType(Game.GameType.TicTacToe);
                populateScoreListView();
            }
        });


        RadioButton radioButton_pokemon = findViewById(R.id.radioButton_pokemon);
        radioButton_pokemon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                _musicHandler.playEventSound(R.raw.click, null);
                _selectedGameId = Game.getGameIdByType(Game.GameType.CatchEmAll);
                populateScoreListView();
            }
        });
    }

}
