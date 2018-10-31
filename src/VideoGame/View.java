package VideoGame;

import javax.swing.*;
import java.awt.*;

import static VideoGame.Constants.FRAME_WIDTH;

public class View extends JComponent {
    private Game game;
    private Image image;

    // Takes game object to draw the objects and also an image for the background
    public View(Game game, Image image) {
        this.game = game;
        this.image = image;
    }

    // Displays the entire game, also shows score, lives and level
    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.drawImage(image, 0, 0, this);
        synchronized (Game.class) {
            for (GameObject object : game.objects) {
                object.draw(g);
            }
            g.setColor(Color.blue);
            g.setFont(new Font("Impact", Font.PLAIN, 22));
            g.drawString(("Score: " + Integer.toString(game.getScore())), 30, 30);
            g.drawString(("Lives: " + Integer.toString(game.lives)), FRAME_WIDTH - 100, 30);
            g.setFont(new Font("Impact", Font.PLAIN, 26));
            g.drawString(("Level " + Integer.toString(game.level)), (FRAME_WIDTH / 2) - 20, 30);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }
}