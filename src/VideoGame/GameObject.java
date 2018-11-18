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
    public boolean falling;
    public boolean onPlatform;

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
    public void collisionHandling(GameObject other) {
        /*if (this.getClass() != other.getClass() && this.overlap(other)) {
            //this.hit();
            //other.hit();
            System.out.println("hit");
        }*/
    }

    // updates general game object position
    public void update()
    {
        prevX = position.x;
        prevY = position.y;
        position.addScaled(velocity, DT);
        position.wrap(FRAME_WIDTH, FRAME_HEIGHT);
    }

    public void resetCollisions()
    {
        onPlatform = false;
    }

    public abstract void draw(Graphics2D g);
}
