package controller;

import model.Card;
import model.Player;
import java.util.List;

/**
 * Manages the core game logic, including turn management, 
 * card matching validation, and score updates.
 */
public class GameEngine {
    private Player p1;
    private Player p2;
    private Player currentPlayer;
    private List<Card> cards; // The deck of cards
    private boolean isPvP;
    
    // State variables for the current turn
    private Card firstSelectedCard;
    private Card secondSelectedCard;
    private boolean isProcessing; // Locks input during animations

    public GameEngine(Player p1, Player p2, List<Card> cards, boolean isPvP) {
        this.p1 = p1;
        this.p2 = p2;
        this.cards = cards;
        this.isPvP = isPvP;
        
        // Player 1 always starts the game
        this.currentPlayer = p1;
        this.isProcessing = false;
    }

    /**
     * Handles the logic when a card is clicked.
     * @param card The card that was clicked.
     * @return true if a match was found, false otherwise.
     */
    public boolean handleCardSelection(Card card) {
        // Validation: Ignore clicks if processing, or if card is already revealed/matched
        if (isProcessing || card.isFaceUp() || card.isMatched()) {
            return false;
        }

        // Reveal the card
        card.setFaceUp(true);

        // Case 1: First card selection
        if (firstSelectedCard == null) {
            firstSelectedCard = card;
            return false; // No match possible yet, waiting for second card
        } 
        // Case 2: Second card selection
        else {
            secondSelectedCard = card;
            isProcessing = true; // Lock input to prevent cheating

            // Check if the two selected cards match
            if (checkMatch(firstSelectedCard, secondSelectedCard)) {
                // Match found
                firstSelectedCard.setMatched(true);
                secondSelectedCard.setMatched(true);
                
                // Update score for the current player
                currentPlayer.addScore(10);
                
                // Reset selections for the next move (Player keeps turn)
                resetSelections();
                return true;
            } else {
                // No match found
                // The UI will handle the delay and turn switch
                return false;
            }
        }
    }

    /**
     * Helper method to compare two cards based on value and suit.
     */
    private boolean checkMatch(Card c1, Card c2) {
        return c1.getValue().equals(c2.getValue()) && c1.getSuit().equals(c2.getSuit());
    }

    /**
     * Switches the active player and hides unmatched cards.
     * Called by the UI after a delay when no match is found.
     */
    public void switchTurn() {
        // Flip cards back down if they were not matched
        if (firstSelectedCard != null && !firstSelectedCard.isMatched()) {
            firstSelectedCard.setFaceUp(false);
        }
        if (secondSelectedCard != null && !secondSelectedCard.isMatched()) {
            secondSelectedCard.setFaceUp(false);
        }

        // Clear selection state
        firstSelectedCard = null;
        secondSelectedCard = null;
        isProcessing = false;

        // Toggle player turn
        if (currentPlayer == p1) {
            currentPlayer = p2;
        } else {
            currentPlayer = p1;
        }
    }

    /**
     * Resets the temporary selection variables.
     * Used when a match is successfully found.
     */
    private void resetSelections() {
        firstSelectedCard = null;
        secondSelectedCard = null;
        isProcessing = false;
    }

    /**
     * Checks if all cards on the board have been matched.
     */
    public boolean isGameOver() {
        for (Card c : cards) {
            if (!c.isMatched()) {
                return false; // At least one card is still in play
            }
        }
        return true; // All cards matched
    }

    /**
     * Determines the winner based on the final score.
     */
    public Player getWinner() {
        if (p1.getScore() > p2.getScore()) return p1;
        if (p2.getScore() > p1.getScore()) return p2;
        
        // Handle draw scenario
        return new Player("Draw") { 
            @Override public void playTurn() {} 
        }; 
    }

    // --- Getters for UI access ---

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Card> getCards() {
        return cards;
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    public Card getSecondSelectedCard() {
        return secondSelectedCard;
    }
    
    public Player getP1() { return p1; }
    public Player getP2() { return p2; }
}