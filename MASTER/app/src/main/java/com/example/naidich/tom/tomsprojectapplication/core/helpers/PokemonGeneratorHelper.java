package com.example.naidich.tom.tomsprojectapplication.core.helpers;

import com.example.naidich.tom.tomsprojectapplication.R;
import com.example.naidich.tom.tomsprojectapplication.core.models.Pokemon;

import java.util.Random;

public final class PokemonGeneratorHelper {
    private static final Random _rnd = new Random();
    public static final int MAX_POKEMON_TYPES = 12;
    private static int _countGeneratedPokemons = 0;

    public static Pokemon getRandomPokemon(){
        int pokemonType = getRandomPokemonType();

        _countGeneratedPokemons++;

        switch(pokemonType){
            case 1: return new Pokemon(R.drawable.pokemon128_1, R.string.pokemon_snorlax);
            case 2: return new Pokemon(R.drawable.pokemon128_2, R.string.pokemon_bulbasaur);
            case 3: return new Pokemon(R.drawable.pokemon128_3, R.string.pokemon_eevee);
            case 4: return new Pokemon(R.drawable.pokemon128_4, R.string.pokemon_jigglypuff);
            case 5: return new Pokemon(R.drawable.pokemon128_5, R.string.pokemon_charmander);
            case 6: return new Pokemon(R.drawable.pokemon128_6, R.string.pokemon_pikachu);
            case 7: return new Pokemon(R.drawable.pokemon128_7, R.string.pokemon_psyduck);
            case 8: return new Pokemon(R.drawable.pokemon128_8, R.string.pokemon_squirtle);
            case 9: return new Pokemon(R.drawable.pokemon128_9, R.string.pokemon_meowth);
            case 10: return new Pokemon(R.drawable.pokemon128_10, R.string.pokemon_rattata);
            case 11: return new Pokemon(R.drawable.pokemon128_11, R.string.pokemon_abra);
            case 12: return new Pokemon(R.drawable.pokemon128_12, R.string.pokemon_monkey);

            default: return new Pokemon(R.drawable.pokemon128_1, R.string.pokemon_snorlax);
        }
    }

    public static int getGeneratedPokemonsCount(){
        return _countGeneratedPokemons;
    }

    private static int getRandomPokemonType(){
        return _rnd.nextInt(MAX_POKEMON_TYPES) + 1;
    }
}
