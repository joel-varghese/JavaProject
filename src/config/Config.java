package config;

import java.awt.Font;

public final class Config {

    public static final String    ROM_IPL_FILENAME = "IPL.txt";

    public static final String    ROM_FILE_EXTENSION = "txt";

    public static final String    LOG_FILE_EXTENSION = "log";

    // Enables/disabled debugging artifacts and output for the UI
    public static final boolean   UI_DEBUG = true;

    public static final double    UI_SCALE_WIDTH = 0.65;

    public static final double    UI_SCALE_HEIGHT = 0.65;

    public static final double    UI_CONSOLE_SCALE_WIDTH = 0.3;
    
    public static final double    UI_CONSOLE_SCALE_HEIGHT = 0.3;

    public static final int       UI_MARGIN = 2;

    public static final int       UI_FONT_SIZE = 12;

    public static final Font      UI_FONT_MONOSPACE = new Font(Font.MONOSPACED, Font.BOLD, Config.UI_FONT_SIZE);

}