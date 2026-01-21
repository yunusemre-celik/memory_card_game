package view;

import javax.swing.*;
import java.awt.*;

/**
 * The main menu screen of the application.
 * Provides the central navigation hub to access all game features.
 */
public class MenuPanel extends JPanel {
    private MainFrame frame;

    /**
     * Initializes the Menu UI with buttons and styling.
     * @param frame Reference to the main window controller.
     */
    public MenuPanel(MainFrame frame) {
        this.frame = frame;

        // Apply Cozy Pastel background
        setBackground(Theme.BG);
        
        // Use GridBagLayout for professional centering of elements
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Add spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make buttons fill the width
        gbc.gridx = 0;

        // 1. Game Title
        JLabel lblTitle = new JLabel("MEMORY MATCH");
        Theme.styleTitleLabel(lblTitle);
        
        gbc.gridy = 0;
        add(lblTitle, gbc);

        // 2. Subtitle / Decorative Line
        JLabel lblSubtitle = new JLabel("Train Your Brain!");
        lblSubtitle.setFont(Theme.FONT_HEADING.deriveFont(Font.ITALIC));
        lblSubtitle.setForeground(Theme.MUTED);
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 15, 30, 15); // Extra bottom margin
        add(lblSubtitle, gbc);

        // Reset insets for buttons
        gbc.insets = new Insets(10, 50, 10, 50);

        // 3. New Game Button
        JButton btnNewGame = Theme.createPrimaryButton("New Game");
        btnNewGame.addActionListener(e -> frame.openModeSelection());
        gbc.gridy = 2;
        add(btnNewGame, gbc);

        // 4. High Scores Button
        JButton btnHighScores = new JButton("High Scores");
        Theme.styleButton(btnHighScores, false);
        btnHighScores.addActionListener(e -> frame.showPanel("HighScores"));
        gbc.gridy = 3;
        add(btnHighScores, gbc);

        // 5. Settings Button
        JButton btnSettings = new JButton("Settings");
        Theme.styleButton(btnSettings, false);
        btnSettings.addActionListener(e -> frame.showPanel("Settings"));
        gbc.gridy = 4;
        add(btnSettings, gbc);

        // 6. Rules Button
        JButton btnRules = new JButton("How to Play");
        Theme.styleButton(btnRules, false);
        btnRules.addActionListener(e -> frame.showPanel("Rules"));
        gbc.gridy = 5;
        add(btnRules, gbc);

        // 7. Exit Button
        JButton btnExit = new JButton("Exit Game");
        Theme.styleButton(btnExit, false);
        btnExit.setBackground(new Color(0xE06A6A)); // Soft red for exit
        btnExit.setForeground(Theme.BG);
        btnExit.addActionListener(e -> System.exit(0));
        gbc.gridy = 6;
        gbc.insets = new Insets(30, 50, 10, 50); // Extra top margin
        add(btnExit, gbc);
    }


}