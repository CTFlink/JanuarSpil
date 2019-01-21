package main;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.settings.GameSettings;


import static com.almasb.fxgl.app.DSLKt.*;

public class DodgerApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings gameSettings) {
    }

    @Override
    protected void initGame(){
        getGameWorld().addEntityFactory(new DodgerFactory());

        getGameWorld().addEntity(Entities.makeScreenBounds(40));
        spawn("ball",30,30);
        spawn("bird",50,50);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0,0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
