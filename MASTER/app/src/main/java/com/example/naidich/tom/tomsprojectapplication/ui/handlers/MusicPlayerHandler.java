package com.example.naidich.tom.tomsprojectapplication.ui.handlers;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.concurrent.Callable;

public class MusicPlayerHandler {
    private static final float
            BACKGROUND_MUSIC_VOLUME_LEFT = 0.2f,
            BACKGROUND_MUSIC_VOLUME_RIGHT = 0.2f;

    private Context _context;
    private MediaPlayer
            _mpBackgroundMusicPlayer,
            _mpEventMusicPlayer;

    public MusicPlayerHandler(final Context context){
        _context = context;
    }

    @Override
    public void finalize(){
        stopAllMusic();
    }

    public void playEventSound(final int musicResourceId, final Callable<Integer> callback){
        stopEventMusic();

        _mpEventMusicPlayer = MediaPlayer.create(_context, musicResourceId);

        _mpEventMusicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                try{
                    if(_mpEventMusicPlayer != null)
                    {
                        if(_mpEventMusicPlayer.isPlaying())
                            _mpEventMusicPlayer.stop();

                        _mpEventMusicPlayer.reset();
                        _mpEventMusicPlayer.release();
                        _mpEventMusicPlayer = null;
                    }
                }
                catch(Exception ex){

                }

                if(callback != null) {
                    try {
                        callback.call();
                    } catch (Exception ex) {

                    }
                }

            }
        });

        _mpEventMusicPlayer.start();
    }

    public void playBackgroundMusic(final int musicResourceId){
        stopBackgroundMusic();

        _mpBackgroundMusicPlayer = MediaPlayer.create(_context, musicResourceId);
        _mpBackgroundMusicPlayer.setLooping(true);
        _mpBackgroundMusicPlayer.setVolume(BACKGROUND_MUSIC_VOLUME_LEFT, BACKGROUND_MUSIC_VOLUME_RIGHT);
        _mpBackgroundMusicPlayer.start();
    }

    public void stopAllMusic(){
        stopBackgroundMusic();
        stopEventMusic();
    }

    public void stopBackgroundMusic(){
        if(_mpBackgroundMusicPlayer != null && _mpBackgroundMusicPlayer.isPlaying())
            _mpBackgroundMusicPlayer.stop();
    }

    public void stopEventMusic(){
        if(_mpEventMusicPlayer != null && _mpEventMusicPlayer.isPlaying())
            _mpEventMusicPlayer.stop();
    }
}
