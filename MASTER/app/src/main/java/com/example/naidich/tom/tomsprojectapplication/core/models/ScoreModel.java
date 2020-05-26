package com.example.naidich.tom.tomsprojectapplication.core.models;

import java.util.Date;

public class ScoreModel {
    public String id;
    private String gameId;
    private String dateTime;
    private String message;

    public ScoreModel() { }

    public ScoreModel(String id, String gameId, String dateTime, String message) {
        this.id = id;
        this.gameId = gameId;
        this.dateTime = dateTime;
        this.message = message;
    }

    public ScoreModel(String gameId, String dateTime, String message) {
        this.id = java.util.UUID.randomUUID().toString();
        this.gameId = gameId;
        this.dateTime = dateTime;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
