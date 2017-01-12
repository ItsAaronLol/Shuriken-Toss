package com.shirukentoss.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shirukentoss.game.BoundaryHorizontal;
import com.shirukentoss.game.BoundaryVertical;
import com.shirukentoss.game.Content;
import com.shirukentoss.game.Managers.MusicManager;
import com.shirukentoss.game.MyContactListener;
import com.shirukentoss.game.Scenes.Hud;
import com.shirukentoss.game.Shiruken;
import com.shirukentoss.game.ShirukenToss;
import com.shirukentoss.game.Target;
import com.shirukentoss.game.utils.Constants;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by AaronW on 25/04/16.
 */



//this is the playScreen. All code here is run when the user is playing the game
public class PlayScreen implements Screen{


    //animation
    private TextureAtlas atlas;

    //target variables

    public float targetSpawnInterval = 2;
    public float minTargetSpawnInterval = 1;
    public float targetsHit;


    private float elapsedTime = 0;

    private static ShirukenToss game;
    private static Stage ui_stage;
    private Skin skin;

    private OrthographicCamera camera;
    private Viewport gamePort;
    private Viewport gamePort2;

    private Box2DDebugRenderer b2dr;
    private static World world; //a box2d world
    private MyContactListener cl;
    //private Shiruken shiruken;
//    private Target target;
    private float throwSpeed = 10f;
    public Vector3 touchPos;
    private Body platform;

    SpriteBatch batch;
    SpriteBatch hudBatch;

    public Vector2 initialPos  = new Vector2(0,0); //your initial position when you first create the shuriken

    //public Sprite sprite;


    public Vector2 flingVector = new Vector2(0,0);
    public float flingLineYPosition = -5;

    public static Texture shurikenSprite;
    public static Texture targetSprite;






    public static ArrayList<Shiruken> tmpShirukens = new ArrayList<Shiruken>();

    public static ArrayList<Target> targetObjects = new ArrayList<Target>();

    private static int currentShirukenIndex = -1;
    private static int currentTargetIndex = -1;


    private static Shiruken currentShiruken;

    public static Content res;

    static boolean isHolding = false;
    static boolean isPlaying = false; //this is for checking to see if the player has started throwing shirukens

    //public Vector3 touchPos;

    Hud hud;


    //last target Y position



    //waves

    public static boolean hasStarted = false; //checks to see if the game has started

    public static int waveStartDelay = 1;
    public static int waveNumber = 1;
    public static boolean waveComplete = false;
    public static int targetsRemaining = 1;
    public static int targetNumber = 1;
    public static int maxTargetNumber = 2;
    public static float targetMinimumSpeed = 200/Constants.PPM; //set the minimum speed of a spawned target
    public static float targetMaximumSpeed;

    public static int lives = 1;

    public static Array<Body> bodies;




    //sounds
    //private Sound[] tossSounds;
    public static List<Sound> tossSounds = new ArrayList<Sound>();
    public static List<Sound> impactSounds = new ArrayList<Sound>();

    //target animation
    public static Texture targetSpriteSheet;
    public static Texture targetNegSpriteSheet;
    public static Animation targetDyingAnimation;
    public static Animation targetDyingNeg;
    public static Animation targetMoving;
    public Array<TextureRegion> frames;

    //shuriken animation


    //score
    public static int killStreak = 0;



    public static boolean isPaused = false;

    public PlayScreen(ShirukenToss game){


        MusicManager.stopMainMusic();
        MusicManager.playTheme();

        this.game = game;
        shurikenSprite = new Texture(Gdx.files.internal("shuriken.png"));
        targetSpriteSheet = new Texture("target-sprite-neutral.png");


        frames = new Array<TextureRegion>();
        for(int i = 0; i < 12; i++)
            frames.add(new TextureRegion(targetSpriteSheet, i * 163, 0, 163, 160));
        targetDyingAnimation = new Animation(1/30f, frames);
        frames.clear();

    }


    @Override
    public void show() {
        tossSounds.add(Gdx.audio.newSound(Gdx.files.internal("shuriken-toss.mp3")));
        tossSounds.add(Gdx.audio.newSound(Gdx.files.internal("shuriken-toss2.mp3")));
        impactSounds.add(Gdx.audio.newSound(Gdx.files.internal("impact1.mp3")));
        impactSounds.add(Gdx.audio.newSound(Gdx.files.internal("impact2.mp3")));
        hasStarted = true;
        Gdx.app.log("start game", "start game");

        res = new Content();
        res.loadTexture("shuriken.png", "shiruken");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(); //for controlling the viewport can pan, zoom flip.
        gamePort = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera); //shows more of the game world


