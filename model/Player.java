package model;

/**
 * Abstract base class representing a participant in the game.
 * It holds common data like the player's name and current score.
 * Specific turn logic is defined in subclasses (e.g., ComputerPlayer).
 */
public abstract class Player {
    private String name;
    private int score;

    /**
     * Initializes a new player with a specific name.
     * Score starts at 0.
     * @param name The display name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    /**
     * Increases the player's score by a specified amount.
     * @param points Points to add (usually 10 for a match).
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Resets the score to zero. Useful for restarting the game.
     */
    public void resetScore() {
        this.score = 0;
    }

    /**
     * Abstract method that defines how a player takes their turn.
     * - For human players, this might be handled via UI events.
     * - For the computer, this contains the AI logic.
     */
    public abstract void playTurn();

    // --- Getters and Setters ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }
}