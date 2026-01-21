package view;

import controller.GameEngine;
import model.Card;
import model.ComputerPlayer; 
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The main game board panel.
 * Handles grid layout, user interactions (clicks), and coordinates the AI's turn.
 */
public class GamePanel extends JPanel {
    private GameEngine engine;
    private MainFrame frame;
    private int gridSize;
    private List<CardButton> cardButtons;
    private JLabel lblStatus;

    /**
     * Initializes the game board.
     * @param engine The logic controller.
     * @param frame The main window.
     * @param gridSize The size of the grid (e.g., 4 for 4x4).
     */
    public GamePanel(GameEngine engine, MainFrame frame, int gridSize) {
        this.engine = engine;
        this.frame = frame;
        this.gridSize = gridSize;
        this.cardButtons = new ArrayList<>();

        // Apply Cozy Pastel surface background
        setBackground(Theme.SURFACE);
        setLayout(new BorderLayout(10, 10));

        // 1. Status Panel (Top)
        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        infoPanel.setOpaque(false);
        
        lblStatus = new JLabel("Turn: " + engine.getCurrentPlayer().getName(), SwingConstants.CENTER);
        lblStatus.setForeground(Theme.TEXT);
        lblStatus.setFont(Theme.FONT_HEADING);
        infoPanel.add(lblStatus);
        
        add(infoPanel, BorderLayout.NORTH);

        // 2. Card Grid (Center)
        JPanel board = new JPanel(new GridLayout(gridSize, gridSize, 5, 5));
        board.setOpaque(false);
        board.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Calculate card size dynamically based on screen space
        Dimension cardDim = calculateCardSize(gridSize);

        for (Card card : engine.getCards()) {
            CardButton btn = new CardButton(card, cardDim);
            btn.addActionListener(e -> handleCardClick(btn));
            cardButtons.add(btn);
            board.add(btn);
        }
        add(board, BorderLayout.CENTER);

        // 3. Control Panel (Bottom)
        JButton btnQuit = new JButton("Quit Game");
        Theme.styleSmallButton(btnQuit, false);
        btnQuit.addActionListener(e -> frame.showPanel("Menu"));
        
        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.add(btnQuit);
        add(southPanel, BorderLayout.SOUTH);

        // Check if Computer starts first (rare case, but handled)
        if (engine.getCurrentPlayer() instanceof ComputerPlayer) {
            startComputerTurn();
        }
    }

    /**
     * Calculates appropriate card dimensions to fit the window.
     */
    private Dimension calculateCardSize(int size) {
        int availableWidth = 1100;
        int availableHeight = 700;
        int w = (availableWidth / size) - 10;
        int h = (availableHeight / size) - 10;
        
        // Maintain aspect ratio
        int cardW = Math.min(w, (int)(h * 0.75));
        int cardH = (int)(cardW / 0.75);
        return new Dimension(cardW, cardH);
    }

    /**
     * Handles the logic when a user (or AI) clicks a card.
     */
    private void handleCardClick(CardButton btn) {
        // Validate: Ignore if processing animation, card is up, or matched
        if (engine.isProcessing() || btn.getCard().isFaceUp() || btn.getCard().isMatched()) return;

        // --- AI MEMORY UPDATE ---
        // If playing against AI, show this card to the AI so it can remember it
        if (engine.getP2() instanceof ComputerPlayer) {
            ((ComputerPlayer) engine.getP2()).memorizeCard(btn.getCard());
        }
        // ------------------------

        // Process the selection in the engine
        boolean isMatch = engine.handleCardSelection(btn.getCard());
        refreshUI();

        // Scenario 1: No Match (Wait and Switch Turn)
        if (!isMatch && engine.getSecondSelectedCard() != null) {
            Timer timer = new Timer(1000, e -> {
                engine.switchTurn();
                refreshUI();
                
                // If turn passed to Computer, start AI logic
                if (engine.getCurrentPlayer() instanceof ComputerPlayer) {
                    startComputerTurn();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
        // Scenario 2: Match Found (Current player continues)
        else if (isMatch) {
             // Clean AI memory regarding matched cards
             if (engine.getP2() instanceof ComputerPlayer) {
                ((ComputerPlayer) engine.getP2()).forgetMatchedCards();
             }

             // If it was the AI's turn, it plays again
             if (engine.getCurrentPlayer() instanceof ComputerPlayer) {
                 Timer timer = new Timer(1000, e -> startComputerTurn());
                 timer.setRepeats(false);
                 timer.start();
             }
        }

        // Check Game Over
        if (engine.isGameOver()) {
            frame.triggerGameOver(engine.getWinner().getName(), 
                                engine.getP1().getScore(), 
                                engine.getP2().getScore());
        }
    }

    /**
     * Refreshes the UI state (card images and status text).
     */
    public void refreshUI() {
        for (CardButton btn : cardButtons) {
            btn.updateDisplay();
        }
        lblStatus.setText("Turn: " + engine.getCurrentPlayer().getName() + 
                         " (Score: " + engine.getCurrentPlayer().getScore() + ")");
    }

    /**
     * Orchestrates the Computer's turn with visual delays.
     */
    private void startComputerTurn() {
        if (engine.isGameOver() || !(engine.getCurrentPlayer() instanceof ComputerPlayer)) {
            return;
        }

        ComputerPlayer ai = (ComputerPlayer) engine.getCurrentPlayer();

        // Delay 1: "Thinking" time before first move
        Timer t1 = new Timer(1000, e1 -> {
            // Ask AI for the best first move
            CardButton btn1 = ai.makeMove(cardButtons, null);
            
            if (btn1 != null) {
                handleCardClick(btn1); 

                // Delay 2: Time before second move
                Timer t2 = new Timer(1000, e2 -> {
                    // Ask AI for the best second move (knowing the first card)
                    CardButton btn2 = ai.makeMove(cardButtons, btn1.getCard());
                    
                    if (btn2 != null) {
                        handleCardClick(btn2); 
                    }
                });
                t2.setRepeats(false);
                t2.start();
            }
        });
        t1.setRepeats(false);
        t1.start();
    }
}