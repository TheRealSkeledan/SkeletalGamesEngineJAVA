// Main.java

import Characters.Red;
import Engine.Map;
import Engine.Rooms;
import Engine.UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {
    // All of the initial variables

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    private boolean[] keys = new boolean[8]; // Add more to this if you want more keys
    private boolean p1isFacingRight = true;

    private Red p1;

    private long lastTime = System.nanoTime();
    private int fps = 0;
    private int frameCount = 0;

    // This is for optimised run
    private static final int TARGET_FPS = 300;
    private static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

    private ScheduledExecutorService executorService;

    private int renderWidth = SCREEN_WIDTH;
    private int renderHeight = SCREEN_HEIGHT;
    private int renderXOffset = 0;
    private int renderYOffset = 0;

    private static int[][] sampleRooms = {
        {1, 0, 1}, 
        {0, 1, 0}, 
        {1, 0, 1}
    };

    private int cameraX = 0;
    private int cameraY = 0;

    private static Rooms rooms;

    public Main() throws IOException {
        p1 = new Red(40, 300); // Create a player 1
        Map.setName("greenMap"); // Change the name for a different name (MUST MATCH THE IMAGE NAME)
        UI.create();

        rooms = new Rooms(sampleRooms, "skeld");

        this.setFocusable(true);
        this.addKeyListener(new Keyboard());

        setDoubleBuffered(true);

        // This handles the Executor Service
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::gameLoop, 0, OPTIMAL_TIME, TimeUnit.NANOSECONDS);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                adjustViewport();
            }
        });
    }

    // Makes it so it adds a black outline to the sides if the window is maxed
    private void adjustViewport() {
        Dimension size = getSize();
        double aspectRatio = (double) SCREEN_WIDTH / SCREEN_HEIGHT;
        double windowRatio = (double) size.width / size.height;

        if (windowRatio > aspectRatio) {
            renderHeight = size.height;
            renderWidth = (int) (size.height * aspectRatio);
            renderXOffset = (size.width - renderWidth) / 2;
            renderYOffset = 0;
        } else {
            renderWidth = size.width;
            renderHeight = (int) (size.width / aspectRatio);
            renderXOffset = 0;
            renderYOffset = (size.height - renderHeight) / 2;
        }
    }

    // The main loop of hte game
    private void gameLoop() {
        try {
            moveP1();
            p1.updatePosition();
            p1.updateAnimationFrame();
        
            repaint();
        
            long now = System.nanoTime();
            frameCount++;
            if (now - lastTime >= 1000000000) {
                fps = frameCount;
                frameCount = 0;
                lastTime = now;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This is where everything on the screen is drawn
    // Builds like layers from bottom to top
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.translate(renderXOffset, renderYOffset);
        g2d.scale((double) renderWidth / SCREEN_WIDTH, (double) renderHeight / SCREEN_HEIGHT);

        // Draw background
        Map.drawBackStage(g, 0 - cameraX, 0 - cameraY);

        // Draw the player at their absolute position
        if (p1isFacingRight) {
            g2d.drawImage(p1.getCurrentFrame(), 40, 300, this);
        } else {
            g2d.drawImage(p1.getCurrentFrame(), (40 + p1.getWidth()), 300,
                        -p1.getWidth(), p1.getHeight(), this);
        }

        // Draw front stage and UI
        Map.drawFrontStage(g, 0 - cameraX, 0 - cameraY);
        UI.drawUI(p1.getHP(), p1.getKP(), true, g2d, p1.name);

        // FPS Counter
        g2d.setColor(Color.RED);
        g2d.drawString("FPS: " + fps, 10, 10);

        g2d.dispose();
        g.dispose();
    }


    // Assigns a key that once pressed will perform an action
    // Make sure that the key is within range of the keys array. If it isn't, just add enough to accomodate
    private class Keyboard implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyChar()) {
                // Player 1
                case 'w' -> keys[0] = true;
                case 'a' -> {
                    keys[1] = true;
                    p1isFacingRight = false;
                }
                case 's' -> keys[2] = true;
                case 'd' -> {
                    keys[3] = true;
                    p1isFacingRight = true;
                }
                case 'f' -> keys[4] = true;
                case 'c' -> keys[5] = true;
                case 'q' -> keys[6] = true;
                case 'r' -> keys[7] = true;
            }
        }

        // Reset the keys so continuous movement doesn't occur
        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyChar()) {
                // Player 1
                case 'w' -> keys[0] = false;
                case 'a' -> keys[1] = false;
                case 'e' -> keys[2] = false;
                case 'd' -> keys[3] = false;
                case 'f' -> keys[4] = false;
                case 'c' -> keys[5] = false;
                case 'q' -> keys[6] = false;
                case 'r' -> keys[7] = false;
            }
        }
    }

    // Main function that makes the player functional
    public void moveP1() throws IOException {
        if(keys[0]) {
            p1.move(0, -p1.speed, Map.getWidth(), Map.getHeight());
        }
        
        if (keys[1]) {
            p1.move(-p1.speed, 0, Map.getWidth(), Map.getHeight());
        }

        if (keys[2]) {
            p1.move(0, p1.speed, Map.getWidth(), Map.getHeight());
        }
        
        if (keys[3]) {
            p1.move(p1.speed, 0, Map.getWidth(), Map.getHeight());
        }

        if(keys[6]) {
            p1.taunt();
        }

        resetAnimP1();

        cameraX = p1.getX() - (SCREEN_WIDTH / 2);
        cameraY = p1.getY() - (SCREEN_HEIGHT / 2);
    }

    // Resets once a key isn't being pressed
    public void resetAnimP1() {
        if (!keys[1] && !keys[2] && !keys[3] && !keys[4] && !keys[5] && !keys[6] && !keys[7] && !p1.jumping && !p1.isLocked) {
            p1.setAction("idle");
        }
    }

    public static void main(String[] args) throws IOException {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Skeletal Games Engine");
            frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            try {
                frame.setContentPane(new Main());
            } catch (IOException e) {
                e.printStackTrace();
            }
            frame.setVisible(true);
        });
    }
}