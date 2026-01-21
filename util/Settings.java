package util;

/**
 * Lightweight runtime settings holder.
 * Currently stores whether card flip animations are enabled.
 */
public class Settings {
    private static boolean animationsEnabled = true; // default

    public static boolean isAnimationsEnabled() {
        return animationsEnabled;
    }

    public static void setAnimationsEnabled(boolean enabled) {
        animationsEnabled = enabled;
    }
}