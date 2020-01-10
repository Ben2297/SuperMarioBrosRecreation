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
    public BufferedImage currentImage;

    public GameObject() {
        super();
        position = new Vector2D();
        velocity = new Vector2D();
    }

    // sets the object as dead to be removed from the game
    public void hit() {
        dead = true;
    }

    // gets the general bounds of the object
    public Rectangle getBounds()
    {
        return new Rectangle((int)(position.x + velocity.x * DT), (int)(position.y + velocity.y * DT), (int)width, (int)height);
    }

    // gets the top bounds of the object
    public Rectangle getBoundsTop() {
        return new Rectangle(
                (int)(position.x + velocity.x * DT),
                (int)(position.y + velocity.y * DT),
                (int)(width),
                (int)(height * 0.18f)
        );
    }

    // gets the bottom bounds of the object
    public Rectangle getBoundsBottom() {
        return new Rectangle(
                (int)(position.x + velocity.x * DT),
                (int)((position.y + velocity.y * DT) + height - (height * 0.18f)),
                (int)(width),
                (int)(height * 0.18f)
        );
    }

    // gets the left bounds of the object
    public Rectangle getBoundsLeft() {
        return new Rectangle(
                (int)(position.x + velocity.x * DT),
                (int)(position.y + (height * 0.18f) + velocity.y * DT),
                (int)(width * 0.5f),
                (int)(height - ((height * 0.18f) * 2))
        );
    }

    // gets the right bounds of the object
    public Rectangle getBoundsRight() {
        return new Rectangle(
                (int)((position.x + width - (width * 0.5f)) + velocity.x * DT),
                (int)(position.y + (height * 0.18f) + velocity.y * DT),
                (int)(width * 0.5f),
                (int)(height - ((height * 0.18f) * 2))
        );
    }

    // updates general game object position
    public void update()
    {
        position.addScaled(velocity, DT);
        if (position.y > 800 || (position.y + height) < 0)
        {
            this.dead = true;
        }
    }

    // abstract draw method to be overridden by subclasses
    public abstract void draw(Graphics2D g);
}
