package VideoGame;

import javax.swing.*;
import java.awt.*;

public class View extends JComponent {
    private Game game;
    private Image image;
    private Camera camera;

    public View(Game game, Image image, Camera camera) {
        this.game = game;
        this.image = image;
        this.camera = camera;
    }

    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.translate(-camera.position.x, -camera.position.y);
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