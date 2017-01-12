package com.shirukentoss.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shirukentoss.game.Screens.PlayScreen;

public class Hud
{
    //Stage variables
    public Stage stage;
    private Viewport viewport;

    //HUD values
    public static Integer worldTimer;
    private static float timeCount;
    public static Integer score;
    private static Integer shurikens;

    //HUD Labels
    Label timeLabel;
    static Label scoreLabel;
    Label timeTitleLabel;
    Label scoreTitleLabel;
    static Label shurikensLabel;

    //HUD Icons
    Image shurikenImage;
    Texture shurikenTexture;
    TextureRegion pauseTextureRegion;
    Texture pauseTexture;

    //HUD Sounds
    Sound tick;

    //HUD Buttons
    ImageButton pauseButton;

    public Hud(SpriteBatch sb){

        //Default HUD values
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        shurikens = 5;

        //Create the viewport and stage
        viewport = new FitViewport(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //Bitmap font
        BitmapFont HUDfont = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
        BitmapFont proximaFont = new BitmapFont(Gdx.files.internal("proxima.fnt"), Gdx.files.internal("proxima.png"), false);

        //The root table which fills the stage
        Table rootTable = new Table();
        rootTable.setFillParent(true); //table is size of stage
        rootTable.top();

        //The table containing the top part of the HUD
        Table topTable = new Table();
        topTable.setBackground(new NinePatchDrawable(getNinePatch(("HUDBackground2.9.png"))));

        //The table fill the empty space in the middle of the HUD (play area)
        Table midTable = new Table();
        //topTable.setBackground(new NinePatchDrawable(getNinePatch(("Japanese_landscape_fix.jpg"))));

        //The table containing the bottom part of the HUD
        Table botTable = new Table();
        botTable.left();
        botTable.setBackground(new NinePatchDrawable(getNinePatch(("HUDBackground2.9.png"))));

        //Empty space for the bottom table
        Table emptySpace = new Table();

        timeTitleLabel = new Label("TIME", new Label.LabelStyle(HUDfont, Color.WHITE));
        timeLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        scoreTitleLabel = new Label("SCORE", new Label.LabelStyle(HUDfont, Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        shurikensLabel = new Label("x " + String.format("%01d", shurikens), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //Shuriken Icon
        shurikenTexture = new Texture(Gdx.files.internal("shuriken-icon.png"));
        shurikenImage = new Image(shurikenTexture);

        //Pause Icon (don't why all this is nessecary)
        pauseTexture = new Texture(Gdx.files.internal("pauseButton.png"));
        pauseTextureRegion = new TextureRegion(pauseTexture);
        TextureRegionDrawable pauseTRD = new TextureRegionDrawable(pauseTextureRegion);

        //Pause Button (doesn't do anything yet)
        pauseButton = new ImageButton(pauseTRD);
        pauseButton.addListener(new ActorGestureListener(){

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(PlayScreen.isPaused){
                    PlayScreen.isPaused = false;
                } else {
                    PlayScreen.isPaused = true;
                }
            }
        });

        //Countdown Tick
        tick = Gdx.audio.newSound(Gdx.files.internal("tick.mp3"));

        //Add all of the elements to the Top Table
        topTable.add(scoreTitleLabel).expandX();
        topTable.add(timeTitleLabel).expandX();
        topTable.row();
        topTable.add(scoreLabel).expandX();
        topTable.add(timeLabel).expandX();

        //Add all of the elements to the Bot Table
        botTable.add(shurikenImage);
        botTable.add(shurikensLabel);
        botTable.add(emptySpace).expandX();
        botTable.add(pauseButton);

        //Add all of the tables to the Root Table
        rootTable.add(topTable).fillX();
        rootTable.row();
        rootTable.add(midTable).fillX().fillY().expandX().expandY();
        rootTable.row();
        rootTable.add(botTable).fillX().expandX();

        //Add the Root Table to the stage
        stage.addActor(rootTable);


    }

    public void update(float dt){ //this method is called every frame inside the playScreen Class
        timeCount += dt;
        if(timeCount >= 1)
        {
            worldTimer--;
            timeLabel.setText(String.format("%03d", worldTimer));

            //Check if there are less then 10 seconds left in the game
            if(worldTimer < 10)
            {
                timeLabel.setColor(Color.RED);
                timeTitleLabel.setColor(Color.RED);
                tick.play(1.0f);
            }

            timeCount = 0;
        }
    }

    public static void addScore(int value){ //add the value to the current score
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }


    //players get a limited amount of shurikens


    public static void subtractShuriken(int value){ //subtract the number of shurikens by the value
        shurikens -= value;
        shurikensLabel.setText("x " + String.format("%01d", shurikens));

        //Set text to red when only 2 shurikens left
        if(shurikens < 3)
        {
            shurikensLabel.setColor(Color.RED);
        }
    }

    public static void addShuriken(int value){ //add the value to the number of shurikens
        shurikens += value;
        shurikensLabel.setText("x " + String.format("%01d", shurikens));
    }

    //Loads texture and creates NinePatch
    private NinePatch getNinePatch(String fname) {

        // Get the image
        final Texture t = new Texture(Gdx.files.internal(fname));
        return new NinePatch(new TextureRegion(t, 1, 1, t.getWidth() - 2, t.getHeight() - 2), 10, 10, 10, 10);

    }

    public static int getShurikens(){
        return shurikens;
    }
    public static void reset(){
        worldTimer = 100;
        timeCount = 0;
        score = 0;
        shurikens = 10;
    }

}
