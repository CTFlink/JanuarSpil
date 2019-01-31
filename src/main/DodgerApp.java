package main;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsParticleComponent;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.Map;


//static import gør det muligt at undgå at skrive DSLKT hver gang dens metoder bliver brugt.
import static com.almasb.fxgl.app.DSLKt.*;



public class DodgerApp extends GameApplication {

    /**
     * En typisk main method der starter programmet.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
    }

    /**
     * Her angives der hvad der sker når brugeren trykker på tastaturtasterne  a,d,s,w
     */
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

    /**
     * her bliver brugerens indtastede retning sat til at bevæge fuglene.
     * Direction bliver forskudt for hver bird enhed vi har. Så venstre bliver til højre, højre bliver til ned osv.
     * Dette gøres for at fulgene bliver svære at styre.
     * @param direction
     */
    private void move(Direction direction) {

        for (Entity bird : getGameWorld().getEntitiesByType(EntityType.BIRD)) {
            BirdComponent comp = bird.getComponent(BirdComponent.class);

            comp.move(direction);

            direction = direction.next();
        }
    }

    /**
     * Liv sættes til 3 og score sættes til 0 hver eneste gang spillet startes.
     * Dette gøre for at tælleren ikke bare fortsætter når brugeren genstarter spillet.
     * @param vars
     */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("lives", 3);
        vars.put("score", 0);
    }

    /**
     * Her ændres generelle settings ved spillet.
     */
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

    /**
     *
     */
    private void respawnEntities(){

        Entity bounds = Entities.makeScreenBounds(40);
        bounds.setType(EntityType.SCREEN);
        bounds.addComponent(new CollidableComponent(true));

        getGameWorld().addEntity(bounds);

        spawn("ball", getWidth()/2, 30);

        for (int i = 0; i < 4; i++) {
            double x = random(i * 150, i * 150 + 150);
            double y = random(400, 600 - 60);
            spawn("bird", x, y);

        }
    }

    /**
     * initPhysics metoden giver mulighed for at ændre på hvordan ting bevæger sig i forhold til hinanden.
     */
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

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.SCREEN) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity screen) {
                ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter(100);

                Entity e = new Entity();
                e.setPosition(ball.getCenter());
                e.addComponent(new ParticleComponent(emitter));

                getGameWorld().addEntity(e);
            }
        });
    }

    /*
     * Her overskrives den måde UI'en ser ud på som default.
     * Vi gør det her ved at tilføje en tekst (der består af tal) med antal liv og en med spillerens score
     */
    @Override
    protected void initUI() {
        Text textLives = addVarText(10, 20, "lives");
        textLives.setFont(getUIFactory().newFont(26));

        Text textScore = addVarText(700, 20, "score");
        textScore.setFont(getUIFactory().newFont(26));
    }

    @Override
    protected void onUpdate(double tpf) {
        inc("score", +1 );
    }
}