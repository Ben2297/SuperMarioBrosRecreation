package VideoGame;

import Utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static VideoGame.Constants.*;

public class Player extends GameObject {
    private static final int RADIUS = 8;

    // rotation velocity in radians per second
    private static final double STEER_RATE = 2* Math.PI;

    // acceleration when thrust is applied
    private static final double MAG_ACC = 200;

    // constant speed loss factor
    private static final double DRAG = 0.98;

    private static final Color COLOR = Color.cyan;

    private Vector2D direction;
    private boolean thrusting;
    //public Bullet bullet = null;

    // controller which provides an Action object in each frame
    private Controller ctrl;

    // Constructs ship object, initialises fields
    public Player(Controller ctrl) {
        this.ctrl = ctrl;
        Vector2D pos = new Vector2D();
        Vector2D vel = new Vector2D();
        direction = new Vector2D();
        pos.set(FRAME_WIDTH / 2, FRAME_HEIGHT / 2);
        vel.set(0, 0);
        direction.set(1, 1);
        position = new Vector2D();
        position.set(pos);
        velocity = new Vector2D();
        velocity.set(vel);
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
        velocity.addScaled(direction, (MAG_ACC * DT * action.thrust));
        velocity.mult(DRAG);
        if (action.thrust == 1) {
            thrusting = true;
            //SoundManager.startThrust();
        } else {
            thrusting = false;
            //SoundManager.stopThrust();
        }
        if (action.shoot) {
            //mkBullet();
            action.shoot = false;
        }
    }

    // Draws the ship using coordinates
    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        g.scale(1, 1);
        g.setColor(COLOR);
        g.fillRect(400, 400, 100, 100);
        g.setTransform(at);
    }
}
