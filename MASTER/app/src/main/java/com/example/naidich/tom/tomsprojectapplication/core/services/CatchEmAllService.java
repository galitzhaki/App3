package com.example.naidich.tom.tomsprojectapplication.core.services;

import com.example.naidich.tom.tomsprojectapplication.core.helpers.PokemonGeneratorHelper;
import java.util.Random;
import static com.example.naidich.tom.tomsprojectapplication.core.services.CatchEmAllService.CatchResult.CatchFailed;
import static com.example.naidich.tom.tomsprojectapplication.core.services.CatchEmAllService.CatchResult.Caught;
import static com.example.naidich.tom.tomsprojectapplication.core.services.CatchEmAllService.CatchResult.NoBalls;
import static com.example.naidich.tom.tomsprojectapplication.core.services.CatchEmAllService.CatchResult.NoPokemons;

public class CatchEmAllService {
    public enum CatchResult{
        NoPokemons,
        NoBalls,
        Caught,
        CatchFailed,
        AllPokemonsAreCaught
    }

    private static final int FAILURE_CATCH_CHANCE = 33; // 33%
    public static final int DEFAULT_BALLS_COUNT = 5;
    private static final Random _rnd = new Random();

    private boolean isCatching;
    private int caughtPokemons;
    private int availableBalls;
    private int availablePokemons;

    public CatchEmAllService(){
        super();
        caughtPokemons = 0;
        availablePokemons = 0;
        availableBalls = DEFAULT_BALLS_COUNT;
        isCatching = false;
    }

    public boolean canGeneratePokemon(){
        return availablePokemons < PokemonGeneratorHelper.MAX_POKEMON_TYPES;
    }
    public int getCaughtPokemons(){ return caughtPokemons; }
    public int getAvailableBalls(){ return availableBalls; }
    public boolean getIsCatching(){return isCatching;}
    public void setIsCatching(boolean flag){
        isCatching = flag;
    }

    public void addPokemon(){
        if(!canGeneratePokemon())
            return;

        availablePokemons++;
    }

    public void getMoreBalls(){
        availableBalls = DEFAULT_BALLS_COUNT;
    }

    public CatchResult catchPokemon(){
        if(availableBalls == 0)
            return NoBalls;

        if(availablePokemons == 0)
            return NoPokemons;

        availableBalls--;
        availablePokemons--;

        if(!isCatchSuccessful())
            return CatchFailed;

        caughtPokemons++;

        return Caught;
    }

    private boolean isCatchSuccessful(){
        int chance = _rnd.nextInt(100) + 1;
        return chance > FAILURE_CATCH_CHANCE;
    }
}
