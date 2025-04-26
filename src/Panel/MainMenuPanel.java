// MainMenuPanel.java

package Panel;

import App.Main;
import java.awt.*;
import javax.swing.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(Main mainFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Start Button
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> mainFrame.showCharacterSelect());
        gbc.gridy = 0;
        add(startButton, gbc);
    }
}