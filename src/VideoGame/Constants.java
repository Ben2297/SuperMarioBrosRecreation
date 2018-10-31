package VideoGame;

import java.awt.*;

// Holds constant values uses throughout the game
public class Constants {
    public static final int FRAME_HEIGHT = 800;
    public static final int FRAME_WIDTH = 1000;
    public static final Dimension FRAME_SIZE = new Dimension(
            Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
    public static final int DELAY = 20;  // in milliseconds
    public static final double DT = DELAY / 1000.0;  // in seconds
}