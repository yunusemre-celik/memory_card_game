package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Cozy Pastel theme utility. Centralizes colors, fonts and helper styles for the UI.
 */
public class Theme {
    public enum ThemeName { COZY, CYBERPUNK, SAPPHIRE }

    // Current active colors (mutable to allow runtime switching)
    public static Color PRIMARY;
    public static Color SECONDARY;
    public static Color BG;
    public static Color SURFACE;
    public static Color TEXT;
    public static Color MUTED;
    public static Color SHADOW;

    // Radii / sizes
    public static int RADIUS_LG = 20;

    // Fonts (may vary per theme)
    public static Font FONT_TITLE;
    public static Font FONT_HEADING;
    public static Font FONT_BODY;
    public static Font FONT_BUTTON;

    static {
        // Initialize default theme
        setTheme(ThemeName.COZY);
    }

    public static ThemeName currentTheme;

    public static void setTheme(ThemeName theme) {
        currentTheme = theme;
        switch (theme) {
            case COZY:
                applyCozy();
                break;
            case CYBERPUNK:
                applyCyberpunk();
                break;
            case SAPPHIRE:
                applySapphire();
                break;
        }
    }

    private static void applyCozy() {
        PRIMARY = new Color(0x8FBC8F);
        SECONDARY = new Color(0xE0B0FF);
        BG = new Color(0xFDFDD0);
        SURFACE = new Color(0xF5EBDD);
        TEXT = new Color(0x33373A);
        MUTED = new Color(0x7A7D80);
        SHADOW = new Color(50, 40, 40, 16);

        RADIUS_LG = 20;
        FONT_TITLE = new Font("Nunito", Font.BOLD, 36);
        FONT_HEADING = new Font("Nunito", Font.BOLD, 18);
        FONT_BODY = new Font("Nunito", Font.PLAIN, 14);
        FONT_BUTTON = new Font("Nunito", Font.BOLD, 16);
    }

    private static void applyCyberpunk() {
        PRIMARY = new Color(0x00FFFF); // Cyber Cyan
        SECONDARY = new Color(0xFF00FF); // Hot Magenta
        BG = new Color(0x0A0012); // Very dark purple / near-black
        SURFACE = new Color(0x0F0720); // Slightly lighter surface
        TEXT = new Color(0xE6F7FF); // Bright text
        MUTED = new Color(0x7A7D80);
        SHADOW = new Color(0, 255, 255, 40);

        RADIUS_LG = 6; // sharper corners
        FONT_TITLE = new Font("Monospaced", Font.BOLD, 36);
        FONT_HEADING = new Font("Monospaced", Font.BOLD, 16);
        FONT_BODY = new Font("Monospaced", Font.PLAIN, 14);
        FONT_BUTTON = new Font("Monospaced", Font.BOLD, 14);
    }

    private static void applySapphire() {
        PRIMARY = new Color(0x000080); // Deep Navy
        SECONDARY = new Color(0x7DF9FF); // Electric Blue highlight
        BG = new Color(0x001234); // Deep-blue gradient base (approx)
        SURFACE = new Color(0x063659); // semi transparent glass tint approximation
        TEXT = new Color(0xFFFFFF);
        MUTED = new Color(0xA8D6FF);
        SHADOW = new Color(10, 20, 30, 30);

        RADIUS_LG = 14; // moderate rounding for glass
        FONT_TITLE = new Font("SansSerif", Font.BOLD, 36);
        FONT_HEADING = new Font("SansSerif", Font.BOLD, 16);
        FONT_BODY = new Font("SansSerif", Font.PLAIN, 14);
        FONT_BUTTON = new Font("SansSerif", Font.BOLD, 14);
    }

    // Helpers
    public static void applyWindowDefaults(JFrame frame) {
        frame.getContentPane().setBackground(BG);
        frame.getContentPane().setFont(FONT_BODY);
    }

    public static void applyPanel(JPanel panel) {
        panel.setBackground(SURFACE);
        panel.setOpaque(true);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
    }

