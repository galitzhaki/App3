package com.example.naidich.tom.tomsprojectapplication.ui.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.naidich.tom.tomsprojectapplication.R;
import com.example.naidich.tom.tomsprojectapplication.core.enums.Game;
import com.example.naidich.tom.tomsprojectapplication.core.helpers.PokemonGeneratorHelper;
import com.example.naidich.tom.tomsprojectapplication.core.models.Pokemon;
import com.example.naidich.tom.tomsprojectapplication.core.providers.ScoreModelProvider;
import com.example.naidich.tom.tomsprojectapplication.core.services.CatchEmAllService;
import com.example.naidich.tom.tomsprojectapplication.ui.handlers.MusicPlayerHandler;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;

public class CatchEmAllActivity extends AppCompatActivity {
    private ArrayList<ImageView>
            _availablePokeballViews,
            _pokemonViews,
            _availablePokemonViews
    ;
    private static final Random _rnd = new Random();
    private final MusicPlayerHandler _musicHandler = new MusicPlayerHandler(CatchEmAllActivity.this);
    private final CatchEmAllService _service = new CatchEmAllService();

    private SharedPreferences _sharedPreferences;
    private ScoreModelProvider _scoreProvider;

    private int POKEMON_GENERATION_INTERVAL = 4000; // default is 4 seconds
    private final Handler _mPokemonGenerationHandler = new Handler();
    private final Runnable mPokemonGenerator = new Runnable() {
        @Override
        public void run() {
            try {
                generateRandomPokemon();
            } finally {
                if(_service.canGeneratePokemon())
                    _mPokemonGenerationHandler.postDelayed(mPokemonGenerator, POKEMON_GENERATION_INTERVAL); // Generate next Pokemon for player.
                else
                    handleGameOverEvent(); // Cannot generate any more Pokemons, player has lost the game.
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch_em_all);

        _sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        _scoreProvider = new ScoreModelProvider(_sharedPreferences);

        initAvailablePokeballViews();
        initPokemonViews();

        updateCaughtPokemonsTextView();

        handleMainButtonEvents();

        startPokemonGenerationTask();

        _musicHandler.playBackgroundMusic(R.raw.catch_em_all_background);
    }

    private void startPokemonGenerationTask() {
        mPokemonGenerator.run();
    }

    private void stopPokemonGenerationTask() {
        _mPokemonGenerationHandler.removeCallbacks(mPokemonGenerator);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        _musicHandler.stopAllMusic();
        stopPokemonGenerationTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        _musicHandler.stopAllMusic();
        stopPokemonGenerationTask();
    }

    private void catchPokemon(final int slot) {
        if(_service.getIsCatching())
            return;

        final ImageView pokemonView = _pokemonViews.get(slot);
        final Pokemon pokemon = (Pokemon)pokemonView.getTag();

        if(pokemon == null)
            return; // UI hasn't fully updated yet.

        final String pokemonName = getResources().getString(pokemon.getNameResource());

        final CatchEmAllService.CatchResult result = _service.catchPokemon();

        if(result.equals(CatchEmAllService.CatchResult.NoBalls)) {
            displayToast(getResources().getString(R.string.no_pokeballs), Toast.LENGTH_LONG);
            _musicHandler.playEventSound(R.raw.error, null);
            return;
        }

        if(result.equals(CatchEmAllService.CatchResult.AllPokemonsAreCaught)) {
            displayToast(getResources().getString(R.string.no_pokemons_left), Toast.LENGTH_LONG);
            _musicHandler.playEventSound(R.raw.error, null);
            return;
        }

        _service.setIsCatching(true);

        updateAvailablePokeballs();

        pokemonView.setImageResource(R.drawable.pokeball_open128);

        _musicHandler.playEventSound(R.raw.pokemon_catching, new Callable<Integer>() {
            public Integer call() {
                // Method will be executed only after the sound is finished playing (timer-like behavior).

                if(result.equals(CatchEmAllService.CatchResult.CatchFailed)) {
                    _musicHandler.playEventSound(R.raw.pokemon_catch_failed, null);
                    displayToast(getResources().getString(R.string.catch_failed), Toast.LENGTH_SHORT);
                }
                else if(result.equals(CatchEmAllService.CatchResult.Caught)) {
                    _musicHandler.playEventSound(R.raw.pokemon_caught, null);
                    updateCaughtPokemonsTextView();
                    displayToast(getResources().getString(R.string.pokemon_caught).replace("{POKEMON}", pokemonName), Toast.LENGTH_SHORT);
                }
                clearPokemonView(pokemonView);
                _service.setIsCatching(false);

                makeNextPokemonGenerationFaster();

                return 1;
            }
        });
    }

    private void updateAvailablePokeballs(){
        // Updates the UI part of the available Pokeballs.
        for (int i = 0; i < _availablePokeballViews.size(); i++)
            _availablePokeballViews.get(i).setVisibility(View.INVISIBLE);

        int countAvailableBalls = _service.getAvailableBalls();

        while(countAvailableBalls > 0)
            _availablePokeballViews.get(CatchEmAllService.DEFAULT_BALLS_COUNT - countAvailableBalls--).setVisibility(View.VISIBLE);
    }

    private void updateCaughtPokemonsTextView(){
        // Updates the UI part of the caught Pokemons.
        String resultMessage = getResources().getString(R.string.count_caught_pokemons).replace("{COUNT}", _service.getCaughtPokemons() + "");
        TextView resultTextView = findViewById(R.id.textViewCaughtPokemons);
        resultTextView.setText(resultMessage);
    }

    private void displayToast(String toastMessage, int length){
        // Wrapper for a Toast displayer.
        Toast toast = Toast.makeText(getApplicationContext(), toastMessage, length);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    private void initAvailablePokeballViews(){
        // Inits the AvailablePokemonViews.
        _availablePokeballViews = new ArrayList() {
            {
                add(findViewById(R.id.imageViewPokeball1));
                add(findViewById(R.id.imageViewPokeball2));
                add(findViewById(R.id.imageViewPokeball3));
                add(findViewById(R.id.imageViewPokeball4));
                add(findViewById(R.id.imageViewPokeball5));
            }
        };
    }

    private void initPokemonViews(){
        // Inits the PokemonViews.
        _pokemonViews = new ArrayList() {
            {
                add(findViewById(R.id.imageViewPokemon1));
                add(findViewById(R.id.imageViewPokemon2));
                add(findViewById(R.id.imageViewPokemon3));
                add(findViewById(R.id.imageViewPokemon4));
                add(findViewById(R.id.imageViewPokemon5));
                add(findViewById(R.id.imageViewPokemon6));
                add(findViewById(R.id.imageViewPokemon7));
                add(findViewById(R.id.imageViewPokemon8));
                add(findViewById(R.id.imageViewPokemon9));
                add(findViewById(R.id.imageViewPokemon10));
                add(findViewById(R.id.imageViewPokemon11));
                add(findViewById(R.id.imageViewPokemon12));
            }
        };

        // Hide all views until a pokemon 'shows up'.
        for (int i = 0; i < _pokemonViews.size(); i++) {
            ImageView view = _pokemonViews.get(i);
            view.setVisibility(View.INVISIBLE);
            view.setTag(null);
        }

        _availablePokemonViews = new ArrayList();
        for(ImageView view : _pokemonViews)
            _availablePokemonViews.add(view);
    }

    private void generateRandomPokemon(){
        // Generates a random Pokemon to appear, if possible.

        if(!_service.canGeneratePokemon())
            return;

        ImageView availablePokemonView = findAvailablePokemonView();

        if(availablePokemonView == null)
            return;

        // Remove view from AvailablePokemon views.
        _availablePokemonViews.remove(availablePokemonView);

        // Set Pokemon properties in the view.
        Pokemon pokemon = PokemonGeneratorHelper.getRandomPokemon();
        availablePokemonView.setTag(pokemon);
        availablePokemonView.setImageResource(pokemon.getImageResource());
        availablePokemonView.setVisibility(View.VISIBLE);

        _service.addPokemon();

        /*  Toast has been commented due to massive spam.
            We want the player to be able to see the relevant messages only. */

        /*String pokemonName = getResources().getString(pokemon.getNameResource());
        String pokemonAppearedMessage = getResources().getString(R.string.pokemon_appeared).replace("{POKEMON}", pokemonName);
        displayToast(pokemonAppearedMessage, Toast.LENGTH_SHORT);*/
    }


    private ImageView findAvailablePokemonView(){
        // Finds the next available view for the Pokemon to be generated at.
        do{
            if(_availablePokemonViews.size() == 0)
                return null;

            int random = _rnd.nextInt(_availablePokemonViews.size());
            ImageView pokemonView = _availablePokemonViews.get(random);
            return pokemonView;

        } while(true);
    }

    private void clearPokemonView(ImageView view){
        // Clear view and remove from AvailablePokemonViews list.
        view.setTag(null);
        view.setVisibility(View.INVISIBLE);
        _availablePokemonViews.add(view);
    }

    private void handleGameOverEvent(){
        stopPokemonGenerationTask();
        _musicHandler.playEventSound(R.raw.pokemon_you_lose, null);
        final String youLoseMessage = getResources().getString(R.string.pokemon_you_lose);
        final String dialogTitle = getResources().getString(R.string.catch_em_all_title);

        final String scoreMessage = getResources().getString(R.string.meme_score_message).replace("{COUNT}", _service.getCaughtPokemons() + "");
        _scoreProvider.addScore(Game.GameType.CatchEmAll, scoreMessage);

        // Display alert message for player.
        new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setMessage(youLoseMessage)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        onBackPressed();
                    }
                }).show();
    }

    private void handleMainButtonEvents(){
        // Handle 'Back' button
        Button mBackButton = findViewById(R.id.button_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Handle 'Get Balls' button
        Button mGetBallsButton = findViewById(R.id.button_more_balls);
        mGetBallsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_service.getIsCatching())
                    return;

                _service.getMoreBalls();
                updateAvailablePokeballs();
                _musicHandler.playEventSound(R.raw.get_pokeballs, null);
                displayToast(getResources().getString(R.string.received_pokeballs), Toast.LENGTH_SHORT);
            }
        });

        // Handle Pokemon Catch Click Events
        for(int i = 0; i < _pokemonViews.size(); i++){
            final int pokemon_index = i;

            ImageView mImageViewPokemon = _pokemonViews.get(pokemon_index);
            mImageViewPokemon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Threads are not required here, only one Pokemon can be caught at a time.
                    catchPokemon(pokemon_index);
                }
            });
        }
    }

    private void makeNextPokemonGenerationFaster(){
        // Make generation faster every 5 catches
        if(POKEMON_GENERATION_INTERVAL > 1000 && PokemonGeneratorHelper.getGeneratedPokemonsCount() % 5 == 0)
            POKEMON_GENERATION_INTERVAL -= 500;
    }
}
