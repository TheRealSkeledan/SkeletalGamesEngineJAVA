// Main.java

package App;

import Abstract.Character;
import Engine.GamePanel;
import Panel.CharacterSelectPanel;
import Panel.MainMenuPanel;
import java.io.IOException;
import javax.swing.*;

public class Main extends JFrame {
    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    public Main() throws IOException {
        setTitle("VS IMPOSTER Fighting Game thing");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showMainMenu();
        setVisible(true);
    }

    public void showMainMenu() throws IOException {
        setContentPane(new MainMenuPanel(this));
        revalidate();
    }

    public void showCharacterSelect() {
        setContentPane(new CharacterSelectPanel(this));
        revalidate();
    }

    public void startGame(Character playerCharacter) throws IOException {
        GamePanel gamePanel = new GamePanel(playerCharacter);
        setContentPane(gamePanel);
        revalidate();
        
        gamePanel.requestFocusInWindow();
        gamePanel.repaint();
    }
    
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            try {
                new Main();
            } catch (IOException ex) {
            }
        });
    }
}