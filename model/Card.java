package model;

import javax.swing.ImageIcon;

/**
 * Represents a single playing card in the game.
 * Stores the card's identity (rank/suit), its visual image, 
 * and its current state (face up/down, matched).
 */
public class Card {
    private String value;      // e.g., "1", "2", "K"
    private String suit;       // e.g., "c", "d", "h", "s"
    private ImageIcon frontImage;
    
    // State variables
    private boolean faceUp;    // true if the card image is visible
    private boolean matched;   // true if the card has been paired successfully

    /**
     * Creates a new card with the specified properties.
     * Initially, the card is face down and unmatched.
     * * @param value The rank of the card.
     * @param suit The suit of the card.
     * @param frontImage The image to display when face up.
     */
    public Card(String value, String suit, ImageIcon frontImage) {
        this.value = value;
        this.suit = suit;
        this.frontImage = frontImage;
        this.faceUp = false; 
        this.matched = false;
    }

    // --- Getters and Setters ---

    public String getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    public ImageIcon getFrontImage() {
        return frontImage;
    }

    /**
     * Checks if the card is currently facing up (visible).
     * Used by the UI to determine which image to render.
     */
    public boolean isFaceUp() {
        return faceUp;
    }

    /**
     * Sets the visibility state of the card.
     * @param faceUp true to show the front image, false to show the back.
     */
    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }

    /**
     * Checks if the card has been successfully matched with its pair.
     */
    public boolean isMatched() {
        return matched;
    }

    /**
     * Marks the card as matched, permanently disabling it in the game.
     * @param matched true if the card is part of a found pair.
     */
    public void setMatched(boolean matched) {
        this.matched = matched;
    }
}