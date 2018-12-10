package VideoGame;

import Utilities.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static VideoGame.Constants.*;

public class Player extends GameObject {
    private static final int RADIUS = 20;

    // acceleration when thrust is applied
    private static final double MAG_ACC = 800;

    // constant speed loss factor
    private static final double DRAG = 0.94;

    public static final double JUMP_STRENGTH = 110;

    public static final double GRAVITY = 1.5;

    public static final double FLOOR = 800;

    private static final Color COLOR = Color.red;

    private Vector2D direction;
    private Vector2D jumpDirection;
    private boolean jumping;
    private boolean moving;
    BufferedImage image;

    // controller which provides an Action object in each frame
    private Controller ctrl;

    // Constructs player object, initialises fields
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
        try
        {
            image = ImageIO.read(new File("Sprite.png"));
        } catch (IOException ie)
        {

        }

    }

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
        position.wrap(FRAME_WIDTH, FRAME_HEIGHT);
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
            onPlatform = false;
        }

        System.out.println(onPlatform);
    }

    public boolean collisionHandling(GameObject other) {
        if (!(other instanceof Player) && ((position.x + radius + 0.2) >= (other.position.x - other.radius) &&
                (position.x + radius + 0.2) <= (other.position.x + other.radius)) &&
                ((((position.y + radius) >= (other.position.y - radius)) &&
                ((position.y + radius) <= (other.position.y + radius))) ||
                (((position.y - radius) >= (other.position.y - radius)) &&
                ((position.y - radius) <= (other.position.y + radius)))))
        {
            position.x = prevX;
            velocity.x = 0;
        }

        if (!(other instanceof Player) && ((position.x - radius - 0.2) >= (other.position.x - other.radius) &&
                (position.x - radius - 0.2) <= (other.position.x + other.radius)) &&
                ((((position.y + radius) >= (other.position.y - radius)) &&
                ((position.y + radius) <= (other.position.y + radius))) ||
                (((position.y - radius) >= (other.position.y - radius)) &&
                ((position.y - radius) <= (other.position.y + radius)))))
        {
            position.x = prevX;
            velocity.x = 0;
        }

        if (!(other instanceof Player) && ((((position.y + radius + 1) >= (other.position.y - other.radius)) &&
                ((position.y + radius) < (other.position.y + other.radius))) &&
                ((((position.x - radius) <= (other.position.x + other.radius)) &&
                ((position.x - radius) >= (other.position.x - radius))) ||
                (((position.x + radius) >= (other.position.x - radius)) &&
                        ((position.x + radius) <= (other.position.x + radius))))))
        {
            onPlatform = true;
            jumping = false;
            position.y = prevY;
            velocity.y = 0;
        }

        if (!(other instanceof Player) && ((((position.y - radius - 1) <= (other.position.y + other.radius)) &&
                ((position.y - radius) > (other.position.y - other.radius))) &&
                ((((position.x - radius) <= (other.position.x + other.radius)) &&
                        ((position.x - radius) >= (other.position.x - radius))) ||
                        (((position.x + radius) >= (other.position.x - radius)) &&
                                ((position.x + radius) <= (other.position.x + radius))))))
        {
            position.y = prevY;
            velocity.y = 0;
        }
        return onPlatform;
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
        //g.drawImage(image, (int)position.x - 40, (int)position.y, null);
    }

    public void resetOnPlatform()
    {
        onPlatform = false;
    }
}
