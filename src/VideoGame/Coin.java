package VideoGame;

import Utilities.Vector2D;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import static VideoGame.Constants.DT;

public class Coin extends GameObject
{

    private static final double ACCELERATION = 700;

    private static final double GRAVITY = 2.5;

    private Vector2D direction;
    private Vector2D jumpDirection;

    public Coin(Vector2D pos)
    {
        direction = new Vector2D();
        direction.set(0, -1);
        position = new Vector2D();
        position.set(pos);
        velocity = new Vector2D();
        velocity.set(0, 0);
        jumpDirection = new Vector2D();
        jumpDirection.set(0, -1);

        try
        {
            currentImage = ImageIO.read(new File("Coin.png"));
        } catch (IOException ie)
        {
            System.out.println("Image file not found");
        }

        velocity.addScaled(direction, (ACCELERATION * DT * 60));
        height = currentImage.getHeight();
        width = currentImage.getWidth();
    }

    public void update() {
        position.y += (velocity.y * DT);
        applyGravity();

        if (velocity.y > 200)
        {
            hit();
        }
    }

    private void applyGravity()
    {
        velocity.addScaled(jumpDirection, (ACCELERATION * DT * -GRAVITY));
    }

    public void draw(Graphics2D g)
    {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.scale(1, 1);
        //g.fillRect(0, 0, (int)width, (int)height);
        g.setTransform(at);
        g.drawImage(currentImage, (int)position.x, (int)position.y, null);
        //g.draw(getBounds());
//        g.draw(getBoundsRight());
//        g.draw(getBoundsLeft());
//        g.draw(getBoundsTop());
//        g.draw(getBoundsBottom());
    }
}
