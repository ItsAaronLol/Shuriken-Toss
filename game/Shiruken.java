package com.shirukentoss.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.graphics.ParticleEmitterBox2D;
import com.shirukentoss.game.Screens.PlayScreen;
import com.shirukentoss.game.utils.Constants;

/**
 * Created by AaronW on 31/03/16.
 */
public class Shiruken extends Sprite{
    public World world;
    public Body pBody;
    public boolean isThrown = false;
    public float damage = 10f;
    public boolean isBouncy = false;
    public ParticleEffect pe;
    public int index;
    Sprite sprite;

    public int kills;


    public Vector2 currentVelocity = new Vector2(0,0);
    public Vector2 lastPosition = new Vector2(0,0);


    Texture img;
    private TextureRegion shuriken;


    public Shiruken(World world, float x, float y, int index) {


        img = PlayScreen.shurikenSprite;
        this.index = index;
        this.world = world;
        defineShiruken(x, y);


        shuriken = new TextureRegion(img, 0, 0, 531, 531);
        setBounds(0, 0, 531/Constants.PPM, 531/Constants.PPM);
        setRegion(shuriken);
        setSize(128 / Constants.PPM, 128 / Constants.PPM);
        setOrigin(getWidth() / 2, getHeight() / 2);
//        setTexture(img);
//        setSize(128 / Constants.PPM, 128 / Constants.PPM);
//        setOrigin(getWidth() / 2, getHeight() / 2);

    }


    public void update(float dt){
      //  Gdx.app.log("setpos", "setpos");
        setPosition(pBody.getPosition().x - getWidth() / 2, pBody.getPosition().y - getHeight() / 2);
        setRotation(pBody.getAngle() * MathUtils.radiansToDegrees);
    }
    public void defineShiruken(float x, float y){
        BodyDef bdef = new BodyDef(); //body defenition:
        // describe physical properties that body will have. friction, etc.
        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.position.set(x / Constants.PPM,y / Constants.PPM);
        FixtureDef fdef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(128 / 2 / Constants.PPM);
        fdef.shape = shape;
        fdef.density = 9999999f;
        fdef.filter.categoryBits = Constants.BIT_SHURIKEN; //is a
        fdef.filter.maskBits = Constants.BIT_TARGET | Constants.BIT_WALL; //collides with
        fdef.filter.groupIndex = 0;

        this.pBody = world.createBody(bdef);
        this.pBody.createFixture(fdef).setUserData(this);//set this shiruken fixture to consider it a shiruken object





       /* pe = new ParticleEffect();

        pe.load(Gdx.files.internal("part"), Gdx.files.internal(""));
        pe.getEmitters().first().setPosition(pBody.getPosition().x, pBody.getPosition().y);

        pe.start();
        pBody.setUserData(pe);
        ShirukenToss.bodies.add(pBody);*/



    }

    public void hit(){

        kills += 1;
    }
}
