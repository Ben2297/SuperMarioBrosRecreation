package VideoGame;

import javax.swing.*;
import java.awt.*;

public class View extends JComponent {
    private Game game;
    private Image image;

    public View(Game game, Image image) {
        this.game = game;
        this.image = image;
    }

    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.drawImage(image, 0, 0, this);
        synchronized (Game.class) {
            for (GameObject object : game.objects) {
                object.draw(g);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }
}