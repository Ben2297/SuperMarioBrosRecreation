package VideoGame;

import Utilities.Vector2D;

import java.awt.*;

import static VideoGame.Constants.*;

public abstract class GameObject {
    public Vector2D position;
    public Vector2D velocity;
    public boolean dead;
    public double radius;
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

    // Checks for overlap between two game objects
    public boolean overlap(GameObject other){
        return ((position.dist(other.position) - (radius + other.radius)) <= 0);
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
        return new Rectangle((int)position.x, (int)position.y, (int)radius*2, (int)radius*2);
    }

    public Rectangle getBoundsTop() {
        return new Rectangle(
                (int)position.x,
                (int)position.y,
                (int)(radius*2),
                (int)((radius * 2) * 0.1f)
        );
    }

    public Rectangle getBoundsBottom() {
        return new Rectangle(
                (int)position.x,
                (int)(position.y + (radius * 2) - ((radius * 2) * 0.1f)),
                (int)(radius*2),
                (int)((radius * 2) * 0.1f)
        );
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle(
                (int)(position.x),
                (int)(position.y + 4),
                (int)((radius*2) * 0.1f),
                (int)((radius*2) - 8)
        );
    }

    public Rectangle getBoundsRight() {
        return new Rectangle(
                (int)(position.x + (radius * 2) - ((radius * 2) * 0.1f)),
                (int)(position.y + 4),
                (int)((radius*2) * 0.1f),
                (int)((radius*2) - 8)
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
