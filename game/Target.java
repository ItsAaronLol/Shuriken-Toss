package com.shirukentoss.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.shirukentoss.game.Scenes.Hud;
import com.shirukentoss.game.Screens.PlayScreen;
import com.shirukentoss.game.utils.Constants;

/**
 * Created by AaronW on 18/04/16.
 */
public class Target extends Sprite{

    public World world;
    public Body pBody;
    public float hitPoints;
    private int type;

    private int numPoints;
    private float[] dists;

    private boolean remove;

    private float x;
    private float y;
    private float speed;

    public int index;

    private TextureRegion targetMoving;

    private float stateTimer;

    Texture img;

    public enum State {MOVING, DYING};
    public State currentState;
    public State previousState;
    public boolean isDead;

    public float elapsedDeathTime;


    public Array<TextureRegion> frames;

    public boolean isPositive;
    public Target(World world, float x, float y, int index) {

        img = PlayScreen.targetSpriteSheet;
        setRegion(PlayScreen.targetDyingAnimation.getKeyFrame(0));
        isPositive = true;
        double d = Math.random();
        if(d < 1){
            setColor(201/255f, 145 /255f, 217/255f, 1);
        }
        if (d < 0.85) {
            setColor(143/255f, 187/255f, 81/255f, 1);
        }
        if (d < 0.7){
            setColor(222/255f, 107/255f, 107/255f, 1);
        }
        if (d < 0.55){
            setColor(102/255f, 153 /255f, 204/255f, 1);
        }
        if (d < 0.4){
            setColor(241/255f, 151/255f, 71/255f, 1);
        }
        if (d < 0.25){
            setColor(235/255f, 214/255f, 58/255f, 1);
        }




        //setColor(com.badlogic.gdx.graphics.Color.BLACK);

        this.world = world;

        currentState = State.MOVING;
        previousState = State.MOVING;
        stateTimer = 0;



        this.index = index;
        defineTarget(x, y);
        this.x = x;
        this.y = y;
        speed = MathUtils.random(70, 100);
        //targetMoving = new TextureRegion(img, 0, 0, 163, 160);

        setBounds(0, 0, 264.875f / Constants.PPM, 260 / Constants.PPM);
        //setRegion(PlayScreen.targetMoving);

        setOrigin(getWidth() / 2, getHeight() / 2);
    }


    public void update(float dt){
        setPosition(pBody.getPosition().x - getWidth()/2, pBody.getPosition().y - getHeight()/2 - 0.3f);
        setRotation(pBody.getAngle() * MathUtils.radiansToDegrees);
       // setRegion(targetMoving);
        if(isDead){

            elapsedDeathTime += Gdx.graphics.getDeltaTime();
            setRegion(PlayScreen.targetDyingAnimation.getKeyFrame(elapsedDeathTime));



        }
     //  setRegion(getFrame(dt)); //getFrame will return appropriate frame
    }


    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case DYING:
                region = PlayScreen.targetDyingAnimation.getKeyFrame(stateTimer);
                break;
            case MOVING:
            default:
                region = PlayScreen.targetMoving.getKeyFrame(stateTimer);
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if(isDead == true) {
            return State.DYING;
        } else {
            return State.MOVING;
        }
    }

    public void defineTarget(float x, float y){
        BodyDef bdef = new BodyDef(); //body defenition:
        // describe physical properties that body will have. friction, etc.
        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.position.set(x / Constants.PPM,y / Constants.PPM);
        FixtureDef fdef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(128 / 2 / Constants.PPM);
        fdef.shape = shape;
        fdef.density = 1f;
        fdef.filter.categoryBits = Constants.BIT_TARGET; //is a
        fdef.filter.maskBits = Constants.BIT_SHURIKEN | Constants.BIT_WALL; //collides with
        fdef.filter.groupIndex = 0;
        fdef.isSensor = true;
        this.pBody = world.createBody(bdef);
        this.pBody.createFixture(fdef).setUserData(this);//set this shiruken fixture to consider it a shiruken object

        hitPoints = 10;


    }

    public void hit(float damage){
        Gdx.app.log("target: ", "i've been hit: " + hitPoints);
        hitPoints -= damage;
        if(hitPoints<=0){
            Gdx.app.log("target: ", "I died");
        }
        Hud.addScore(200);
    }

    public void update(){

    }
}
