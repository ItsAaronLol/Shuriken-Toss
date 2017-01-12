package com.shirukentoss.game.Managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;



/**
 * Created by AaronW on 28/04/16.
 */




public class MusicManager{
   static Music theme;

    static Music gameOver;


   public static void playTheme(){
       theme = Gdx.audio.newMusic(Gdx.files.internal("Epic Japanese Music - Shadow Ninja.mp3"));
       theme.play();
       theme.setLooping(true);
       theme.setVolume(0.9f);
       theme.setPosition(2f);
   }

    public static void playGameOverMusic(){
        gameOver = Gdx.audio.newMusic(Gdx.files.internal("gameover.mp3"));
        gameOver.play();
        gameOver.setLooping(false);
        gameOver.setVolume(.1f);

    }

    public static void stopMainMusic(){
        theme.dispose();
    }

}
