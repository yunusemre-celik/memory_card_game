package view;

import javax.swing.*;
import java.awt.*;

/**
 * Displays the game instructions and rules to the player.
 * Uses a read-only text area to show detailed information.
 */
public class RulesPanel extends JPanel {
    private MainFrame frame;

    /**
     * Initializes the Rules UI.
     * @param frame Reference to the main window controller.
     */
    public RulesPanel(MainFrame frame) {
        this.frame = frame;

        // Apply dark theme background
        setBackground(new Color(45, 45, 45));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // 1. Title Section
        JLabel lblTitle = new JLabel("HOW TO PLAY");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setForeground(Color.CYAN);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        // 2. Rules Content
        JTextArea txtRules = new JTextArea();
        txtRules.setText(
            "1. OBJECTIVE:\n" +
            "   Find all matching pairs of cards on the board.\n\n" +
            "2. GAMEPLAY:\n" +
            "   - Click on a card to flip it over.\n" +
            "   - Select a second card to try and find a match.\n" +
            "   - If the cards match, they remain face up and you earn points.\n" +
            "   - If they don't match, they will flip back over after a short delay.\n\n" +
            "3. TURNS:\n" +
            "   - Making a match allows you to keep your turn.\n" +
            "   - Missing a match passes the turn to the next player (or Computer).\n\n" +
            "4. SCORING:\n" +
            "   - Each match is worth 10 points.\n" +
            "   - The player with the highest score at the end wins.\n\n" +
            "5. DIFFICULTY MODES (vs Computer):\n" +
            "   - Easy: Computer plays mostly randomly.\n" +
            "   - Normal: Computer has average memory.\n" +
            "   - Hard: Computer has excellent memory for cards it has seen (does not peek at hidden cards)."
        );
        
        // Styling the text area
        txtRules.setFont(new Font("Arial", Font.PLAIN, 16));
        txtRules.setForeground(Color.WHITE);
        txtRules.setBackground(new Color(60, 60, 60));
        txtRules.setEditable(false); // Read-only
        txtRules.setLineWrap(true);
        txtRules.setWrapStyleWord(true);
        txtRules.setMargin(new Insets(20, 20, 20, 20));

        // Add to a scroll pane in case text is too long for small screens
        JScrollPane scrollPane = new JScrollPane(txtRules);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        add(scrollPane, BorderLayout.CENTER);

        // 3. Back Button
        JButton btnBack = new JButton("Back to Menu");
        btnBack.setFont(new Font("Arial", Font.BOLD, 16));
        btnBack.addActionListener(e -> frame.showPanel("Menu"));
        
        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.add(btnBack);
        add(southPanel, BorderLayout.SOUTH);
    }
}