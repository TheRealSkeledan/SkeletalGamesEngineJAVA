// CharacterSelectPanel.java

package Panel;

import Abstract.Character;
import App.Main;
import Characters.Green;
import Characters.Red;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class CharacterSelectPanel extends JPanel {
    private final Main mainFrame;
    private final JLabel portraitLabel;
    private final JButton confirmButton;
    private final String[] characterNames = {"Red", "Green"};
    private int currentIndex = 0;
    private ImageIcon bgIcon, menuBG;

    public CharacterSelectPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        bgIcon = new ImageIcon(getClass().getResource("/assets/images/backgrounds/Menu/barThing.png"));
        menuBG = new ImageIcon(getClass().getResource("/assets/images/backgrounds/Menu/menuBG.png"));

        portraitLabel = new JLabel();
        portraitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        portraitLabel.setVisible(false);

        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> confirmSelection());
        confirmButton.setVisible(false);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(portraitLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(confirmButton);
        rightPanel.add(Box.createVerticalGlue());

        add(rightPanel, BorderLayout.CENTER);

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new Keyboard());

        showPortrait(characterNames[currentIndex]);
    }

    private void navigateLeft() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = characterNames.length - 1;
        }
        showPortrait(characterNames[currentIndex]);
    }

    private void navigateRight() {
        currentIndex++;
        if (currentIndex >= characterNames.length) {
            currentIndex = 0;
        }
        showPortrait(characterNames[currentIndex]);
    }

    private void showPortrait(String characterName) {
        try {
            String portraitPath = "/assets/images/characters/" + characterName.toLowerCase() + "Portrait.png";
            ImageIcon portraitIcon = new ImageIcon(getClass().getResource(portraitPath));

            portraitLabel.setIcon(portraitIcon);
            portraitLabel.setVisible(true);
            confirmButton.setVisible(true);
            repaint();
        } catch (Exception e) {
            System.out.println("Error loading portrait: " + e.getMessage());
        }
    }

    private void confirmSelection() {
        try {
            String selectedCharacterName = characterNames[currentIndex];
            Character selected = switch (selectedCharacterName) {
                case "Red" -> new Red(40, 300);
                case "Green" -> new Green(40, 300);
                default -> throw new IllegalStateException("Unexpected character: " + selectedCharacterName);
            };
            mainFrame.startGame(selected);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(menuBG.getImage(), 0, 0, 1280, 720, this);
        g.drawImage(bgIcon.getImage(), 0, 0, 1280, 720, this);
    }

    private class Keyboard implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyChar()) {
                case 'a' -> navigateLeft();
                case 'd' -> navigateRight();
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {}
    }
}