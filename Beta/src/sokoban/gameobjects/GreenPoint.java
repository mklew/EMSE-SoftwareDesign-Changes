package sokoban.gameobjects;

import platform.core.GameObject;
import platform.core.Movement;
import platform.geometry.Position;
import sokoban.io.ImageReader;

import java.awt.*;

/**
 * @author Marek Lewandowski <marek.lewandowski@icompass.pl>
 * @since 19/05/14
 */
public class GreenPoint extends GameObject {

    private static int size = 30;

    private static Image image = ImageReader.getImage("GREEN");

    private static int drawPriority = 2;

    public GreenPoint (Position position) {
        super(position);
    }

    public GreenPoint (int x, int y) {
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
        return true;
    }

    @Override public void postMovementAction (Movement movement) {
        return;
    }

    @Override public boolean isGreenPoint () {
        return true;
    }
}
