package com.example.naidich.tom.tomsprojectapplication.core.enums;
public final class CharacterSkin {
    public enum SkinType{
        Char1,
        Char2,
        Char3,
        Enemy1,
        Enemy2,
        Enemy3
    }

    public static final String getSkinTypeName(SkinType skinType){
        switch(skinType){
            case Char1: return "char1";
            case Char2: return "char2";
            case Char3: return "char3";
            case Enemy1: return "enemy1";
            case Enemy2: return "enemy2";
            case Enemy3: return "enemy3";
            default: return "";
        }
    }
}
