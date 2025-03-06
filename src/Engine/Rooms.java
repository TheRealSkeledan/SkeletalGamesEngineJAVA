package Engine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

public class Rooms {
    private int[][] rooms;
    private String packageDir;
    private BufferedImage[] walls;
    private BufferedImage door;

    public Rooms(int[][] rooms, String packageDirectory) {
        this.rooms = rooms;
        packageDir = "assets/images/rooms/" + packageDirectory;
        loadWallImages();
    }

    private void loadWallImages() {
        File dir = new File(packageDir);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Invalid directory: " + packageDir);
            return;
        }

        File[] imageFiles = dir.listFiles((d, name) -> name.matches("wall\\d+\\.png"));

        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("No wall images found.");
            return;
        }

        Arrays.sort(imageFiles, Comparator.comparingInt(Rooms::extractNumber));

        walls = new BufferedImage[imageFiles.length];
        for (int i = 0; i < imageFiles.length; i++) {
            try {
                walls[i] = ImageIO.read(imageFiles[i]);
            } catch (IOException e) {
                System.out.println("Error loading: " + imageFiles[i].getName());
                e.printStackTrace();
            }
        }
        System.out.println("Loaded " + walls.length + " wall images.");
    }

    private static int extractNumber(File file) {
        Pattern pattern = Pattern.compile("wall(\\d+)\\.png");
        Matcher matcher = pattern.matcher(file.getName());
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : Integer.MAX_VALUE;
    }

    public void generate(Graphics g) {
        int[] toGenerate = rooms[(int) (Math.random() * rooms.length)];
        
        for (int i = 0; i < toGenerate.length; i++) {
            if (toGenerate[i] == 1) {
                if (walls != null && walls.length > 0) {
                    int xPosition = i * walls[0].getWidth();
                    g.drawImage(walls[(int)(Math.random() * walls.length)], xPosition, 0, null);
                }
            }
        }
    }
    
}