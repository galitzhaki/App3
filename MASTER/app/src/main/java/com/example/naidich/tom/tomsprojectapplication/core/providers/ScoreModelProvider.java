package com.example.naidich.tom.tomsprojectapplication.core.providers;

import android.content.SharedPreferences;

import com.example.naidich.tom.tomsprojectapplication.core.enums.Game;
import com.example.naidich.tom.tomsprojectapplication.core.models.ScoreModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ScoreModelProvider {
    private static final String SCORES_KEY = "Scores";
    private static List<ScoreModel> _scores;
    private SharedPreferences _sharedPreferences;

    public ScoreModelProvider(SharedPreferences sharedPreferences){
        _sharedPreferences = sharedPreferences;

        if(_scores == null)
            loadScores();
    }

    public List<ScoreModel> getScores(){ return this._scores; }

    public void loadScores(){
        // Load scores from Shared Preferences;
        String json = _sharedPreferences.getString(SCORES_KEY, "");

        Type listType = new TypeToken<ArrayList<ScoreModel>>(){}.getType();
        _scores = new Gson().fromJson(json, listType);

        if(_scores == null) _scores = new ArrayList<>();
    }

    public void addScore(Game.GameType gameType, String message){
        String gameId = Game.getGameIdByType(gameType);

        Calendar calendar = Calendar. getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String date = dateFormat.format(calendar.getTime());

        ScoreModel score = new ScoreModel(gameId, date, message);

        _scores.add(score);

        // Save scores to Shared Preferences.
        SharedPreferences.Editor prefsEditor = _sharedPreferences.edit();
        String json = new Gson().toJson(_scores);
        prefsEditor.putString(SCORES_KEY, json);
        prefsEditor.commit();
    }
}
