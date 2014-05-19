package sokoban.io;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class ImageReader {
    private static final String filepath = "./images/";

    private static HashMap<String, Image> images = new HashMap<String, Image>();

    public static Image getImage (String name) {
        name = name.toUpperCase();

        if (images.containsKey(name)) return images.get(name);

        Image image;
        String filename;
        switch (name) {
            case "PERSONUP":
                filename = "PersonUp.gif";
                break;
            case "PERSONDOWN":
                filename = "PersonDown.gif";
                break;
            case "PERSONLEFT":
                filename = "PersonLeft.gif";
                break;
            case "PERSONRIGHT":
                filename = "PersonRight.gif";
                break;
            case "CRATE":
                filename = "Crate.gif";
                break;
            case "WALL":
                filename = "Wall.gif";
                break;
            case "TARGET":
                filename = "Target.gif";
                break;
            case "FLOOR":
                filename = "Floor.gif";
                break;
            case "DOG":
                filename = "Dog.jpg";
                break;
            default:
                return null;
        }

        try {
            image = ImageIO.read(new File(filepath + filename));
        } catch (IOException e) {
            image = null;
        }

        if (image != null) images.put(name, image);
        return image;
    }
}