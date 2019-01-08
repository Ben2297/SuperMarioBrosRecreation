package VideoGame;

import Utilities.Vector2D;

import java.awt.*;

import static VideoGame.Constants.*;

public abstract class GameObject {
    public Vector2D position;
    public Vector2D velocity;
    public boolean dead;
    public double radius;
    public double height;
    public double width;
    public double prevX;
    public double prevY;

    // Constructs a new game object
    public GameObject() {
        super();
    }

    // Updates a hit game object to dead
    public void hit() {
        dead = true;
    }

    // Checks if game object is hit
    public boolean collisionHandling(GameObject other) {
        /*if (this.getClass() != other.getClass() && this.overlap(other)) {
            //this.hit();
            //other.hit();
            System.out.println("hit");
        }*/
        return false;
    }

    public Rectangle getBounds()
    {
        return new Rectangle((int)(position.x + velocity.x * DT), (int)(position.y + velocity.y * DT), (int)width, (int)height);
    }

    public Rectangle getBoundsTop() {
        return new Rectangle(
                (int)(position.x + velocity.x * DT),
                (int)(position.y + velocity.y * DT),
                (int)width,
                (int)(height * 0.1f)
        );
    }

    public Rectangle getBoundsBottom() {
        return new Rectangle(
                (int)(position.x + velocity.x * DT),
                (int)((position.y + velocity.y * DT) + height - (height * 0.1f)),
                (int)width,
                (int)(height * 0.1f)
        );
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle(
                (int)(position.x + velocity.x * DT),
                (int)(position.y + (height * 0.1) + velocity.y * DT),
                (int)(width * 0.1f),
                (int)(height - ((height * 0.1) * 2))
        );
    }

    public Rectangle getBoundsRight() {
        return new Rectangle(
                (int)(position.x + width - (width * 0.1f) + velocity.x * DT),
                (int)(position.y + (height * 0.1) + velocity.y * DT),
                (int)(width * 0.1f),
                (int)(height - ((height * 0.1) * 2))
        );
    }

    // updates general game object position
    public void update()
    {
        prevX = position.x;
        prevY = position.y;
        position.addScaled(velocity, DT);
    }

    public abstract void draw(Graphics2D g);
}
