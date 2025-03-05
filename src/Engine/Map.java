package Engine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// This is where the map is created for the game.
public class Map {
    private static String name;
    private static BufferedImage foreground, background;

    public static void setName(String n) {
        name = n;

        try {
            foreground = ImageIO.read(new File("assets/images/backgrounds/" + name + "-front.png"));
            background = ImageIO.read(new File("assets/images/backgrounds/" + name + "-back.png"));
        } catch (IOException e) {
            System.out.println("Path couldn't find the file, " + name);
        }
    }

    public static void setName(String nBack, String nFront) {
        name = nBack + " Background";

        try {
            foreground = ImageIO.read(new File("assets/images/backgrounds/" + nBack + "-front.png"));
            background = ImageIO.read(new File("assets/images/backgrounds/" + nFront + "-back.png"));
        } catch (IOException e) {
            System.out.println("Path couldn't find the file, " + name);
        }
    }

    public static String getName() {
        return name;
    }

    public static BufferedImage getForeground() {
        return foreground;
    }

    public static BufferedImage getBackground() {
        return background;
    }

    public static void drawBackStage(Graphics g) {
        g.drawImage(getBackground(), 0, 0, null);
    }

    public static void drawFrontStage(Graphics g) {
        g.drawImage(getForeground(), 0, 0, null);
    }

    public static int getWidth() {
        return foreground != null ? foreground.getWidth() : 0;
    }

    public static int getHeight() {
        return foreground != null ? foreground.getHeight() : 0;
    }
}