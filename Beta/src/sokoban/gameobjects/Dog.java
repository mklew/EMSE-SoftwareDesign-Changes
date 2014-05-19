package sokoban.gameobjects;

import platform.core.GameObject;
import platform.core.Movement;
import platform.geometry.Position;
import sokoban.interactions.Collision;
import sokoban.io.ImageReader;

import java.awt.*;
import java.util.ArrayList;

import static platform.geometry.Transform.transform;

/**
 * @author Marek Lewandowski <marek.lewandowski@icompass.pl>
 * @since 19/05/14
 */
public class Dog extends GameObject {

    private static int size = 30;

    private static Image image = ImageReader.getImage("DOG");

    private static int drawPriority = 2;

    public Dog (Position position) {
        super(position);
    }

    public Dog (int x, int y) {
        super(x, y);
    }

    @Override public int getHeight () {
        return size;
    }

    @Override public int getWidth () {
        return size;
    }

    @Override public void draw (Graphics g) {
        if (image != null)
            g.drawImage(image, this.position.x, this.position.y, size, size, null);
    }

    @Override public int drawPriority () {
        return drawPriority;
    }

    @Override public boolean movementQuery (Movement movement) {
        platform.geometry.Rectangle movementArea = transform(this.getArea(), movement.getInitialVector());
        ArrayList<GameObject> solidObjects = Collision.getSolidObjectsInArea(movement.getMap(), movementArea);
        final ArrayList<GameObject> targetObjectsInArea = Collision.getTargetObjectsInArea(movement.getMap(), movementArea);

        if (solidObjects.isEmpty() && targetObjectsInArea.isEmpty()) {
            movement.add(this, movement.getInitialVector());
            return true;
        }

        return false;
    }

    @Override public void postMovementAction (Movement movement) {
        return;
    }
}
