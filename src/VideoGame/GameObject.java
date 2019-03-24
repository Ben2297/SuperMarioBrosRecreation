package VideoGame;

import Utilities.Vector2D;
import java.awt.*;
import java.awt.image.BufferedImage;

import static VideoGame.Constants.*;

public abstract class GameObject {
    public Vector2D position;
    public Vector2D velocity;
    public boolean dead;
    public double height;
    public double width;
    public double prevX;
    public double prevY;
    public BufferedImage currentImage;

    public GameObject() {
        super();
        position = new Vector2D();
        velocity = new Vector2D();

    }

    public void hit() {
        dead = true;
    }

    public Rectangle getBounds()
    {
        return new Rectangle((int)(position.x + velocity.x * DT), (int)(position.y + velocity.y * DT), (int)width, (int)height);
    }

    public Rectangle getBoundsTop() {
        return new Rectangle(
                (int)(position.x + velocity.x * DT),
                (int)(position.y + velocity.y * DT),
                (int)(width),
                (int)(height * 0.15f)
        );
    }

    public Rectangle getBoundsBottom() {
        return new Rectangle(
                (int)(position.x + velocity.x * DT),
                (int)((position.y + velocity.y * DT) + height - (height * 0.15f)),
                (int)(width),
                (int)(height * 0.15f)
        );
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle(
                (int)(position.x + velocity.x * DT),
                (int)(position.y + (height * 0.15f) + velocity.y * DT),
                (int)(width * 0.5f),
                (int)(height - ((height * 0.15f) * 2))
        );
    }

    public Rectangle getBoundsRight() {
        return new Rectangle(
                (int)((position.x + width - (width * 0.5f)) + velocity.x * DT),
                (int)(position.y + (height * 0.15f) + velocity.y * DT),
                (int)(width * 0.5f),
                (int)(height - ((height * 0.15f) * 2))
        );
    }

    // updates general game object position
    public void update()
    {
        prevX = position.x;
        prevY = position.y;
        position.addScaled(velocity, DT);
        if (position.y > 800 || (position.y + height) < 0)
        {
            this.dead = true;
        }
    }

    public abstract void draw(Graphics2D g);
}
