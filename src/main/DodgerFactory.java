package main;

import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.extra.entity.components.KeepOnScreenComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.BoundingBox;
import javafx.util.Duration;

import static com.almasb.fxgl.app.DSLKt.texture;

public class DodgerFactory implements EntityFactory {

    @Spawns ("ball")
    public Entity newBall (SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().restitution(1.0f));
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(350,350));


        return Entities.builder()
                .type(EntityType.BALL)
                .from(data)
                .viewFromNodeWithBBox(texture("ball.png",40,40))
                .with(physics)
                .with(new RandomVelocityComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns ("bird")
    public Entity newplayer (SpawnData data){
        return Entities.builder()
                .type(EntityType.BIRD)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(70,60)))
                .viewFromAnimatedTexture("bird.png",2, Duration.seconds(0.5))
                .with(new BirdComponent())
                .with(new KeepOnScreenComponent(true, true))
                .with(new CollidableComponent(true))
                .build();
    }
}
