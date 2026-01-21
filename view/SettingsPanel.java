package view;

import javax.swing.*;
import java.awt.*;

/**
 * The configuration screen where users can adjust game preferences.
 * Currently supports changing the AI difficulty level.
 */
public class SettingsPanel extends JPanel {
    private MainFrame frame;
    private JCheckBox animationCheck;

    /**
     * Initializes the Settings UI.
     * @param frame Reference to the main window controller.
     */
    public SettingsPanel(MainFrame frame) {
        this.frame = frame;
        
        // Apply Cozy Pastel background
        setBackground(Theme.BG);
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Padding around elements
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Title
        JLabel lblTitle = new JLabel("SETTINGS");
        Theme.styleTitleLabel(lblTitle);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        // 3. Animation Toggle (Improved clarity)
        JLabel lblAnim = new JLabel("Card Animations:");
        Theme.styleHeadingLabel(lblAnim);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(lblAnim, gbc);

        // Use a descriptive checkbox label and tooltip for clarity
        animationCheck = new JCheckBox("Enable Card Flip Animations");
        animationCheck.setFont(Theme.FONT_BODY);
        animationCheck.setBackground(Theme.SURFACE);
        animationCheck.setForeground(Theme.TEXT);
        animationCheck.setToolTipText("When enabled, cards flip with a short animation during play; disable to improve performance.");
        // initialize from Settings
        animationCheck.setSelected(util.Settings.isAnimationsEnabled());
        animationCheck.addActionListener(e -> util.Settings.setAnimationsEnabled(animationCheck.isSelected()));
        
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(animationCheck, gbc);

        // Helpful description under the control
        JLabel lblDesc = new JLabel("Toggle flip animations for cards. Disable on slower machines.");
        lblDesc.setFont(Theme.FONT_BODY.deriveFont(Font.ITALIC, 12));
        lblDesc.setForeground(Theme.MUTED);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(lblDesc, gbc);

        // 4. Theme Selector
        JLabel lblTheme = new JLabel("Theme:");
        Theme.styleHeadingLabel(lblTheme);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST; gbc.insets = new Insets(10, 15, 10, 15);
        add(lblTheme, gbc);

        String[] themes = {"Cozy Pastel", "Cyberpunk / Synthwave", "Sapphire Glass"};
        JComboBox<String> themeBox = new JComboBox<>(themes);
        Theme.styleComboBox(themeBox);
        themeBox.setSelectedIndex(0);
        themeBox.addActionListener(e -> {
            int idx = themeBox.getSelectedIndex();
            Theme.ThemeName selected = Theme.ThemeName.COZY;
            if (idx == 1) selected = Theme.ThemeName.CYBERPUNK;
            else if (idx == 2) selected = Theme.ThemeName.SAPPHIRE;
            frame.applyTheme(selected);
        });
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
        add(themeBox, gbc);

        // 5. Save & Back Button (more explicit label and confirmation)
        JButton btnBack = new JButton("Save & Back to Menu");
        Theme.styleButton(btnBack, true);
        btnBack.addActionListener(e -> frame.showPanel("Menu"));
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(15, 15, 15, 15);
        add(btnBack, gbc);
    }


}