        Gdx.app.log("width:", Float.toString(Gdx.graphics.getWidth()));


        camera.setToOrtho(false, w, h); //divided by 2 to get a zoomed in view
       // camera.zoom = 2;
        world = new World(new Vector2(0, 0), false);
        cl = new MyContactListener();
       world.setContactListener(cl);
        //for the world to show us what it looks like, you must use a box2d renderer
        b2dr = new Box2DDebugRenderer();

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        ui_stage = new Stage(new ScreenViewport());
        final TextButton gestureButton = new TextButton("", skin, "default");


        gestureButton.setWidth(Gdx.graphics.getWidth());
        gestureButton.setHeight(Gdx.graphics.getHeight());
        gestureButton.setColor(gestureButton.getColor().r, gestureButton.getColor().g, gestureButton.getColor().b, gestureButton.getColor().a - .9f);
        touchPos = new Vector3(0,0,0);

        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        hud = new Hud(hudBatch);
        ui_stage.addActor(gestureButton);
        ui_stage.draw();


        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.stage);
        multiplexer.addProcessor(ui_stage);
        Gdx.input.setInputProcessor(multiplexer);





        gestureButton.addListener(new ActorGestureListener() {

            @Override
            public boolean handle(Event e) {
                return super.handle(e);
            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if(!isPaused){
                    //touchPos = new Vector3(x,y,0);
                    touchPos.x = x;
                    touchPos.y = y;
                    touchPos.z = 0;
                    camera.unproject(touchPos);


                    float cameraWorldPosY = camera.position.y / Constants.PPM;

                    float finalTouchY;
                    if (touchPos.y / Constants.PPM < cameraWorldPosY) {
                        //if the touch is higher than the center of the screen
                        finalTouchY = cameraWorldPosY + Math.abs(cameraWorldPosY - touchPos.y / Constants.PPM);
                    } else {
                        finalTouchY = cameraWorldPosY - Math.abs(cameraWorldPosY - touchPos.y / Constants.PPM);
                    }

                    initialPos.x = touchPos.x / Constants.PPM;
                    initialPos.y = finalTouchY;

                    //on touching down on the screen, add a new shiruken onto the screen
                    if (!isHolding && finalTouchY <= flingLineYPosition && Hud.getShurikens() > 0) {//if the player is not holding onto a shiruken.
                        currentShirukenIndex += 1;
                        isHolding = true; //player is now holding onto a shiruken
                        Shiruken shiruken = new Shiruken(world, touchPos.x / Constants.PPM, touchPos.y / Constants.PPM, currentShirukenIndex);
                        currentShiruken = shiruken;
                        tmpShirukens.add(shiruken);

                        isPlaying = true;

                    }

                    if (isPlaying && finalTouchY <= flingLineYPosition && Hud.getShurikens() > 0) {
                        currentShiruken.pBody.setTransform(touchPos.x / Constants.PPM, -touchPos.y / Constants.PPM, 0);
                    }
                }



            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
            }

            @Override
            public boolean longPress(Actor actor, float x, float y) {
                return super.longPress(actor, x, y);
            }

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {


                if(!isPaused){
                    //Vector2 flingDirection = new Vector2(velocityX, velocityY);
                    flingVector.set(velocityX / Constants.PPM, velocityY / Constants.PPM); //set the fling vector's velocity
                    if (velocityY > velocityX && velocityY / Constants.PPM > 10 && isHolding) { //make sure throwing up
                        //currentShiruken.pBody.setTransform(currentShiruken.pBody.getPosition().x, currentShiruken.pBody.getPosition().y, flingVector.angle());

                        playTossSound();
                        //  toss.setPitch(id, 1.5f);
                        //  toss.setPitch(id, (float)Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));


                        currentShiruken.pBody.setLinearVelocity(velocityX / Constants.PPM, velocityY / Constants.PPM);
                        //set the rotation based on the direction of the thrown shuriken
                        if (velocityX > 0) {
                            currentShiruken.pBody.setAngularVelocity(-1 * Math.abs(velocityX / Constants.PPM) / 2);
                        } else if (velocityX <= 0) {
                            currentShiruken.pBody.setAngularVelocity(1 * Math.abs(velocityX / Constants.PPM) / 2);
                        }


                        isHolding = false;
                        Hud.subtractShuriken(1);
                        currentShiruken = null;

                    }
                }



            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {

                if(!isPaused){

                    float panRadius = 5000; //how much you can pan before the ball goes back to original position
                    //sprite.setPosition(touchPos.x - sprite.getWidth() / 2, touchPos.y - sprite.getHeight() / 2);
                    if (isHolding) {


                        touchPos.x = x;
                        touchPos.y = y;
                        touchPos.z = 0;

                        camera.unproject(touchPos);


                        currentShiruken.pBody.setTransform(touchPos.x / Constants.PPM, -touchPos.y / Constants.PPM, 0);


                        currentShiruken.currentVelocity.x = (currentShiruken.pBody.getPosition().x - currentShiruken.lastPosition.x) / Gdx.graphics.getDeltaTime();
                        currentShiruken.currentVelocity.y = (currentShiruken.pBody.getPosition().y - currentShiruken.lastPosition.y) / Gdx.graphics.getDeltaTime();

                        currentShiruken.lastPosition.x = currentShiruken.pBody.getPosition().x;
                        currentShiruken.lastPosition.y = currentShiruken.pBody.getPosition().y;


                        if (currentShiruken.pBody.getPosition().y >= flingLineYPosition) { //automatically throw shuriken if it goes past the y fling coordinate
                            playTossSound();
                            currentShiruken.pBody.setLinearVelocity(currentShiruken.currentVelocity.x * camera.zoom, currentShiruken.currentVelocity.y * camera.zoom);
                            isHolding = false;
                            Hud.subtractShuriken(1);
                        }
                        if ((Math.abs(currentShiruken.pBody.getPosition().y - initialPos.y) > panRadius / Constants.PPM)) {
                            //what happens if you drag the body outside the drag radius

                            currentShiruken.pBody.setLinearVelocity(currentShiruken.currentVelocity.x * camera.zoom, currentShiruken.currentVelocity.y * camera.zoom);
                            isHolding = false;
                            Hud.subtractShuriken(1);
                        }

                    }
                }



            }

            @Override
            public void zoom(InputEvent event, float initialDistance, float distance) {
                super.zoom(event, initialDistance, distance);
            }

            @Override
            public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                super.pinch(event, initialPointer1, initialPointer2, pointer1, pointer2);
            }

            @Override
            public GestureDetector getGestureDetector() {
                return super.getGestureDetector();
            }

            @Override
            public Actor getTouchDownTarget() {
                return super.getTouchDownTarget();
            }
        });

        BoundaryHorizontal boundary = new BoundaryHorizontal(world, 0, 0);
        boundary.pBody.setTransform(0, Gdx.graphics.getHeight() / 1.5f / Constants.PPM, 0);

        BoundaryHorizontal boundary2 = new BoundaryHorizontal(world, 0, 0);
        boundary2.pBody.setTransform(0, -Gdx.graphics.getHeight()/1.5f / Constants.PPM, 0);

        BoundaryVertical boundary3 = new BoundaryVertical(world, 0, 0);
        boundary3.pBody.setTransform(Gdx.graphics.getHeight()/2.4f / Constants.PPM, 0, 0);

        BoundaryVertical boundary4 = new BoundaryVertical(world, 0, 0);
        boundary4.pBody.setTransform(-Gdx.graphics.getHeight() / 2.4f / Constants.PPM, 0, 0);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                startWave();
            }
        }, waveStartDelay);




    }

    @Override
    public void render(float delta) {
        if(!isPaused){
            update(Gdx.graphics.getDeltaTime()); //the amount of time it took between

            //a frame refresh
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            ui_stage.draw();
            batch.setProjectionMatrix(camera.combined.scl(Constants.PPM));



            batch.begin();

            for(Shiruken shiruken: tmpShirukens){ //go through the sprite array and draw all the sprites
                shiruken.draw(batch);
            }

            for(Target target: targetObjects){ //go through the sprite array and draw all the sprites
                target.draw(batch);
            }

            batch.end();

            hudBatch.setProjectionMatrix(hud.stage.getCamera().combined);
            hud.stage.draw();
            if(Hud.worldTimer<=0){
                game.setScreen(new GameOverScreen(game, Hud.score));
                dispose();
            }
        }

    }

    @Override
    public void resize(int width, int height) { //called once on launch, then again on every screen size
        gamePort.update(width, height); //Configures this viewport's screen bounds using the specified screen size and calls apply(boolean).
        //apply method: Applies the viewport to the camera and sets the glViewport.
    }

    @Override
    public void pause() { //called when the application focus is lost, also when it is closed

    }

    @Override
    public void resume() { //called when the program recieves focus

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {//called when the application is being destroyed
        ui_stage.dispose();
    }




    public void update(float delta){

        if(!isPaused){
            world.step(1 / 60f, 6, 2);
            //remove shiruken
            for(int i = 0; i<targetObjects.size(); i++){
                targetObjects.get(i).update(delta);
            }

            for(int i = 0; i<tmpShirukens.size(); i++){
                tmpShirukens.get(i).update(delta);
            }


            inputUpdate(delta);
            cameraUpdate(delta);

            hud.update(Gdx.graphics.getDeltaTime());

        }



    }

    public void inputUpdate(float delta) {


        bodies = cl.getBodiesToRemove();
        for(int i = 0; i<bodies.size; i++ ){
            Body b = bodies.get(i);
            //world.destroyBody(b);
            b.setActive(false);
            b = null;
        }
        bodies.clear();



    }

    public void cameraUpdate(float delta){
        camera.position.x = 0;
        camera.position.y = 0;
        camera.update();
    }


    public static void startWave(){

        if(hasStarted){
            //determine how many targets
            targetNumber = MathUtils.random(1, maxTargetNumber);
            targetsRemaining = targetNumber;
            //spawn every target for this wave
            for (int i = 0; i < targetNumber; i++) {

                float randomDelay = MathUtils.random(1); // seconds
                //spawn the new targets at a random delay between 0 and 3
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        Gdx.app.log("run schedule", "run schedule");
                        targetMaximumSpeed = targetMinimumSpeed + 300 / Constants.PPM;
                        float randomY = MathUtils.random(-200 / Constants.PPM, 700 / Constants.PPM);
                        float randomSpeed = MathUtils.random(targetMinimumSpeed, targetMaximumSpeed);
                        currentTargetIndex += 1;
                        Target target = new Target(world, 0, 0, currentTargetIndex);
                        targetObjects.add(target);


                        double d = Math.random();
                        if (d < 0.5) {
                            target.pBody.setTransform(-650 / Constants.PPM, randomY, 0);
                            target.pBody.setLinearVelocity(randomSpeed, 0);
                        } else {
                            target.pBody.setTransform(650 / Constants.PPM, randomY, 0);
                            target.pBody.setLinearVelocity(-randomSpeed, 0);
                        }


                    }
                }, randomDelay);
            }
        }

    }

    public static void endWave(){
        Gdx.app.log("maxTargetNumber: ", Float.toString(maxTargetNumber));
        Gdx.app.log("wave Number: ", Float.toString(waveNumber));
        //every 5 waves increase the number of max targets by 1;
        if(waveNumber % 5 == 0){
            //increase the number of targets
            maxTargetNumber += 1;


        }
        targetMinimumSpeed += 10/Constants.PPM;

        //start the new wave after 1 second
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                startWave();
            }
        }, waveStartDelay);


    }

    public static void endGame(){
        hasStarted = false;


        isPlaying = false;
        isHolding = false;

        waveStartDelay = 1;
        waveNumber = 1;
        waveComplete = false;
        targetsRemaining = 1;
        targetNumber = 1;
        maxTargetNumber = 2;
        targetMinimumSpeed = 200/Constants.PPM; //set the minimum speed of a spawned target
        lives = 1;
        killStreak = 0;

        for(int i = 0; i < tmpShirukens.size()-1; i++){
            bodies.add(tmpShirukens.get(i).pBody);

        }

        for(int i = 0; i < targetObjects.size()-1; i++){
            bodies.add(targetObjects.get(i).pBody);
        }




        tmpShirukens.clear();
        targetObjects.clear();
        currentTargetIndex = -1;
        currentShirukenIndex = -1;


        game.setScreen(new GameOverScreen(game, Hud.score));
        ui_stage.dispose();
        Hud.reset();

    }

    public void restartGame(){

    }

   public void playTossSound(){

       int soundIndex = MathUtils.random(0, tossSounds.size() - 1);
       long id = tossSounds.get(soundIndex).play();
       tossSounds.get(soundIndex).setVolume(id, 0.1f);

   }

    public static void playImpactSound(){

        int soundIndex = MathUtils.random(0, impactSounds.size() - 1);
        long id = impactSounds.get(soundIndex).play();
        impactSounds.get(soundIndex).setVolume(id, 0.1f);

    }




}
