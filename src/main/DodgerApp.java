package main;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.Map;

import static com.almasb.fxgl.app.DSLKt.*;


public class DodgerApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings gameSettings) {
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.A, "Left", () -> {
            move(Direction.LEFT);
        });
        onKey(KeyCode.D, "Right", () -> {
            move(Direction.RIGHT);
        });
        onKey(KeyCode.S, "Down", () -> {
            move(Direction.DOWN);
        });
        onKey(KeyCode.W, "Up", () -> {
            move(Direction.UP);
        });
    }

    private void move(Direction direction) {

        for (Entity bird : getGameWorld().getEntitiesByType(EntityType.BIRD)) {
            BirdComponent comp = bird.getComponent(BirdComponent.class);

            comp.move(direction);

            direction = direction.next();
        }
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("lives", 3);
        vars.put("score", 0);
    }

    @Override
    protected void initGame() {

        getGameState().<Integer>addListener("lives", (prev, now) -> {
            if (now == 0) {
                getDisplay().showConfirmationBox("Game Over. Continue?", yes -> {
                    if (yes) {
                        startNewGame();}
                    else {
                        exit();}
                });
            }
        });

        getGameScene().setBackgroundColor(Color.BLACK);

        getGameWorld().addEntityFactory(new DodgerFactory());

        respawnEntities();
    }

    private void respawnEntities(){

        getGameWorld().addEntity(Entities.makeScreenBounds(40));

        spawn("ball", getWidth()/2, 30);

        for (int i = 0; i < 4; i++) {
            double x = random(i * 150, i * 150 + 150);
            double y = random(400, 600 - 60);
            spawn("bird", x, y);

        }
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0,0);

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BIRD, EntityType.BALL) {
            @Override
            protected void onCollisionBegin(Entity bird, Entity ball) {
                play("hit_wall.wav");

                inc("lives", -1);

                getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);

                respawnEntities();

            }
        });
    }

    @Override
    protected void initUI() {
        Text textLives = addVarText(10, 20, "lives");
        textLives.setFont(getUIFactory().newFont(26));

        Text textScore = addVarText(750, 20, "score");
        textScore.setFont(getUIFactory().newFont(26));
    }

    public static void main(String[] args) {
        launch(args);
    }
}