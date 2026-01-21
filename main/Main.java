package main;

import javax.swing.SwingUtilities;
import view.MainFrame;

/**
 * The entry point for the application.
 * It launches the GUI within the Swing Event Dispatch Thread (EDT) to ensure thread safety.
 */
public class Main {
    public static void main(String[] args) {
        // Use invokeLater to ensure thread safety for Swing components
        SwingUtilities.invokeLater(() -> {
            try {
                // Create and display the main application window
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                // Print stack trace if something goes wrong during initialization
                e.printStackTrace();
            }
        });
    }
}