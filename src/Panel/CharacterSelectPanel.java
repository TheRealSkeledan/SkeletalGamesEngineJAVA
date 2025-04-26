// CharacterSelectPanel.java

package Panel;

import Abstract.Character;
import App.Main;
import Characters.Green;
import Characters.Red;
import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class CharacterSelectPanel extends JPanel {
    private Main mainFrame;

    public CharacterSelectPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton redButton = new JButton("Red");
        JButton greenButton = new JButton("Green");

        redButton.addActionListener(e -> selectCharacter("Red"));
        greenButton.addActionListener(e -> selectCharacter("Green"));

        gbc.gridy = 0;
        add(redButton, gbc);

        gbc.gridy = 1;
        add(greenButton, gbc);
    }

    private void selectCharacter(String characterName) {
        try {
            Character selectedCharacter = null;
            switch (characterName) {
                case "Red" -> selectedCharacter = new Red(40, 300);
                case "Green" -> selectedCharacter = new Green(40, 300);
            }
            
            mainFrame.startGame(selectedCharacter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}