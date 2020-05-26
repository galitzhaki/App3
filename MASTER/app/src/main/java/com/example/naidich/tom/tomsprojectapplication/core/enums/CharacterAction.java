package com.example.naidich.tom.tomsprojectapplication.core.enums;

public final class CharacterAction {
    public enum ActionType{
        Idle,
        Walk,
        Attack,
        Hurt,
        Die
    }

    public static final String getActionTypeName(ActionType actionType){
        switch(actionType){
            case Idle: return "idle";
            case Walk: return "walk";
            case Attack: return "attack";
            case Hurt: return "hurt";
            case Die: return "die";
            default: return "";
        }
    }
}
