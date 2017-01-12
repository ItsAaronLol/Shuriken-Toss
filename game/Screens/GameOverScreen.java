package com.shirukentoss.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shirukentoss.game.Managers.MusicManager;
import com.shirukentoss.game.ShirukenToss;


/**
 * Created by AaronW on 25/04/16.
 */

//this is the gameover screen. You go to this screen after losing. At the moment, it is when the time runs out
public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Table table;
    private Skin skin;
    //bring in main game object so that we can have access so we can change screens later on
    private ShirukenToss game;
    private int score;
    private float GOscreenWidth;
    private float GOscreenHeight;
    private boolean GOplaying;
    SpriteBatch GObatch;
    Texture GOimg;
    Image GObger;
    Image GObackground;
    TextureAtlas GOatlas;
    Sprite GObg;
    TextureRegion GOwholeScreen;
    TextButton GOplayButton;
    BitmapFont GOShoju;
    Music GOtheme;
    Sound gameOverSound;



    private String rank;
    public Array<Integer> highScores = new Array<Integer>();


    public Label scoreLabel;
    public Label rankLabel;




    public GameOverScreen(ShirukenToss game, int score){
        this.game = game;
        this.score = score;

    }

    @Override
    public void show() {



                Preferences prefs = Gdx.app.getPreferences("My Preferences");
        int currentHighScore = prefs.getInteger("highscore");
        if(score > currentHighScore){
            prefs.putInteger("highscore", score);
            prefs.flush();
        }



        //get the highscores list from the
        skin = new Skin(Gdx.files.internal("uiskin.json")); //set the look and feel
        stage = new Stage(new ScreenViewport()); //create a new stage
        GOscreenHeight = Gdx.graphics.getHeight();
        GOscreenWidth = Gdx.graphics.getWidth();


        //draw background
        GObatch = new SpriteBatch();
        GOimg = new Texture("Japanese_landscape_fix.jpg");
        GOwholeScreen = new TextureRegion(GOimg);
        GObg = new Sprite(GOimg);
        GObg.setSize(GOscreenWidth, GOscreenHeight);
        GObger = new Image(GOimg);
        GObger.setFillParent(true);
        stage.addActor(GObger);


        table = new Table(); //create a table to organize the menu buttons
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.bottom);
        table.setPosition(0, 0);


        //assign ranks
        if(score >= 0){
            rank = "L Plate Ninja";
        }
        if(score > 1000){
            rank = "Rookie Ninja";
        }
        if(score > 3000){
            rank = "Ninja Warrior";
        }
        if(score > 6000){
            rank = "Ninja Warrior with Swag Glasses";
        }
        if(score > 10000){
            rank = "Ninja Master";
        }
        if(score > 15000){
            rank = "Ninja Raid Boss";
        }
        if(score > 21000){
            rank = "Super Sayain Ninja";
        }
        if(score > 28000){
            rank = "Super Sayain Ninja with Dreadlocks";
        }
        if(score > 36000){
            rank = "Super Sayain Ninja with Dreadlocks and Swag Glasses";
        }

        prefs.putString("rank", rank);
        prefs.flush();



        rankLabel = new Label(rank, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        rankLabel.setWidth(200);
        rankLabel.setHeight(50);
        rankLabel.setFontScale(8.0f);

        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel.setWidth(200);
        scoreLabel.setHeight(50);
        scoreLabel.setFontScale(8.0f);
        MusicManager.stopMainMusic();
        MusicManager.playGameOverMusic();

        final TextButton playAgainButton = new TextButton("RESTART", skin, "default"); //create the play again Button

        playAgainButton.setWidth(GOscreenWidth / 2);
        playAgainButton.setHeight(GOscreenHeight / 9);

        playAgainButton.addListener(new ClickListener() { //when the play again button is clicked, go to the playscreen

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
                dispose();
            }
        });

        playAgainButton.getLabel().setFontScale(5.0f); //increase the font of the playbutton

        final TextButton menuButton = new TextButton("MENU", skin, "default"); //create the play again Button

        menuButton.setWidth(GOscreenWidth/2);
        menuButton.setHeight(GOscreenHeight / 9);

        menuButton.addListener(new ClickListener() { //when the play again button is clicked, go to the playscreen

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        menuButton.getLabel().setFontScale(5.0f); //increase the font of the playbutton
        //table.add(rankLabel).padBottom(50);
        table.add(rankLabel).padTop(100);
        table.row();
        table.add(scoreLabel).padBottom(250);
        table.row();
        table.add(playAgainButton).padBottom(400);
        table.row();
        table.add(menuButton).padBottom(100);

//        table.add(quitButton).padBottom(300);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
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
        stage.dispose();
    }

    public void saveScore(){

    }
}
