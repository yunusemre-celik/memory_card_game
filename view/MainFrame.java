package view;

import controller.GameEngine;
import model.*;
import util.ScoreManager; 
import javax.swing.*;
import java.awt.*;

/**
 * The main window controller that acts as the central hub of the application.
 * Manages navigation between different screens (Menu, Game, Settings, etc.)
 * and handles the initialization of new game sessions.
 */
public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    
    // References to the various screens of the application
    private MenuPanel menuPanel;
    private ModeSelectionPanel modeSelectionPanel;
    private RulesPanel rulesPanel;
    private SettingsPanel settingsPanel;
    private GameOverPanel gameOverPanel;
    private HighScoresPanel highScoresPanel;

    public MainFrame() {
        // Window configuration
        setTitle("Memory Match & Collect Game");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Use CardLayout to switch between views
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // Initialize all interface panels
        // IMPORTANT: settingsPanel must be initialized here so we can access it later
        menuPanel = new MenuPanel(this);
        modeSelectionPanel = new ModeSelectionPanel(this);
        rulesPanel = new RulesPanel(this);
        settingsPanel = new SettingsPanel(this);
        gameOverPanel = new GameOverPanel(this);
        highScoresPanel = new HighScoresPanel(this);

        // Add panels to the container with unique identifiers
        mainContainer.add(menuPanel, "Menu");
        mainContainer.add(modeSelectionPanel, "ModeSelection");
        mainContainer.add(rulesPanel, "Rules");
        mainContainer.add(settingsPanel, "Settings");
        mainContainer.add(gameOverPanel, "GameOver");
        mainContainer.add(highScoresPanel, "HighScores");

        add(mainContainer);
        
        // Apply theme defaults to the window
        Theme.applyWindowDefaults(this);

        // Start showing the main menu
        showPanel("Menu");
    }

    /**
     * Switches the current view to the specified panel.
     * Also handles data refreshing for specific panels like High Scores.
     * @param panelName The string identifier of the panel to show.
     */
    public void showPanel(String panelName) {
        if (panelName.equals("HighScores")) {
            highScoresPanel.refreshScores();
        }
        cardLayout.show(mainContainer, panelName);
        mainContainer.requestFocusInWindow();
    }

    /**
     * Helper method to open the game mode selection screen.
     */
    public void openModeSelection() {
        showPanel("ModeSelection");
    }

    /**
     * Apply a theme at runtime and refresh visible UI components.
     */
    public void applyTheme(Theme.ThemeName theme) {
        Theme.setTheme(theme);
        Theme.applyWindowDefaults(this);
        Theme.updateComponentTree(this.getContentPane());
        this.revalidate();
        this.repaint();
    }

    /**
     * Sets up and starts a new game based on user configuration.
     * @param isPvP True for 2-player mode, False for Player vs Computer.
     * @param p1Name Name of the first player.
     * @param p2Name Name of the second player (or ignored if AI).
     * @param gridSize The dimension of the board (e.g., 4 for 4x4).
     */
    public void initializeGame(boolean isPvP, String p1Name, String p2Name, int gridSize, String difficulty) {
        System.out.println("Initializing Game..."); // Debug print

        // 1. Prepare the Deck
        int pairsNeeded = (gridSize * gridSize) / 2;
        
        // Card values corresponding to file names (1.jpg ... 13.jpg)
        String[] values = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        String[] suits = {"c", "d", "h", "s"};
        
        Deck deck = new Deck();
        // Load images from the resources folder
        deck.initializeDeck(values, suits, "resources/images/", pairsNeeded);

        // 2. Configure Players
        // Using anonymous subclass for human players to implement abstract methods
        Player p1 = new Player(p1Name) { @Override public void playTurn() {} };
        Player p2;

        if (isPvP) {
            // Multiplayer setup
            p2 = new Player(p2Name) { @Override public void playTurn() {} };
        } else {
            // Single Player setup (vs Computer)
            ComputerPlayer ai = new ComputerPlayer();
            
            // Use difficulty passed from ModeSelectionPanel with fallback
            String diff = (difficulty == null || difficulty.isEmpty()) ? "Easy" : difficulty;
            System.out.println("Setting AI Difficulty to: " + diff);
            ai.setDifficulty(diff);
            
            p2 = ai;
        }

        // 3. Initialize Game Logic Engine
        GameEngine engine = new GameEngine(p1, p2, deck.getCards(), isPvP);

        // 4. Create Game Board UI
        // We create a new GamePanel instance for every new game to reset state
        GamePanel gamePanel = new GamePanel(engine, this, gridSize);
        
        // Add it to the layout (replacing any old game instance if necessary)
        // Note: CardLayout handles duplicate names by keeping the latest one usually, 
        // but removing the old one first is cleaner if possible. Swing handles this ok.
        mainContainer.add(gamePanel, "Game");
        
        // Switch view to the game
        showPanel("Game");
    }

    /**
     * Handles the end-of-game sequence.
     * Saves scores and displays the results screen.
     */
    public void triggerGameOver(String winnerName, int p1Score, int p2Score) {
        // Calculate the winning high score
        int winningScore = Math.max(p1Score, p2Score);

        // Save score only if it's a valid human win
        // We don't save "Draw" or if the Computer won
        if (!winnerName.equals("Draw") && !winnerName.equals("Computer AI")) {
            ScoreManager.saveScore(winnerName, winningScore);
        }

        // Update the Game Over screen with details
        gameOverPanel.setResults(winnerName, p1Score, p2Score);
        showPanel("GameOver");
    }
}