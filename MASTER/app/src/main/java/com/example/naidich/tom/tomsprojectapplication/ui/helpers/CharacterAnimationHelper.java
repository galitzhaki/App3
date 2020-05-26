package com.example.naidich.tom.tomsprojectapplication.ui.helpers;

import android.content.Context;

import com.example.naidich.tom.tomsprojectapplication.R;
import com.example.naidich.tom.tomsprojectapplication.core.enums.CharacterAction;
import com.example.naidich.tom.tomsprojectapplication.core.enums.CharacterSkin;

public class CharacterAnimationHelper {
    public static final int MAX_ANIM_STEPS = 6;
    private Context _context;

    public CharacterAnimationHelper(Context context){
        _context = context;
    }

    public int getCharacterResourceId(CharacterSkin.SkinType skinType, CharacterAction.ActionType actionType, int animStep) {
        String resourceName = getResourceNameByParameters(skinType, actionType, animStep);
        int resourceId = _context.getResources().getIdentifier(resourceName, "drawable", _context.getPackageName());
        return resourceId;
    }

    private String getResourceNameByParameters(CharacterSkin.SkinType skinType, CharacterAction.ActionType actionType, int animStep){
        //war_char1_idle_001
        //war_enemy1_idle_001
        String skinTypeName = CharacterSkin.getSkinTypeName(skinType);
        String actionTypeName = CharacterAction.getActionTypeName(actionType);
        String animStepName = "00" + animStep;
        return "war_" + skinTypeName + "_" + actionTypeName + "_" + animStepName;
    }
}
