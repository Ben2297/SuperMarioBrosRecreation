package VideoGame;

import Utilities.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block extends GameObject {
    private static final int RADIUS = 20;
    private static final Color COLOR = Color.ORANGE;
    private Vector2D direction;
    private Color blockColor;
    BufferedImage image;

    public Block(Vector2D pos, Color col)
    {
        direction = new Vector2D();
        direction.set(1, 0);
        position = new Vector2D();
        position.set(pos);
        velocity = new Vector2D();
        velocity.set(0, 0);
        radius = RADIUS;
        blockColor = col;
        try
        {
            image = ImageIO.read(new File("Brickblock.png"));
        } catch (IOException ie)
        {

        }
    }

    public void draw(Graphics2D g)
    {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        g.scale(1, 1);
        g.setColor(blockColor);
        g.fillRect(0, 0, (int)radius * 2, (int)radius * 2);
        g.setTransform(at);
        g.drawImage(image, (int)position.x - 40, (int)position.y, null);
    }
}
