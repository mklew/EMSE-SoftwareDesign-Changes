package sokoban.gameobjects;

import platform.core.GameObject;
import platform.core.Movement;
import platform.geometry.Position;
import platform.geometry.Rectangle;
import sokoban.interactions.Collision;
import sokoban.io.ImageReader;

import java.awt.*;
import java.util.ArrayList;

import static platform.geometry.Transform.transform;

public class Crate extends GameObject {
    private static int height = 30, width = 30;
    private static Image crateImage = ImageReader.getImage("Crate");
    private static Image crateOnTargetImage = ImageReader.getImage("CrateOnTarget");
    private static int drawPriority = 2;

    private boolean isOnTheTarget = false; // TODO need to be checked what are the initial conditions

    public Crate (Position position) {
        super(position);
    }

    public Crate (int x, int y) {
        super(x, y);
    }

    public int getHeight () {
        return height;
    }

    public int getWidth () {
        return width;
    }

    public void draw (Graphics g) {

        if (Crate.crateImage != null && !isOnTheTarget)
            g.drawImage(Crate.crateImage, this.position.x, this.position.y, width, height, null);
        else if (isOnTheTarget) {
            g.drawImage(Crate.crateOnTargetImage, this.position.x, this.position.y, width, height, null);
        }
    }

    public int drawPriority () {
        return drawPriority;
    }

    @Override
    public boolean movementQuery (Movement movement) {
        if (isOnTheTarget) {
            return false;
        } else {
            Rectangle movementArea = transform(this.getArea(), movement.getInitialVector());
            ArrayList<GameObject> solidObjects = Collision.getSolidObjectsInArea(movement.getMap(), movementArea);

            if (solidObjects.isEmpty()) {
                movement.add(this, movement.getInitialVector());
                return true;
            }

            return false;
        }
    }

    @Override
    public void postMovementAction (Movement movement) {
        final ArrayList<GameObject> targetObjectsInArea = Collision.getTargetObjectsInArea(movement.getMap(), getArea());
        isOnTheTarget = targetObjectsInArea.size() > 0;

        final ArrayList<GameObject> greenPointsInArea = Collision.getGreenPointsInArea(movement.getMap(), getArea());
        if (greenPointsInArea.size() > 0) {
            movement.getMap().removeObject(this);
        }

        return;
    }
}