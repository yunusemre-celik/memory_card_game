package view;

import util.ScoreManager;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Displays the leaderboard showing the top 10 highest scores.
 * Fetches data from the ScoreManager and renders it in a styled table.
 */
public class HighScoresPanel extends JPanel {
    private MainFrame frame;
    private JTable scoreTable;
    private DefaultTableModel tableModel;

    /**
     * Initializes the High Scores UI layout.
     * @param frame Reference to the main window for navigation.
     */
    public HighScoresPanel(MainFrame frame) {
        this.frame = frame;

        // Setup basic layout and dark theme
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(45, 45, 45));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // 1. Title Label
        JLabel lblTitle = new JLabel("TOP 10 HIGH SCORES");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setForeground(Color.ORANGE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        // 2. Table Setup
        String[] columnNames = {"Rank", "Player Name", "Score"};
        
        // Make cells non-editable
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        scoreTable = new JTable(tableModel);
        scoreTable.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreTable.setRowHeight(30);
        scoreTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        scoreTable.setFillsViewportHeight(true);
        
        // Customizing Table Colors to match Dark Theme
        scoreTable.setBackground(new Color(60, 60, 60));
        scoreTable.setForeground(Color.WHITE);
        scoreTable.getTableHeader().setBackground(new Color(30, 30, 30));
        scoreTable.getTableHeader().setForeground(Color.WHITE);
        scoreTable.setGridColor(new Color(100, 100, 100));

        // Center align text in all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < scoreTable.getColumnCount(); i++) {
            scoreTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // ScrollPane for the table
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.getViewport().setBackground(new Color(45, 45, 45));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        add(scrollPane, BorderLayout.CENTER);

        // 3. Back Button
        JButton btnBack = new JButton("Back to Menu");
        btnBack.setFont(new Font("Arial", Font.BOLD, 16));
        btnBack.addActionListener(e -> frame.showPanel("Menu"));
        
        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.add(btnBack);
        add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Refreshes the table data from the file.
     * This is called every time the user navigates to this panel.
     */
    public void refreshScores() {
        tableModel.setRowCount(0); // Clear existing data
        List<String[]> scores = ScoreManager.getTopScores();
        
        int rank = 1;
        for (String[] entry : scores) {
            // entry[0] is Name, entry[1] is Score
            tableModel.addRow(new Object[]{rank++, entry[0], entry[1]});
        }
    }
}