package model;

import view.CardButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the computer opponent.
 * Implements logic for memory retention and decision making based on difficulty levels.
 */
public class ComputerPlayer extends Player {
    private String difficulty; // "Easy", "Normal", "Hard"
    private List<Card> memory;
    private Random random;

    public ComputerPlayer() {
        super("Computer AI");
        this.difficulty = "Easy"; // Default setting
        this.memory = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Updates the difficulty level based on settings selection.
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Decides whether to remember a revealed card based on the current difficulty.
     */
    public void memorizeCard(Card card) {
        // Don't memorize if already in memory or already matched
        if (memory.contains(card) || card.isMatched()) return;

        boolean shouldRemember = false;

        switch (difficulty) {
            case "Easy":
                // 10% chance to remember (Mostly random behavior)
                shouldRemember = random.nextDouble() < 0.1;
                break;
            case "Normal":
                // 50% chance to remember
                shouldRemember = random.nextDouble() < 0.5;
                break;
            case "Hard":
                // 100% chance to remember (Remembers everything it has seen)
                shouldRemember = true;
                break;
            default:
                shouldRemember = false;
        }

        if (shouldRemember) {
            memory.add(card);
        }
    }

    /**
     * Cleans up the memory by removing cards that have been matched/removed from the board.
     */
    public void forgetMatchedCards() {
        memory.removeIf(Card::isMatched);
    }

    /**
     * Main AI Logic: Selects the best possible move.
     * @param availableButtons List of all card buttons on the board.
     * @param firstCard The first card selected in the current turn (null if this is the first move).
     * @return The CardButton to click.
     */
    public CardButton makeMove(List<CardButton> availableButtons, Card firstCard) {
        // SCENARIO 1: Second move of the turn (We need to find a match for firstCard)
        if (firstCard != null) {
            for (CardButton btn : availableButtons) {
                Card c = btn.getCard();
                
                // Check if this card is the pair for the first card
                // Must ensure we don't click the exact same card instance again
                if (c != firstCard && c.getValue().equals(firstCard.getValue()) && c.getSuit().equals(firstCard.getSuit())) {
                    // We pick it if it's in our memory OR if we are on Hard mode (always finds pairs)
                    if (memory.contains(c)) {
                        return btn;
                    }
                }
            }
        } 
        // SCENARIO 2: First move of the turn (Try to find a known pair in memory)
        else {
            for (int i = 0; i < memory.size(); i++) {
                for (int j = i + 1; j < memory.size(); j++) {
                    Card c1 = memory.get(i);
                    Card c2 = memory.get(j);
                    
                    // If we have two matching cards in memory
                    if (c1.getValue().equals(c2.getValue()) && c1.getSuit().equals(c2.getSuit())) {
                        // Find the button associated with the first card of the pair
                        for (CardButton btn : availableButtons) {
                            // CRITICAL: Using isFaceUp() as requested
                            if (btn.getCard() == c1 && !btn.getCard().isMatched() && !btn.getCard().isFaceUp()) {
                                return btn;
                            }
                        }
                    }
                }
            }
        }

        // SCENARIO 3: Fallback (Random Move)
        // If no intelligent move is possible, pick a random valid card
        List<CardButton> validOptions = new ArrayList<>();
        for (CardButton btn : availableButtons) {
            // CRITICAL: Using isFaceUp() as requested
            if (!btn.getCard().isMatched() && !btn.getCard().isFaceUp() && btn.getCard() != firstCard) {
                validOptions.add(btn);
            }
        }

        if (validOptions.isEmpty()) return null;
        return validOptions.get(random.nextInt(validOptions.size()));
    }

    @Override
    public void playTurn() {
        // Logic is handled via makeMove called by the GamePanel
    }
}