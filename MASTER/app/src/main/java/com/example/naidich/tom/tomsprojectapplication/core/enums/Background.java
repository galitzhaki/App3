package com.example.naidich.tom.tomsprojectapplication.core.enums;

import com.example.naidich.tom.tomsprojectapplication.R;

import static com.example.naidich.tom.tomsprojectapplication.core.enums.Background.BackgroundType.War1;
import static com.example.naidich.tom.tomsprojectapplication.core.enums.Background.BackgroundType.War2;
import static com.example.naidich.tom.tomsprojectapplication.core.enums.Background.BackgroundType.War3;
import static com.example.naidich.tom.tomsprojectapplication.core.enums.Background.BackgroundType.War4;

public class Background {
    public static final int MAX_BACKGROUNDS = 4;

    public enum BackgroundType{
        War1,
        War2,
        War3,
        War4
    }

    public static final int getBackgroundResourceByType(BackgroundType bgType){
        switch(bgType){
            case War1: return R.drawable.war_background1;
            case War2: return R.drawable.war_background2;
            case War3: return R.drawable.war_background3;
            case War4: return R.drawable.war_background4;
            default: return R.drawable.war_background1;
        }
    }

    public static final BackgroundType getBackgroundTypeByIndex(int index){
        switch(index){
            case 1: return War1;
            case 2: return War2;
            case 3: return War3;
            case 4: return War4;
            default: return War1;
        }
    }
}
