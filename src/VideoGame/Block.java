package VideoGame;

import Utilities.Vector2D;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block extends GameObject {
    private static final int HEIGHT = 40;

    private static final int WIDTH = 40;

    private Vector2D direction;
    BufferedImage image;

    public Block(Vector2D pos, int type)
    {
        direction = new Vector2D();
        direction.set(1, 0);
        position = new Vector2D();
        position.set(pos);
        velocity = new Vector2D();
        velocity.set(0, 0);
        height = HEIGHT;
        width = WIDTH;
        try
        {
            if (type == 1)
            {
                image = ImageIO.read(new File("Bricks.png"));
            } else if (type == 2)
            {
                image = ImageIO.read(new File("Ground.png"));
            } else if (type == 3)
            {
                image = ImageIO.read(new File("Question.png"));
            }

        } catch (IOException ie)
        {

        }
    }

    public void draw(Graphics2D g)
    {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.scale(1, 1);
        //g.fillRect(0, 0, (int)width, (int)height);
        g.setTransform(at);
        g.drawImage(image, (int)position.x, (int)position.y, null);
        //g.draw(getBounds());
//        g.draw(getBoundsRight());
//        g.draw(getBoundsLeft());
//        g.draw(getBoundsTop());
//        g.draw(getBoundsBottom());
    }
}
