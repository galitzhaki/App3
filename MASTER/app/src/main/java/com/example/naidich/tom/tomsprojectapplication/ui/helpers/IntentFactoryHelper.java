package com.example.naidich.tom.tomsprojectapplication.ui.helpers;

import android.content.Context;
import android.content.Intent;

import com.example.naidich.tom.tomsprojectapplication.core.helpers.LogHelper;
import com.example.naidich.tom.tomsprojectapplication.ui.activities.CatchEmAllActivity;
import com.example.naidich.tom.tomsprojectapplication.ui.activities.MainActivity;
import com.example.naidich.tom.tomsprojectapplication.ui.activities.ScoreboardActivity;
import com.example.naidich.tom.tomsprojectapplication.ui.activities.TicTacToeActivity;
import com.example.naidich.tom.tomsprojectapplication.ui.activities.WarGameFightActivity;
import com.example.naidich.tom.tomsprojectapplication.ui.activities.WarGameSelectionActivity;

public final class IntentFactoryHelper {
    public enum IntentType {
        Main,
        Login,
        TicTacToe,
        CatchEmAll,
        WarGameSelection,
        WarGameFight,
        Scores
    }

    public static final Intent getIntent(Context context, IntentType type){
        LogHelper.logDebug("Starting new intent: " + type.toString());
        switch(type){
            case Main:  return new Intent(context, MainActivity.class);
            case TicTacToe: return new Intent(context, TicTacToeActivity.class);
            case CatchEmAll: return new Intent(context, CatchEmAllActivity.class);
            case WarGameSelection: return new Intent(context, WarGameSelectionActivity.class);
            case WarGameFight: return new Intent(context, WarGameFightActivity.class);
            case Scores: return new Intent(context, ScoreboardActivity.class);
            default: return null;
        }
    }
}
