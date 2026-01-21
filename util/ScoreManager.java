package util;

import java.io.*;
import java.util.*;

/**
 * Utility class to handle reading and writing high scores to a text file.
 * Keeps track of the top 10 players and ensures data persistence.
 */
public class ScoreManager {
    // The file where scores are stored locally
    private static final String FILE_PATH = "highscores.txt";

    /**
     * Saves a new score to the high scores file.
     * The method loads existing scores, adds the new one, sorts them,
     * trims the list to the top 10, and saves everything back to the file.
     * * @param name The player's name.
     * @param score The score achieved.
     */
    public static void saveScore(String name, int score) {
        List<ScoreEntry> scores = loadScores();
        
        // Add the new score
        scores.add(new ScoreEntry(name, score));
        
        // Sort by score in descending order (Highest first)
        scores.sort((s1, s2) -> Integer.compare(s2.score, s1.score));
        
        // Keep only the top 10 scores
        if (scores.size() > 10) {
            scores = scores.subList(0, 10);
        }
        
        // Save the updated list back to disk
        writeScores(scores);
    }

    /**
     * Retrieves the top scores formatted for the UI.
     * Used by HighScoresPanel to populate the table.
     * * @return A list of String arrays, where index 0 is Name and index 1 is Score.
     */
    public static List<String[]> getTopScores() {
        List<ScoreEntry> scores = loadScores();
        List<String[]> formattedList = new ArrayList<>();
        
        // Convert internal objects to String arrays for JTable compatibility
        for (ScoreEntry entry : scores) {
            formattedList.add(new String[]{entry.name, String.valueOf(entry.score)});
        }
        return formattedList;
    }

    // --- Private Helper Methods ---

    /**
     * Reads the file and parses lines into ScoreEntry objects.
     */
    private static List<ScoreEntry> loadScores() {
        List<ScoreEntry> scores = new ArrayList<>();
        File file = new File(FILE_PATH);

        // If file doesn't exist yet, return an empty list
        if (!file.exists()) {
            return scores;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by semicolon (Name;Score)
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    scores.add(new ScoreEntry(name, score));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        
        return scores;
    }

    /**
     * Writes the list of score objects to the file.
     */
    private static void writeScores(List<ScoreEntry> scores) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (ScoreEntry entry : scores) {
                writer.write(entry.name + ";" + entry.score);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Internal helper class to represent a single score record.
     */
    private static class ScoreEntry {
        String name;
        int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}