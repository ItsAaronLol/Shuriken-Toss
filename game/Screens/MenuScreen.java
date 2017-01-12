package com.shirukentoss.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shirukentoss.game.Managers.MusicManager;
import com.shirukentoss.game.ShirukenToss;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;



/**
 * Created by AaronW on 28/04/16.
 */

//this is the menu screen. The game starts on this screen.
public class MenuScreen implements Screen{
    private Viewport viewport;
    private Stage stage;
    private Table table;
    private Skin skin;
    private float screenWidth;
    private float screenHeight;
    private boolean playing;
    SpriteBatch batch;
    Texture img;
    Image bger;
    Image background;
    TextureAtlas atlas;
    Sprite bg;
    TextureRegion wholeScreen;
    TextButton playButton;
    BitmapFont Shoju;
    Music theme;




    //bring in main game object so that we can have access so we can change screens later on
    private ShirukenToss game;

    public MenuScreen(ShirukenToss game){

        this.game = game;
    }

    @Override
    public void show() {



        skin = new Skin(Gdx.files.internal("uiskin.json")); //set the look and feel
        stage = new Stage(new ScreenViewport()); //create a new stage
        screenHeight = Gdx.graphics.getHeight(); //get screen height and width for graphics resize
        screenWidth = Gdx.graphics.getWidth();

        //draw background
        batch = new SpriteBatch();
        img = new Texture("Japanese_landscape_fix.jpg");
        wholeScreen = new TextureRegion(img);
        bg = new Sprite(img);
        bg.setSize(screenWidth, screenHeight);
        bger = new Image(img);
        stage.addActor(bger);
        bger.setFillParent(true);

        table = new Table(); //create a table to organize the menu buttons
        table.setWidth(stage.getWidth());
        table.align(Align.center);
        table.align(Align.bottom);
        table.setPosition(0, 0);

        //create play button and add it to the screen
        //create new font for buttons
        Shoju = new BitmapFont(Gdx.files.internal("Shoju.fnt"), false);
        Shoju.setColor(Color.WHITE);
        skin.add("default", Shoju);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Shoju;
        textButtonStyle.fontColor = Color.WHITE;
        playButton = new TextButton("PLAY", skin, "default"); //create the playButton





        playButton.getLabel().setFontScale(5.0f); //set the font of the playbutton

        playButton.setWidth(screenWidth / 2);
        playButton.setHeight(screenHeight / 10);

        playButton.addListener(new ClickListener() { //when the playbutton is clicked, go to the playscreen

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
                dispose();
            }
        });




        final TextButton highScoresButton = new TextButton("High Scores", skin, "default");
        highScoresButton.setWidth(screenWidth / 2);
        highScoresButton.setHeight(screenHeight / 9);

        highScoresButton.addListener(new ClickListener() { //when the quit button is clicked, quit the game

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HighScoresScreen(game));
                dispose();
                //insert quit game code here
            }
        });

        highScoresButton.getLabel().setFontScale(3.0f);


        table.add(playButton).padBottom(100);
        table.row();
        //table.add(highScoresButton);
        table.add(highScoresButton).padBottom(1000);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
//        if(Gdx.input.justTouched()){
//            game.setScreen(new PlayScreen(game));
//            dispose();
//        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();


    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
       // theme.dispose();
        stage.dispose();
        //Shoju.dispose();
        //batch.dispose();
    }
}
