package view;

import javax.swing.*;
import java.awt.*;

/**
 * The configuration screen where players set up the game parameters.
 * Allows selection of Game Mode (PvP or vs Computer), Player Names, and Grid Size.
 */
public class ModeSelectionPanel extends JPanel {
    private MainFrame frame;
    private JTextField txtP1Name;
    private JTextField txtP2Name;
    private JRadioButton rbSinglePlayer;
    private JRadioButton rbMultiPlayer;
    private JComboBox<String> cbGridSize;
    private JComboBox<String> difficultyBox;

    /**
     * Initializes the configuration UI.
     * @param frame Reference to the main window controller.
     */
    public ModeSelectionPanel(MainFrame frame) {
        this.frame = frame;

        // Apply Cozy Pastel background
        setBackground(Theme.BG);
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 1. Title
        JLabel lblTitle = new JLabel("GAME SETUP");
        Theme.styleTitleLabel(lblTitle);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        // 2. Game Mode Selection
        JLabel lblMode = new JLabel("Select Mode:");
        Theme.styleHeadingLabel(lblMode);
        
        gbc.gridwidth = 1; gbc.gridy = 1;
        add(lblMode, gbc);

        // Radio Buttons
        rbSinglePlayer = new JRadioButton("Single Player (vs Computer)");
        rbMultiPlayer = new JRadioButton("Two Players (PvP)");
        
        // Style Radio Buttons
        rbSinglePlayer.setBackground(Theme.SURFACE);
        rbSinglePlayer.setForeground(Theme.TEXT);
        rbSinglePlayer.setFont(Theme.FONT_BODY);
        rbSinglePlayer.setSelected(true); // Default

        rbMultiPlayer.setBackground(Theme.SURFACE);
        rbMultiPlayer.setForeground(Theme.TEXT);
        rbMultiPlayer.setFont(Theme.FONT_BODY);

        // Group them so only one can be selected
        ButtonGroup group = new ButtonGroup();
        group.add(rbSinglePlayer);
        group.add(rbMultiPlayer);

        JPanel modePanel = new JPanel(new GridLayout(2, 1));
        modePanel.setOpaque(false);
        modePanel.add(rbSinglePlayer);
        modePanel.add(rbMultiPlayer);
        
        gbc.gridx = 1;
        add(modePanel, gbc);

        // 3. Player 1 Name
        JLabel lblP1 = new JLabel("Player 1 Name:");
        lblP1.setForeground(Color.WHITE);
        lblP1.setFont(new Font("Arial", Font.PLAIN, 16));
        
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblP1, gbc);

        txtP1Name = new JTextField("Player 1", 15);
        txtP1Name.setFont(Theme.FONT_BODY);
        gbc.gridx = 1;
        add(txtP1Name, gbc);

        // 4. Player 2 Name
        JLabel lblP2 = new JLabel("Player 2 Name:");
        lblP2.setForeground(Color.WHITE);
        lblP2.setFont(new Font("Arial", Font.PLAIN, 16));
        
        gbc.gridx = 0; gbc.gridy = 3;
        add(lblP2, gbc);

        txtP2Name = new JTextField("Computer AI", 15);
        txtP2Name.setEnabled(false); // Disabled by default for Single Player
        txtP2Name.setFont(Theme.FONT_BODY);
        gbc.gridx = 1;
        add(txtP2Name, gbc);

        // Prepare AI difficulty selector (kept off-layout for now; will be added below Start button)
        String[] levelOptions = {"Easy", "Normal", "Hard"};
        difficultyBox = new JComboBox<>(levelOptions);
        difficultyBox.setFont(new Font("Arial", Font.PLAIN, 14));
        difficultyBox.setEnabled(rbSinglePlayer.isSelected());

        // Logic to toggle P2 text field and difficulty control based on mode
        rbSinglePlayer.addActionListener(e -> {
            txtP2Name.setText("Computer AI");
            txtP2Name.setEnabled(false);
            if (difficultyBox != null) difficultyBox.setEnabled(true);
        });

        rbMultiPlayer.addActionListener(e -> {
            txtP2Name.setText("Player 2");
            txtP2Name.setEnabled(true);
            if (difficultyBox != null) difficultyBox.setEnabled(false);
        });

        // 5. Grid Size Selection
        JLabel lblGrid = new JLabel("Grid Size:");
        lblGrid.setForeground(Color.WHITE);
        lblGrid.setFont(new Font("Arial", Font.PLAIN, 16));
        
        gbc.gridx = 0; gbc.gridy = 4;
        add(lblGrid, gbc);

        String[] sizes = {"4x4 (Easy)", "6x6 (Medium)", "8x8 (Hard)", "10x10 (Extreme)"};
        cbGridSize = new JComboBox<>(sizes);
        Theme.styleComboBox(cbGridSize);
        gbc.gridx = 1;
        add(cbGridSize, gbc);

        // 6. Action Buttons
        JButton btnStart = new JButton("START GAME");
        btnStart.setBackground(new Color(50, 200, 50));
        btnStart.setForeground(Color.BLACK);
        Theme.styleButton(btnStart, true);
        
        btnStart.addActionListener(e -> startGame());
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(btnStart, gbc);

        // 7. AI Difficulty (visible only when Single Player is selected)
        JLabel lblDiff = new JLabel("AI Difficulty:");
        Theme.styleHeadingLabel(lblDiff);
        gbc.gridwidth = 1; gbc.gridy = 6; gbc.gridx = 0; gbc.insets = new Insets(10, 10, 10, 10);
        add(lblDiff, gbc);

        Theme.styleComboBox(difficultyBox);
        gbc.gridx = 1; gbc.gridy = 6;
        add(difficultyBox, gbc);

        JButton btnBack = new JButton("Back to Menu");
        btnBack.addActionListener(e -> frame.showPanel("Menu"));
        
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(btnBack, gbc);
    }

    /**
     * Validates input and triggers the game initialization in MainFrame.
     */
    private void startGame() {
        String p1 = txtP1Name.getText().trim();
        String p2 = txtP2Name.getText().trim();
        boolean isPvP = rbMultiPlayer.isSelected();

        if (p1.isEmpty() || p2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter names for all players!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Parse grid size from the combobox string (e.g., "4x4 (Easy)" -> 4)
        String selectedSize = (String) cbGridSize.getSelectedItem();
        int gridSize = 4; // Default
        if (selectedSize.startsWith("4")) gridSize = 4;
        else if (selectedSize.startsWith("6")) gridSize = 6;
        else if (selectedSize.startsWith("8")) gridSize = 8;
        else if (selectedSize.startsWith("10")) gridSize = 10;

        // Launch the game
        String selectedDifficulty = (String) difficultyBox.getSelectedItem();
        frame.initializeGame(isPvP, p1, p2, gridSize, selectedDifficulty);
    }
}