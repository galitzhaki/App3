package com.example.naidich.tom.tomsprojectapplication.core.models;

import com.example.naidich.tom.tomsprojectapplication.core.enums.CharacterAction;
import com.example.naidich.tom.tomsprojectapplication.core.enums.CharacterSkin;
import com.example.naidich.tom.tomsprojectapplication.core.enums.MoveDirection;
import com.example.naidich.tom.tomsprojectapplication.ui.helpers.CharacterAnimationHelper;

import java.io.Serializable;

public class CharacterModel implements Serializable {
    private static final int ATTACK_DAMAGE = 10;

    private CharacterSkin.SkinType _currentSkin;
    private int _currentHealth;
    private CharacterAction.ActionType _currentActionType;
    private int _currentActionStep;
    private int _movingDirection;

    public CharacterModel(CharacterSkin.SkinType _currentSkin, CharacterAction.ActionType _currentActionType) {
        this._currentSkin = _currentSkin;
        this._currentActionType = _currentActionType;
        this._currentHealth = 100;
        this._currentActionStep = 0;
        this._movingDirection = MoveDirection.STILL;
    }

    public int getMovingDirection(){
        return _movingDirection;
    }
    public void setMovingDirection(int dir){
        _movingDirection = dir;
    }

    public CharacterAction.ActionType getCurrentActionType(){
        return _currentActionType;
    }

    public int getCurrentActionStep(){
        return _currentActionStep;
    }

    public CharacterSkin.SkinType getCurrentSkin(){
        return _currentSkin;
    }

    public void setCurrentSkin(CharacterSkin.SkinType skin){
        _currentSkin = skin;
    }

    public void setNextActionStep(){
        _currentActionStep = _currentActionStep == CharacterAnimationHelper.MAX_ANIM_STEPS
                ? 0 : _currentActionStep + 1;
    }



    public void startIdling(){
        _currentActionStep = 0;
        _currentActionType = CharacterAction.ActionType.Idle;
    }

    public void startWalking(){
        _currentActionStep = 1;
        _currentActionType = CharacterAction.ActionType.Walk;
    }

    public void startAttacking(){
        _currentActionStep = 1;
        _currentActionType = CharacterAction.ActionType.Attack;
    }

    public boolean isAttacking() { return _currentActionType == CharacterAction.ActionType.Attack; }
    public boolean isIdling() { return _currentActionType == CharacterAction.ActionType.Idle; }
    public boolean isDying() { return _currentActionType == CharacterAction.ActionType.Die; }

    public void getAttacked(){
        _currentHealth = _currentHealth > 0 ? _currentHealth-ATTACK_DAMAGE : 0;
    }

    public void die(){
        _currentHealth = 0;
        _currentActionStep = 7;
        _currentActionType = CharacterAction.ActionType.Die;
    }

    public boolean isDead(){return _currentHealth == 0;}

    public int getCurrentHealth(){return _currentHealth;}

    public void startDying(){
        _currentActionStep = 1;
        _currentActionType = CharacterAction.ActionType.Die;
    }
}
