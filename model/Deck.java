package model;

import javax.swing.ImageIcon;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the deck of cards, including initialization, shuffling, 
 * and selecting the specific subset of cards needed for the current game grid.
 */
public class Deck {
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Initializes the deck with the required number of pairs.
     * Uses a hybrid loading approach to support both IDE and JAR execution.
     * * @param values Array of rank values (e.g., "1", "13").
     * @param suits Array of suit codes (e.g., "c", "h").
     * @param imagePath Base path for images (e.g., "resources/images/").
     * @param pairsNeeded Number of unique pairs required for the grid size.
     */
    public void initializeDeck(String[] values, String[] suits, String imagePath, int pairsNeeded) {
        this.cards.clear();
        List<Card> prototypeDeck = new ArrayList<>();

        // 1. Generate the master deck (52 cards)
        for (String suit : suits) {
            for (String value : values) {
                // Construct the file path (e.g., "resources/images/1c.jpg")
                String fileName = imagePath + value + suit + ".jpg";
                
                // Load the image securely
                ImageIcon icon = loadCardImage(fileName);
                
                // Create the card object and add to prototype list
                prototypeDeck.add(new Card(value, suit, icon));
            }
        }

        // 2. Shuffle to randomize selection
        Collections.shuffle(prototypeDeck);

        // 3. Select the required number of pairs for the game
        List<Card> selectedCards = new ArrayList<>();
        for (int i = 0; i < pairsNeeded; i++) {
            // Pick a card from the prototype deck
            // Use modulo to cycle through if pairsNeeded > 52 (rare case)
            Card proto = prototypeDeck.get(i % prototypeDeck.size());
            
            // Create two identical instances for the pair
            selectedCards.add(new Card(proto.getValue(), proto.getSuit(), proto.getFrontImage()));
            selectedCards.add(new Card(proto.getValue(), proto.getSuit(), proto.getFrontImage()));
        }

        // 4. Shuffle the final game deck so pairs are scattered
        Collections.shuffle(selectedCards);
        this.cards = selectedCards;
    }

    /**
     * Helper method to load images.
     * Tries to load from the classpath (for JARs) first, then falls back to file system.
     * * @param path Relative path to the image file.
     * @return Loaded ImageIcon, or a blank icon if not found (avoids crashes).
     */
    private ImageIcon loadCardImage(String path) {
        // Attempt 1: Load from Classpath (Standard for JAR distribution)
        URL imgURL = getClass().getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        }
        
        // Attempt 2: Load from File System (Standard for IDE development)
        return new ImageIcon(path);
    }

    /**
     * Returns the finalized list of cards for the current game.
     */
    public List<Card> getCards() {
        return cards;
    }
}