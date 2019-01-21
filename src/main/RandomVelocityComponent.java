package main;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

public class RandomVelocityComponent extends Component {

    private LocalTimer timer = FXGL.newLocalTimer();

    private PhysicsComponent physics;

    @Override
    public void onUpdate(double tpf) {

        if (timer.elapsed(Duration.seconds(2))){
            physics.setLinearVelocity(FXGLMath.randomPoint2D().multiply(350));
            timer.capture();
        }

    }
}
