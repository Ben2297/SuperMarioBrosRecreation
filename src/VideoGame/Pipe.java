package VideoGame;

import Utilities.Vector2D;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

public class Pipe extends GameObject
{
    private Vector2D direction;
    private Vector2D jumpDirection;
    private int type;
    Game game;

    public Pipe(Vector2D pos, Game game, int type)
    {
        direction = new Vector2D();
        direction.set(1, 0);
        position = new Vector2D();
        position.set(pos);
        velocity = new Vector2D();
        velocity.set(0, 0);
        jumpDirection = new Vector2D();
        jumpDirection.set(0, -1);
        this.game = game;
        this.type = type;

        try
        {
            if (type == 1)
            {
                currentImage = ImageIO.read(new File("Pipe.png"));
                position.y -= 40;
            } else if (type == 2)
            {
                currentImage = ImageIO.read(new File("Pipe2.png"));
                position.y -= 60;
            } else if (type == 3)
            {
                currentImage = ImageIO.read(new File("Pipe3.png"));
                position.y -= 80;
            }

        } catch (IOException ie)
        {
            System.out.println("Image file not found");
        }

        height = currentImage.getHeight();
        width = currentImage.getWidth();
    }

    @Override
    public void hit() {}

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
