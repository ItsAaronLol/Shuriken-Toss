package com.shirukentoss.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shirukentoss.game.ShirukenToss;

/**
 * Created by AaronW on 5/06/16.
 */
public class HighScoresScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Table table;
    private Skin skin;
    private float HSscreenWidth;
    private float HSscreenHeight;
    private boolean playing;
    SpriteBatch HSbatch;
    Texture HSimg;
    Image HSbger;
    Image HSbackground;
    TextureAtlas HSatlas;
    Sprite HSbg;
    TextureRegion HSwholeScreen;
    //bring in main game object so that we can have access so we can change screens later on
    private ShirukenToss game;

   // private long[] highScores;
    private String[] names;

    private int highscore;
    private String rank;
    Label scoreLabel;
    Label rankLabel;

    public HighScoresScreen(ShirukenToss game){
        this.game = game;
    }

    @Override
    public void show() {
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        highscore = prefs.getInteger("highscore");
        rank = prefs.getString("rank");

        //get the highscores list from the
        skin = new Skin(Gdx.files.internal("uiskin.json")); //set the look and feel
        stage = new Stage(new ScreenViewport()); //create a new stage
        HSscreenHeight = Gdx.graphics.getHeight(); //get screen height and width for graphics resize
        HSscreenWidth = Gdx.graphics.getWidth();


        //draw background
        HSbatch = new SpriteBatch();
        HSimg = new Texture("BW_BG.png");
        HSwholeScreen = new TextureRegion(HSimg);
        HSbg = new Sprite(HSimg);
        HSbg.setSize(HSscreenWidth, HSscreenHeight);
        HSbger = new Image(HSimg);
        HSbger.setFillParent(true);
        stage.addActor(HSbger);

        table = new Table(); //create a table to organize the menu buttons
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.bottom);
        table.setPosition(0, 0);


        rankLabel = new Label((rank), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        rankLabel.setWidth(200);
        rankLabel.setHeight(50);
        rankLabel.setFontScale(5.0f);

        scoreLabel = new Label(String.format("%06d", highscore), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel.setWidth(200);
        scoreLabel.setHeight(50);
        scoreLabel.setFontScale(5.0f);


        final TextButton playAgainButton = new TextButton("MENU", skin, "default"); //create the play again Button

        playAgainButton.setWidth(200);
        playAgainButton.setHeight(50);

        playAgainButton.addListener(new ClickListener() { //when the play again button is clicked, go to the playscreen

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        playAgainButton.getLabel().setFontScale(5.0f); //increase the font of the playbutton

        table.add(rankLabel).padBottom(50);
        table.row();
        table.add(scoreLabel).padBottom(500);
        table.row();
        table.add(playAgainButton).padBottom(300);

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

    }
}