    public static void styleTitleLabel(JLabel lbl) {
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(PRIMARY);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static void styleHeadingLabel(JLabel lbl) {
        lbl.setFont(FONT_HEADING);
        lbl.setForeground(TEXT);
    }

    public static void styleBodyLabel(JLabel lbl) {
        lbl.setFont(FONT_BODY);
        lbl.setForeground(MUTED);
    }

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, true);
        return btn;
    }

    public static void styleButton(JButton btn, boolean primary) {
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBackground(primary ? PRIMARY : SURFACE);
        btn.setForeground(TEXT);
        btn.setBorder(new LineBorder(new Color(0,0,0,0), 1, RADIUS_LG>10));
        btn.setPreferredSize(new Dimension(250, 50));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // mark button so runtime updater knows if it should be styled as primary
        btn.putClientProperty("themePrimary", primary ? Boolean.TRUE : Boolean.FALSE);

        // Add hover listener that applies theme-specific interactions
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            private Border orig = btn.getBorder();
            private Color origBg = btn.getBackground();
            private Color origFg = btn.getForeground();
            private javax.swing.Timer glitchTimer;

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                switch (currentTheme) {
                    case COZY:
                        // Soft lift + warm shadow
                        btn.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0,0,0,0),1,true), new javax.swing.border.EmptyBorder(6,6,6,6)));
                        btn.setBackground(origBg.brighter());
                        break;
                    case CYBERPUNK:
                        // Neon fill and glitch vibration
                        btn.setBackground(PRIMARY);
                        btn.setForeground(Color.BLACK);
                        btn.setBorder(new LineBorder(SECONDARY, 3, true));
                        // simple vibration with timer
                        glitchTimer = new javax.swing.Timer(40, ev -> {
                            int dx = (Math.random() > 0.5) ? 1 : -1;
                            btn.setLocation(btn.getX()+dx, btn.getY());
                        });
                        glitchTimer.setRepeats(true);
                        glitchTimer.start();
                        break;
                    case SAPPHIRE:
                        // Glow electric blue and increase opacity
                        btn.setBorder(new LineBorder(SECONDARY, 1, true));
                        Color b = btn.getBackground();
                        btn.setBackground(new Color(Math.min(255, b.getRed()+10), Math.min(255, b.getGreen()+10), Math.min(255, b.getBlue()+18), 230));
                        break;
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Revert
                btn.setBorder(orig);
                btn.setBackground(origBg);
                btn.setForeground(origFg);
                if (glitchTimer != null) {
                    glitchTimer.stop();
                    glitchTimer = null;
                }
            }
        });
    }

    public static void styleSmallButton(JButton btn, boolean primary) {
        styleButton(btn, primary);
        btn.setPreferredSize(new Dimension(120, 36));
        btn.putClientProperty("themeSmall", Boolean.TRUE);
    }

    public static void styleComboBox(JComboBox<?> cb) {
        cb.setFont(FONT_BODY);
        cb.setBackground(SURFACE);
        cb.setForeground(TEXT);
        cb.setBorder(new LineBorder(new Color(0,0,0,0),1,true));
    }

    private static boolean isLightText() {
        // crude check: return true if BG is light-ish
        int brightness = (BG.getRed() + BG.getGreen() + BG.getBlue())/3;
        return brightness > 200;
    }

    /**
     * Walk component tree and apply basic theme updates so runtime switching updates visible UI.
     */
    public static void updateComponentTree(Component c) {
        if (c instanceof JFrame) {
            applyWindowDefaults((JFrame) c);
        }
        if (c instanceof JPanel) {
            ((JPanel) c).setBackground(SURFACE);
            ((JPanel) c).setFont(FONT_BODY);
        }
        if (c instanceof JLabel) {
            ((JLabel) c).setForeground(TEXT);
            ((JLabel) c).setFont(FONT_BODY);
        }
        if (c instanceof JButton) {
            JButton b = (JButton) c;
            Object prim = b.getClientProperty("themePrimary");
            boolean primary = prim instanceof Boolean && ((Boolean) prim);
            Object small = b.getClientProperty("themeSmall");
            if (small instanceof Boolean && ((Boolean) small)) {
                styleSmallButton(b, primary);
            } else {
                styleButton(b, primary);
            }
        }
        if (c instanceof JComboBox) {
            styleComboBox((JComboBox<?>) c);
        }
        if (c instanceof Container) {
            for (Component child : ((Container) c).getComponents()) {
                updateComponentTree(child);
            }
        }
    }
}

