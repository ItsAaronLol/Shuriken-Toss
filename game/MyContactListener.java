package com.shirukentoss.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.shirukentoss.game.Scenes.Hud;
import com.shirukentoss.game.Screens.PlayScreen;

/**
 * Created by AaronW on 18/04/16.
 */
public class MyContactListener implements ContactListener{

    private Array<Body> bodiesToRemove;
    private ShirukenToss game;


    public MyContactListener(){
        super();
        bodiesToRemove = new Array<Body>();
    }

    @Override
    public void beginContact(Contact contact) {



        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        if(isTargetContact(fa, fb)){

            String faClassName = fa.getUserData().getClass().toString().substring(fa.getUserData().getClass().toString().lastIndexOf(".")+1);
            String fbClassName = fb.getUserData().getClass().toString().substring(fb.getUserData().getClass().toString().lastIndexOf(".")+1);
            Gdx.app.log("className: ", faClassName);
            //check if the object that got hit has the target class
            if(faClassName.equals("Target") && fbClassName.equals("Shiruken")){

                final Target tba = (Target) fa.getUserData();
                Shiruken tbb = (Shiruken) fb.getUserData();
                if(!tba.isDead){
                    tba.isDead = true;

//                    tba.pBody.setLinearVelocity(tbb.pBody.getLinearVelocity().x/4,tbb.pBody.getLinearVelocity().x/4);
                  //  tba.pBody.setAngularVelocity(tbb.pBody.getAngularVelocity()/16);
                    tba.pBody.setLinearVelocity(0,0);

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            bodiesToRemove.add(tba.pBody);
                            tba.setAlpha(0);
                            PlayScreen.targetObjects.remove(tba);
                            tba.pBody.setActive(false);
                        }
                    }, 0.5f);

                    tbb.hit();
                    PlayScreen.playImpactSound();

//                tba.getTexture().dispose();
//                tba.setAlpha(0);
                    //PlayScreen.targetSprites.get(tba.index).getTexture().dispose();
                    //  PlayScreen.targetSprites.get(tba.index).setAlpha(0);
                    PlayScreen.targetsRemaining -= 1;

                    if(PlayScreen.targetsRemaining <= 0){
                        Gdx.app.log("endwave", "end wave");
                        PlayScreen.waveNumber += 1;
                        PlayScreen.endWave();
                    }

                    Hud.addScore(100 * tbb.kills + PlayScreen.killStreak * 5);
                    PlayScreen.killStreak += 1;
                    Hud.addShuriken(1);
                }

            }

            if(faClassName.equals("Shiruken") && fbClassName.equals("Target")){
                Shiruken tba = (Shiruken) fa.getUserData();
                final Target tbb = (Target) fb.getUserData();
                if(!tbb.isDead){
                    bodiesToRemove.add(tbb.pBody);
                    tbb.isDead = true;

//                    tbb.pBody.setLinearVelocity(tbb.pBody.getLinearVelocity().x/4,tbb.pBody.getLinearVelocity().x/4);
//                    tbb.pBody.setAngularVelocity(tba.pBody.getAngularVelocity()/16);
                    tbb.pBody.setLinearVelocity(0,0);
                    //wait one second before removing the body
                    //  tbb.pBody.setActive(false);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            bodiesToRemove.add(tbb.pBody);
                            PlayScreen.targetObjects.remove(tbb);
                            tbb.setAlpha(0);
                            tbb.pBody.setActive(false);
                        }
                    }, 0.5f);
                    tba.hit();
                    PlayScreen.playImpactSound();

//                tbb.getTexture().dispose();
//                tbb.setAlpha(0);
                    //  PlayScreen.targetSprites.get(tbb.index).getTexture().dispose();
                    //  PlayScreen.targetSprites.get(tbb.index).setAlpha(0);
                    PlayScreen.targetsRemaining -= 1;

                    if(PlayScreen.targetsRemaining <= 0){
                        Gdx.app.log("endwave", "end wave");
                        PlayScreen.waveNumber += 1;
                        PlayScreen.endWave();
                    }
                    Hud.addScore(100 * tba.kills + PlayScreen.killStreak * 5);
                    PlayScreen.killStreak += 1;
                    Hud.addShuriken(1);
                }

                if(!tbb.isDead && !tbb.isPositive){

                }
            }

            //shurikens killed by walls

            if(!faClassName.equals("Target") && fbClassName.equals("Shiruken")){
                Shiruken tbb = (Shiruken) fb.getUserData();
                bodiesToRemove.add(tbb.pBody);
                PlayScreen.tmpShirukens.remove(tbb);

                if(tbb.kills == 0){
                    PlayScreen.killStreak = 0;
                }

                //PlayScreen.tmpSprites.get(tbb.index).getTexture().dispose();


            }
            if(faClassName.equals("Shiruken") && !fbClassName.equals("Target")){
                Shiruken tba = (Shiruken) fa.getUserData();
                bodiesToRemove.add(tba.pBody);
                PlayScreen.tmpShirukens.remove(tba);
                if(tba.kills == 0){
                    PlayScreen.killStreak = 0;
                }


                //PlayScreen.tmpSprites.get(tba.index).getTexture().dispose();

            }

            //targets killed by walls
            if(faClassName.equals("Target") && !fbClassName.equals("Shiruken")){
                Target tba = (Target) fa.getUserData();

                bodiesToRemove.add(tba.pBody);
                tba.setAlpha(0);
                PlayScreen.targetsRemaining -= 1;
                if(tba.isPositive){
                    PlayScreen.lives -= 1;
                }
                if(PlayScreen.lives == 0){
                    PlayScreen.endGame();
                }

                if(PlayScreen.targetsRemaining <= 0){
                    Gdx.app.log("endwave", "end wave");
                    PlayScreen.waveNumber += 1;
                    PlayScreen.endWave();
                }


            }
            if(!faClassName.equals("Shiruken") && fbClassName.equals("Target")){
                Target tbb = (Target) fb.getUserData();

                bodiesToRemove.add(tbb.pBody);
                tbb.setAlpha(0);
                PlayScreen.targetsRemaining -= 1;
                if(tbb.isPositive){
                    PlayScreen.lives -= 1;
                }
                if(PlayScreen.lives == 0){
                    PlayScreen.endGame();
                }

                if(PlayScreen.targetsRemaining <= 0){
                    Gdx.app.log("endwave", "end wave");
                    PlayScreen.waveNumber += 1;
                    PlayScreen.endWave();
                }


            }













//            tba.hit(tbb.damage);
//
//            if(!tbb.isBouncy){
//                bodiesToRemove.add(tbb.pBody);
//            }
//
//            if(tba.hitPoints <= 0){
//                bodiesToRemove.add(tba.pBody);
//            }
        }

    }
    public Array<Body> getBodiesToRemove(){
        return bodiesToRemove;
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private boolean isTargetContact(Fixture a, Fixture b){ //check what kind of fixtures are colliding

        if(a.getUserData() instanceof Target || b.getUserData() instanceof Target){
            if(a.getUserData() instanceof Shiruken || b.getUserData() instanceof Shiruken){
                //is one of them a target and one of them a shiruken
                return true;
            }
        }

        return true;
       // return (a.getUserData() instanceof Target && b.getUserData() instanceof Shiruken);
    }
}
