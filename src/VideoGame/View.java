package VideoGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static VideoGame.Constants.FRAME_HEIGHT;
import static VideoGame.Constants.FRAME_WIDTH;

public class View extends JComponent {
    private Game game;
    private Image image;
    private Camera camera;
    private BufferedImage menuLogoImage;
    private BufferedImage coinImage;
    private BufferedImage castleImage;
    private BufferedImage cloudImage;
    private BufferedImage hillImage;
    private BufferedImage bushImage;

    public View(Game game, Image image, Camera camera) {
        this.game = game;
        this.image = image;
        this.camera = camera;

        try {
            menuLogoImage = ImageIO.read(new File("supermariobroslogo.png"));
            coinImage = ImageIO.read(new File("Coin.png"));
            castleImage = ImageIO.read(new File("Castle.png"));
            cloudImage = ImageIO.read(new File("Cloud.png"));
            hillImage = ImageIO.read(new File("Hill.png"));
            bushImage = ImageIO.read(new File("Bush.png"));
        } catch (IOException e) {
            System.out.println("Incorrect file name");
        }
    }

    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.scale(1.2, 1.2);
        g.translate(-camera.position.x, -camera.position.y);
        Rectangle cameraRec = new Rectangle((int)camera.position.x, (int)camera.position.y, FRAME_WIDTH, FRAME_HEIGHT);
        g.drawImage(menuLogoImage, -825, 250, this);
        g.drawImage(coinImage, (int)camera.position.x + 400, (int)camera.position.y + 30, this);
        g.drawImage(castleImage, 8000, 320, this);
        g.drawImage(cloudImage, 800, 250, this);
        g.drawImage(cloudImage, 1400, 350, this);
        g.drawImage(cloudImage, 2400, 200, this);
        g.drawImage(cloudImage, 3500, 150, this);
        g.drawImage(cloudImage, 4200, 300, this);
        g.drawImage(cloudImage, 5000, 250, this);
        g.drawImage(cloudImage, 6000, 350, this);
        g.drawImage(cloudImage, 6600, 200, this);
        g.drawImage(cloudImage, 7200, 150, this);
        g.drawImage(hillImage, 400, 660, this);
        g.drawImage(hillImage, 2200, 660, this);
        g.drawImage(hillImage, 4000, 660, this);
        g.drawImage(hillImage, 7000, 660, this);
        g.drawImage(bushImage, 200, 690, this);
        g.drawImage(bushImage, 1000, 690, this);
        g.drawImage(bushImage, 1800, 690, this);
        g.drawImage(bushImage, 3000, 690, this);
        g.drawImage(bushImage, 4200, 690, this);
        g.drawImage(bushImage, 6700, 690, this);
        synchronized (Game.class) {
            for (GameObject object : game.objects) {
                if (object.getClass() == PowerUp.class || object.getClass() == Coin.class)
                {
                    if (object.getBounds().intersects(cameraRec))
                    {
                        object.draw(g);
                    }
                }
            }

            for (GameObject object : game.objects) {
                if (object.getClass() != PowerUp.class && object.getClass() != Coin.class)
                {
                    if (object.getBounds().intersects(cameraRec))
                    {
                        object.draw(g);
                    }
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }
}