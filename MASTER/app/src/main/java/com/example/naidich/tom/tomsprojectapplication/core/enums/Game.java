package com.example.naidich.tom.tomsprojectapplication.core.enums;

import com.example.naidich.tom.tomsprojectapplication.R;

public final class Game {
    public enum GameType{
        TicTacToe,
        CatchEmAll,
        WarGame,
        Meme
    }

    public static final String getGameIdByType(GameType type){
        switch(type){
            case TicTacToe: return "tictactoe";
            case CatchEmAll: return "catchemall";
            case WarGame: return "wargame";
            case Meme: return "meme";
            default: return "";
        }
    }

    public static final GameType getGameTypeById(String id){
        switch(id){
            case "tictactoe": return GameType.TicTacToe;
            case "catchemall": return GameType.CatchEmAll;
            case "wargame": return GameType.WarGame;
            case "meme": return GameType.Meme;
            default: return GameType.CatchEmAll; // Default
        }
    }

    public static final int getGameTitleByType(GameType type){
        switch(type){
            case TicTacToe: return R.string.tic_tac_toe;
            case CatchEmAll: return R.string.button_catch_em_all;
            case WarGame: return R.string.button_war_game;
            case Meme:return R.string.button_meme;
            default: return 0;
        }
    }

    public static final int getGameImageByType(GameType type){
        switch(type){
            case TicTacToe: return R.drawable.tic_tac_toe32;
            case CatchEmAll: return R.drawable.pokeball32;
            case WarGame: return R.drawable.war_game32;
            case Meme: return R.drawable.meme_img;
            default: return 0;
        }
    }
}
