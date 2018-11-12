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

    public static final double JUMP_STRENGTH = 20;

    public static final double GRAVITY = 0.5;

    public static final double FLOOR = 800;

    private static final Color COLOR = Color.cyan;

    private Vector2D direction;
    private Vector2D jumpDirection;
    private boolean jumping;
    //public Bullet bullet = null;

    // controller which provides an Action object in each frame
    private Controller ctrl;

    // Constructs ship object, initialises fields
    public Player(Controller ctrl, Vector2D posit) {
        this.ctrl = ctrl;
        Vector2D pos = new Vector2D();
        Vector2D vel = new Vector2D();
        direction = new Vector2D();
        jumpDirection = new Vector2D();
        pos.set(60, (FLOOR - RADIUS * 2));
        vel.set(0, 0);
        direction.set(1, 0);
        jumpDirection.set(0, -1);
        position = new Vector2D();
        position.set(posit);
        velocity = new Vector2D();
        velocity.set(vel);
        jumping = false;
        dead = false;
        radius = RADIUS;
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

    // Updates the direction and state of ship (thrusting, firing, etc.)
    public void update() {
        super.update();
        VideoGame.Action action = ctrl.action();
        //direction.rotate(action.turn * STEER_RATE * DT);
        velocity.addScaled(direction, (MAG_ACC * DT * action.move));
        if (position.y < (FLOOR - RADIUS * 2))
        {
            velocity.addScaled(jumpDirection, (MAG_ACC * DT * -GRAVITY));
        } else {
            if (!action.jump)
            {
                velocity.y = 0;
                jumping = false;
            }
        }
        velocity.mult(DRAG);
        if (action.move == 1) {
            //thrusting = true;
            //SoundManager.startThrust();
        } else {
            //thrusting = false;
            //SoundManager.stopThrust();
        }
        if (action.jump && !jumping) {
            jump();
            action.jump = false;
            jumping = true;
        }
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
