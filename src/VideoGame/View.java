package VideoGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class View extends JComponent {
    private Game game;
    private Image image;
    private Camera camera;
    private BufferedImage menuLogoImage;
    private BufferedImage coinImage;
    private BufferedImage castleImage;
    private BufferedImage cloudImage;

    public View(Game game, Image image, Camera camera) {
        this.game = game;
        this.image = image;
        this.camera = camera;

        try {
            menuLogoImage = ImageIO.read(new File("supermariobroslogo.png"));
            coinImage = ImageIO.read(new File("Coin.png"));
            castleImage = ImageIO.read(new File("Castle.png"));
            cloudImage = ImageIO.read(new File("Cloud.png"));
        } catch (IOException e) {
            System.out.println("Incorrect file name");
        }
    }

    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.scale(1.2, 1.2);
        g.translate(-camera.position.x, -camera.position.y);
        g.drawImage(menuLogoImage, -825, 250, this);
        g.drawImage(coinImage, (int)camera.position.x + 400, (int)camera.position.y + 30, this);
        g.drawImage(castleImage, 8000, 320, this);
        g.drawImage(cloudImage, 800, 250, this);
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