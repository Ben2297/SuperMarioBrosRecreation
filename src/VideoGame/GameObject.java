package VideoGame;

import Utilities.Vector2D;

import java.awt.*;

import static VideoGame.Constants.*;

public abstract class GameObject {
    public Vector2D position;
    public Vector2D velocity;
    public boolean dead;
    public double radius;

    // Constructs a new game object
    public GameObject() {
        super();
    }

    // Updates a hit game object to dead
    public void hit() {
        dead = true;
    }

    // Checks for overlap between two game objects
    private boolean overlap(GameObject other){
        return ((position.dist(other.position) - (radius + other.radius)) <= 0);
    }

    // Checks if game object is hit
    public void collisionHandling(GameObject other) {
        /*if (this.getClass() != other.getClass() && this.overlap(other)) {
            this.hit();
            other.hit();
        }*/
        if (this.overlap(other) && other.getClass().equals(Block.class))
        {

        }

    }

    // updates general game object position
    public void update() {
        position.addScaled(velocity, DT);
        position.wrap(FRAME_WIDTH, FRAME_HEIGHT);
    }

    public abstract void draw(Graphics2D g);
}
