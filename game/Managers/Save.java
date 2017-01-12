package com.shirukentoss.game.Managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by AaronW on 5/06/16.
 */
public class Save {

    public static GameData gd;

    public static void save(){

        Preferences prefs = Gdx.app.getPreferences("My Preferences");
//        try{
//            ObjectOutputStream out = new ObjectOutputStream(
//                new FileOutputStream("highscores.sav")
//            );
//            out.writeObject(gd);
//            out.close();
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            Gdx.app.exit();
//        }

        prefs.putString("name", "Donald Duck");
        prefs.getString("name", "No name stored");



    }

    public static void load(){
        try {
            if(!saveFileExists()){
                init();
                return;
            }
            ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("highscores.sav")

            );
            gd =  (GameData) in.readObject();

        }
        catch (Exception e){
            e.printStackTrace();
            //Gdx.app.exit();
        }
    }

    public static boolean saveFileExists(){
        File f = new File("highscores.sav");
        return f.exists();
    }

    public static void init(){
        gd = new GameData();
        gd.init();
        save();
    }
}
