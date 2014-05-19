package sokoban.io;

import platform.core.GameObject;
import platform.geometry.Position;
import platform.geometry.Rectangle;
import sokoban.gameobjects.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MapReader {
    private int uniformGameObjectLength; // This is the size of each cell in pixels when reading the number grid format.

    private ArrayList<GameObject> gameObjects = null;
    private BufferedImage background;
    private Graphics backgroundGraphics;

    // Special images for constructing the background.
    Image floor = ImageReader.getImage("Floor");

    public MapReader (String filename, int uniformGameObjectLength, Rectangle area) throws Exception {
        this.uniformGameObjectLength = uniformGameObjectLength;
        this.background = new BufferedImage(area.pointLowerRight.x, area.pointLowerRight.y, BufferedImage.TYPE_INT_RGB);
        this.backgroundGraphics = this.background.createGraphics();

        BufferedReader input = null;
        Exception error = null;
        try {
            input = new BufferedReader(new FileReader(filename));
            gameObjects = new ArrayList<GameObject>();

            String currentLine = input.readLine();

            if (filename.endsWith("map") && currentLine.contains(",")) // Figure list format.
            {
                while (currentLine != null) {
                    GameObject currentObject = readFigureLine(currentLine);
                    if (currentObject != null) gameObjects.add(currentObject);

                    currentLine = input.readLine(); // Read the next line to determine whether to continue.
                }
            } else if (filename.endsWith(".map")) // Numerical grid format.
            {
                int lineLength = currentLine.trim().length();
                Position position = new Position(0, 0);
                while (currentLine != null) {
                    GameObject[] currentLineGameObjects = readNumericLine(currentLine, lineLength, position, uniformGameObjectLength);
                    if (currentLineGameObjects != null)
                        for (GameObject currentGameObject : currentLineGameObjects)
                            if (currentGameObject != null) gameObjects.add(currentGameObject);

                    currentLine = input.readLine(); // Read the next line to determine whether to continue.
                    position.y += uniformGameObjectLength;
                }
            } else throw new java.io.IOException();

        } catch (Exception e) {
            error = e;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
            }

            if (error != null) throw error;
        }
    }

    public ArrayList<GameObject> getGameObjects () {
        return this.gameObjects;
    }

    public Image getBackground () {
        return this.background;
    }

    /*
     * This method reads in lines following the figure details format. Each line contains the name and position
     * of a single figure which is then recreated. An example line following this format:
     * Wall,30,60
     * */
    private GameObject readFigureLine (String line) {
        String[] parameter = line.split(",");
        if (parameter.length != 3) return null;

        // Deal with the figure type name.
        String figureClass = parameter[0].trim();

        // Deal with the position parameters.
        int x = Integer.parseInt(parameter[1].trim());
        int y = Integer.parseInt(parameter[2].trim());

        // Create the actual object
        GameObject currentObject = null;
        switch (figureClass.toUpperCase()) {
            case "FLOOR":
                this.backgroundGraphics.drawImage(floor, x, y, uniformGameObjectLength, uniformGameObjectLength, null);
                break;
            case "PLAYER":
                currentObject = new Player(x, y);
                this.backgroundGraphics.drawImage(floor, x, y, uniformGameObjectLength, uniformGameObjectLength, null);
                break;
            case "CRATE":
                currentObject = new Crate(x, y);
                this.backgroundGraphics.drawImage(floor, x, y, uniformGameObjectLength, uniformGameObjectLength, null);
                break;
            case "WALL":
                currentObject = new Wall(x, y);
                break;
            case "TARGET":
                currentObject = new Target(x, y);
                break;
            default:
                return null;
        }

        return currentObject;
    }

    /*
     * This method reads in lines following the terse numeric map format for figures of uniform size.
     * Each line consists in a series of numbers, each representing a specific kind of Figure. Positions
     * for figures are calculated automatically based on the location of each number within the file
     * and the size of each uniform figure as passed by the parameters height and width (measured in
     * pixels). Each line read should be identical, so the expected number of figures is passed, and
     * a figure list is only returned if this expected number of figures is matched. An example line:
     * 00000011132341000000
     * */
    private GameObject[] readNumericLine (String line, int expectedNumberOfGameObjects, Position startingPosition,
                                          int objectWidth) {
        line = line.trim();
        char[] character = line.toCharArray();
        if (character.length != expectedNumberOfGameObjects) return null;

        GameObject[] objects = new GameObject[expectedNumberOfGameObjects];

        Position position = startingPosition.clone();
        for (int x = 0; x < expectedNumberOfGameObjects; x++) {
            switch (Integer.parseInt(String.valueOf(character[x]))) {
                case 0: // Nothing
                    objects[x] = null;
                    break;
                case 1: // Floor
                    objects[x] = null;
                    this.backgroundGraphics.drawImage(floor, position.x, position.y, uniformGameObjectLength, uniformGameObjectLength, null);
                    break;
                case 2: // Wall
                    objects[x] = new Wall(position);
                    break;
                case 3: // Crate
                    objects[x] = new Crate(position);
                    this.backgroundGraphics.drawImage(floor, position.x, position.y, uniformGameObjectLength, uniformGameObjectLength, null);
                    break;
                case 4: // Target
                    objects[x] = new Target(position);
                    break;
                case 5: // Player
                    objects[x] = new Player(position);
                    this.backgroundGraphics.drawImage(floor, position.x, position.y, uniformGameObjectLength, uniformGameObjectLength, null);
                    break;
                case 6: // Dog
                    objects[x] = new Dog(position);
                    this.backgroundGraphics.drawImage(floor, position.x, position.y, uniformGameObjectLength, uniformGameObjectLength, null);
                    break;
                case 7: // GreenPoint
                    objects[x] = new GreenPoint(position);
                    this.backgroundGraphics.drawImage(floor, position.x, position.y, uniformGameObjectLength, uniformGameObjectLength, null);
                    break;
                default:
                    return null;
            }

            position.x += objectWidth;
        }

        return objects;
    }
}