package VideoGame;

import Utilities.Vector2D;
import java.awt.*;
import java.awt.geom.AffineTransform;

import static VideoGame.Constants.*;

public class Player extends GameObject {
    private static final int RADIUS = 20;

    // acceleration when thrust is applied
    private static final double MAG_ACC = 500;

    // constant speed loss factor
    private static final double DRAG = 0.97;

    public static final double JUMP_STRENGTH = 60;

    public static final double GRAVITY = 0.9;

    public static final double FLOOR = 800;

    private static final Color COLOR = Color.cyan;

    private Vector2D direction;
    private Vector2D jumpDirection;
    private boolean jumping;
    private boolean moving;

    // controller which provides an Action object in each frame
    private Controller ctrl;

    // Constructs ship object, initialises fields
    public Player(Controller ctrl, Vector2D pos) {
        this.ctrl = ctrl;
        Vector2D vel = new Vector2D();
        vel.set(0, 0);
        direction = new Vector2D();
        direction.set(1, 0);
        jumpDirection = new Vector2D();
        jumpDirection.set(0, -1);
        position = new Vector2D();
        position.set(pos);
        velocity = new Vector2D();
        velocity.set(vel);
        jumping = false;
        moving = false;
        dead = false;
        radius = RADIUS;
        falling = false;
        onPlatform = false;
    }

    // Creates a new bullet
    /*private void mkBullet() {
        bullet = new Bullet(new Vector2D(position), new Vector2D(velocity));
        bullet.position.set(position);
        bullet.position.addScaled(direction , 8);
        bullet.velocity.addScaled(direction, 200);
        SoundManager.fire();
    }*/

    private void jump() {
        velocity.addScaled(jumpDirection, (MAG_ACC * DT * JUMP_STRENGTH));
    }

    // Calls the game object hit method
    public void hit() {
        super.hit();
        //SoundManager.play(SoundManager.bangSmall);
    }

    // Updates the direction and state of player (moving, jumping, etc.)
    public void update() {
        super.update();
        VideoGame.Action action = ctrl.action();
        velocity.addScaled(direction, (MAG_ACC * DT * action.move));
        if (((position.y + radius * 2) < FLOOR) && !onPlatform)
        {
            applyGravity();
        } else {
            if (!action.jump)
            {
                velocity.y = 0;
                jumping = false;
            }
        }
        velocity.mult(DRAG);
        if (action.move == 1 || action.move == -1) {
            moving = true;
        } else {
            moving = false;
        }
        if (action.jump && !jumping) {
            jump();
            action.jump = false;
            jumping = true;
        }
    }

    public void collisionHandling(GameObject other) {
        if (!(other instanceof Player) && ((position.x + radius) >= (other.position.x - other.radius) &&
                (position.x + radius) <= (other.position.x + other.radius)) &&
                ((position.y + radius) >= (other.position.y - radius)))
        {
            position.x = prevX;
            velocity.x = 0;
        }

        if (!(other instanceof Player) && ((position.x - radius) >= (other.position.x - other.radius) &&
                (position.x - radius) <= (other.position.x + other.radius)) &&
                ((position.y + radius) >= (other.position.y - radius)))
        {
            position.x = prevX;
            velocity.x = 0;
        }

        if (!(other instanceof Player) && ((position.y + radius + 0.5) >= (other.position.y - other.radius) &&
                ((((position.x - radius) <= (other.position.x + other.radius)) &&
                ((position.x - radius) >= (other.position.x - radius))) ||
                (((position.x + radius) >= (other.position.x - radius)) &&
                        ((position.x + radius) <= (other.position.x + radius))))))
        {
            position.y = prevY;
            velocity.y = 0;
            onPlatform = true;
        }
    }

    private void applyGravity()
    {
        velocity.addScaled(jumpDirection, (MAG_ACC * DT * -GRAVITY));
    }

    // Draws the player using coordinates
    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        g.scale(1, 1);
        g.setColor(COLOR);
        g.fillRect(0, 0, (int)radius * 2, (int)radius * 2);
        g.setTransform(at);
    }
}
