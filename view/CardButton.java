package view;

import model.Card;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * A specialized button representing a card on the UI grid.
 * Handles the visual rendering of the card (face up/down) and automatic image scaling.
 */
public class CardButton extends JButton {
    private Card card;
    private Dimension targetSize; 
    // Animation state
    private boolean prevFaceUp = false;
    private float animProgress = 1.0f; // 0.0 = showing back, 1.0 = showing front
    private javax.swing.Timer animTimer;
    private final int ANIM_DURATION = 240; // ms

    /**
     * Initializes the button with a specific card model and target dimensions.
     * @param card The data model for this button.
     * @param targetSize The width and height for the card image.
     */
    public CardButton(Card card, Dimension targetSize) {
        this.card = card;
        this.targetSize = targetSize;

        // Basic button configuration
        setPreferredSize(targetSize);
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

        setFocusable(false); // Removes the focus outline for a cleaner look
        setOpaque(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effects
        addMouseListener(new java.awt.event.MouseAdapter() {
            private Border origBorder = null;
            private Dimension origSize = getPreferredSize();
            private javax.swing.Timer pulseTimer;

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Do not change hover visuals if card is currently selected or matched
                if (card.isFaceUp() || card.isMatched()) return;

                // Capture current border as the revert target
                origBorder = getBorder();

                switch (Theme.currentTheme) {
                    case COZY:
                        // subtle glowing white border on top of existing border
                        setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.WHITE, 2, true), origBorder));
                        break;
                    case CYBERPUNK:
                        // neon border pulse
                        setBorder(new LineBorder(Theme.SECONDARY, 3, true));
                        pulseTimer = new javax.swing.Timer(300, ev -> {
                            if (!(getBorder() instanceof LineBorder)) return;
                            Color c = ((LineBorder) getBorder()).getLineColor();
                            int alpha = Math.min(255, c.getAlpha() + 30);
                            setBorder(new LineBorder(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha), 3, true));
                        });
                        pulseTimer.setRepeats(true);
                        pulseTimer.start();
                        break;
                    case SAPPHIRE:
                        // slight scale up and shine effect (simulate with thicker border)
                        setPreferredSize(new Dimension((int)(origSize.width*1.05), (int)(origSize.height*1.05)));
                        setBorder(new LineBorder(Theme.SECONDARY, 2, true));
                        revalidate();
                        break;
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Revert to the captured border to preserve theme details
                setBorder(origBorder != null ? origBorder : BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
                setPreferredSize(origSize);
                revalidate();
                if (pulseTimer != null) {
                    pulseTimer.stop();
                    pulseTimer = null;
                }
            }
        });
        
        // Render initial state
        updateDisplay();
        // Ensure prevFaceUp matches initial state
        prevFaceUp = card.isFaceUp();
    }

    /**
     * Updates the button's appearance based on the card's state (Face Up vs Face Down).
     * Handles image scaling to ensure the icon fits the button perfectly.
     */
    public void updateDisplay() {
        // Update scaled front image if available
        ImageIcon icon = card.getFrontImage();
        if (icon != null) {
            Image img = icon.getImage();
            scaledFrontImage = img.getScaledInstance(targetSize.width, targetSize.height, Image.SCALE_SMOOTH);
        } else {
            scaledFrontImage = null;
        }

        boolean nowFaceUp = card.isFaceUp() || card.isMatched();

        // If the face-up state changed, animate if enabled
        if (nowFaceUp != prevFaceUp && util.Settings.isAnimationsEnabled()) {
            startFlipAnimation(nowFaceUp);
        } else {
            // No state change or animations disabled — set visuals immediately
            if (nowFaceUp) {
                setIcon(scaledFrontImage != null ? new ImageIcon(scaledFrontImage) : null);
                if (card.isMatched()) {
                    setEnabled(false);
                    setDisabledIcon(getIcon());
                } else {
                    setEnabled(true);
                }
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder());
                animProgress = 1.0f;
                prevFaceUp = true;
            } else {
                setIcon(null);
                setEnabled(true);
                switch (Theme.currentTheme) {
                    case COZY:
                        setBackground(Theme.SECONDARY);
                        setBorder(new LineBorder(Theme.MUTED, 1, true));
                        break;
                    case CYBERPUNK:
                        setBackground(new Color(0x0B0B0B));
                        setBorder(new LineBorder(Theme.SECONDARY, 2, true));
                        break;
                    case SAPPHIRE:
                        setBackground(Theme.PRIMARY);
                        setBorder(new LineBorder(Theme.MUTED, 1, true));
                        break;
                    default:
                        setBackground(Theme.SURFACE);
                }
                animProgress = 0.0f;
                prevFaceUp = false;
            }
            repaint();
        }
    }

    public Card getCard() {
        return card;
    }

    // --- Animation: simple fade between back and front ---
    private Image scaledFrontImage = null;

    private void startFlipAnimation(boolean toFaceUp) {
        if (animTimer != null && animTimer.isRunning()) {
            animTimer.stop();
        }

        final int interval = 40; // ms
        final int steps = Math.max(1, ANIM_DURATION / interval);
        final float stepAmount = 1.0f / steps;

        if (toFaceUp) {
            animProgress = 0.0f;
        } else {
            animProgress = 1.0f;
        }

        animTimer = new javax.swing.Timer(interval, null);
        animTimer.addActionListener(ev -> {
            if (toFaceUp) {
                animProgress += stepAmount;
                if (animProgress >= 1.0f) {
                    animProgress = 1.0f;
                    animTimer.stop();
                    // finalize state
                    setIcon(scaledFrontImage != null ? new ImageIcon(scaledFrontImage) : null);
                    setBackground(Color.WHITE);
                    setBorder(BorderFactory.createEmptyBorder());
                    prevFaceUp = true;
                }
            } else {
                animProgress -= stepAmount;
                if (animProgress <= 0.0f) {
                    animProgress = 0.0f;
                    animTimer.stop();
                    // finalize state
                    setIcon(null);
                    setEnabled(true);
                    switch (Theme.currentTheme) {
                        case COZY:
                            setBackground(Theme.SECONDARY);
                            setBorder(new LineBorder(Theme.MUTED, 1, true));
                            break;
                        case CYBERPUNK:
                            setBackground(new Color(0x0B0B0B));
                            setBorder(new LineBorder(Theme.SECONDARY, 2, true));
                            break;
                        case SAPPHIRE:
                            setBackground(Theme.PRIMARY);
                            setBorder(new LineBorder(Theme.MUTED, 1, true));
                            break;
                        default:
                            setBackground(Theme.SURFACE);
                    }
                    prevFaceUp = false;
                }
            }
            repaint();
        });
        animTimer.setInitialDelay(0);
        animTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (animProgress > 0.0f && animProgress < 1.0f && util.Settings.isAnimationsEnabled()) {
            // Custom cross-fade
            Graphics2D g2 = (Graphics2D) g.create();

            // Draw back (alpha = 1 - animProgress)
            float backAlpha = 1.0f - animProgress;
            Composite prev = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, backAlpha));
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setComposite(prev);

            // Draw standard button border/background features by painting children? We'll draw icon overlay next
            // Draw front (alpha = animProgress)
            if (scaledFrontImage != null) {
                int x = (getWidth() - scaledFrontImage.getWidth(null)) / 2;
                int y = (getHeight() - scaledFrontImage.getHeight(null)) / 2;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animProgress));
                g2.drawImage(scaledFrontImage, x, y, this);
                g2.setComposite(prev);
            }

            g2.dispose();
        } else {
            // Default rendering
            super.paintComponent(g);
        }
    }
}