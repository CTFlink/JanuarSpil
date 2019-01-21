package main;

import com.almasb.fxgl.entity.component.Component;

public class BirdComponent extends Component {

    public void move(Direction direction){
        entity.translate(direction.vector.multiply(5));
    }
}
