package view;

import javax.swing.*;
import java.awt.*;

/**
 * The screen displayed when the game finishes.
 * Shows the winner, final scores, and provides navigation back to the menu.
 */
public class GameOverPanel extends JPanel {
    private MainFrame frame;
    private JLabel lblWinner;
    private JLabel lblScore1;
    private JLabel lblScore2;

    /**
     * Initializes the Game Over UI layout.
     * @param frame Reference to the main window for navigation.
     */
    public GameOverPanel(MainFrame frame) {
        this.frame = frame;

        // Dark theme background consistent with the rest of the app
        setBackground(new Color(45, 45, 45));
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Title Label
        JLabel lblTitle = new JLabel("GAME OVER");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 40));
        lblTitle.setForeground(Color.RED);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 0;
        add(lblTitle, gbc);

        // 2. Winner Label
        lblWinner = new JLabel("Winner: ???");
        lblWinner.setFont(new Font("Arial", Font.BOLD, 24));
        lblWinner.setForeground(Color.GREEN);
        lblWinner.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 1;
        add(lblWinner, gbc);

        // 3. Score Labels
        lblScore1 = new JLabel("Player 1 Score: 0");
        lblScore1.setFont(new Font("Arial", Font.PLAIN, 18));
        lblScore1.setForeground(Color.WHITE);
        lblScore1.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 2;
        add(lblScore1, gbc);

        lblScore2 = new JLabel("Player 2 Score: 0");
        lblScore2.setFont(new Font("Arial", Font.PLAIN, 18));
        lblScore2.setForeground(Color.WHITE);
        lblScore2.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 3;
        add(lblScore2, gbc);

        // 4. Return to Menu Button
        JButton btnMenu = new JButton("Return to Main Menu");
        btnMenu.setFont(new Font("Arial", Font.BOLD, 16));
        btnMenu.setFocusPainted(false);
        btnMenu.addActionListener(e -> frame.showPanel("Menu"));

        gbc.gridy = 4;
        gbc.insets = new Insets(30, 10, 10, 10); // Add extra space above button
        add(btnMenu, gbc);
    }

    /**
     * Updates the text labels with the final game results.
     * Called by MainFrame when the game ends.
     * * @param winnerName The name of the winning player (or "Draw").
     * @param p1Score Final score of Player 1.
     * @param p2Score Final score of Player 2 (or Computer).
     */
    public void setResults(String winnerName, int p1Score, int p2Score) {
        if (winnerName.equals("Draw")) {
            lblWinner.setText("It's a Draw!");
            lblWinner.setForeground(Color.YELLOW);
        } else {
            lblWinner.setText("Winner: " + winnerName + "!");
            lblWinner.setForeground(Color.GREEN);
        }

        lblScore1.setText("Player 1 Score: " + p1Score);
        lblScore2.setText("Player 2 Score: " + p2Score);
    }
}