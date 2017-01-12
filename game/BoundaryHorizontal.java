package com.shirukentoss.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.shirukentoss.game.Scenes.Hud;
import com.shirukentoss.game.utils.Constants;

/**
 * Created by AaronW on 18/04/16.
 */
public class BoundaryHorizontal {

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


    public BoundaryHorizontal(World world, float x, float y) {
        this.world = world;
        defineBoundary(x, y);
        this.x = x;
        this.y = y;
        speed = MathUtils.random(70, 100);
    }


    public void defineBoundary(float x, float y){

        BodyDef bdef = new BodyDef(); //body defenition:
        // describe physical properties that body will have. friction, etc.
        bdef.type = BodyDef.BodyType.StaticBody;

        bdef.position.set(x / Constants.PPM,y / Constants.PPM);
        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1500 / 2 / Constants.PPM, 50 / 2 / Constants.PPM);
        fdef.shape = shape;
        fdef.density = 1f;
        fdef.filter.categoryBits = Constants.BIT_WALL; //is a
        fdef.filter.maskBits = Constants.BIT_SHURIKEN | Constants.BIT_TARGET; //collides with
        fdef.filter.groupIndex = 0;

        this.pBody = world.createBody(bdef);
        this.pBody.createFixture(fdef).setUserData(this);//set this shiruken fixture to consider it a shiruken object

        hitPoints = 99999999999999f;


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
