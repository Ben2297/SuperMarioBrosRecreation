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
        g.translate((int)-camera.position.x, (int)-camera.position.y);
        //g.drawImage(image, 0, 0, this);
        synchronized (Game.class) {
            for (GameObject object : game.objects) {
                if (object.getClass() == PowerUp.class || object.getClass() == Coin.class)
                {
                    object.draw(g);
                }
            }

            for (GameObject object : game.objects) {
                if (object.getClass() != PowerUp.class && object.getClass() != Coin.class)
                {
                    object.draw(g);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }
}