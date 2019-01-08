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
    private static final int HEIGHT = 32;

    private static final int WIDTH = 26;

    private static final double MAG_ACC = 1000;

    private static final double DRAG = 0.94;

    public static final double JUMP_STRENGTH = 200;

    public static final double GRAVITY = 1.8;

    private static final Color COLOR = Color.red;

    private Vector2D direction;
    private Vector2D jumpDirection;
    private boolean moving;
    private boolean falling;
    public boolean canJump;
    BufferedImage runRightImage;
    BufferedImage runLeftImage;
    BufferedImage jumpRightImage;
    BufferedImage jumpLeftImage;
    BufferedImage currentImage;
    VideoGame.Action action;
    Game game;

    private Controller ctrl;

    public Player(Controller ctrl, Vector2D pos, Game game) {
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
        moving = false;
        dead = false;
        height = HEIGHT;
        width = WIDTH;
        falling = true;
        this.game = game;

        try
        {
            runRightImage = ImageIO.read(new File("MarioRunRight.png"));
            runLeftImage = ImageIO.read(new File("MarioRunLeft.png"));
            jumpRightImage = ImageIO.read(new File("MarioJumpRight.png"));
            jumpLeftImage = ImageIO.read(new File("MarioJumpLeft.png"));

        } catch (IOException ie)
        {

        }

        currentImage = runRightImage;
    }

    private void jump() {
        velocity.addScaled(jumpDirection, (MAG_ACC * DT * JUMP_STRENGTH));
        if (currentImage == runRightImage)
        {
            currentImage = jumpRightImage;
        } else if (currentImage == runLeftImage)
        {
            currentImage = jumpLeftImage;
        }
    }

    public void hit() {
        super.hit();
    }

    public void update() {
        prevX = position.x;
        prevY = position.y;

        if (!hasHorizontalCollision()) { position.x += (velocity.x * DT); }
        if (!hasVerticalCollision()) { position.y += (velocity.y * DT); }




        position.wrap(FRAME_WIDTH, FRAME_HEIGHT);
        action = ctrl.action();
        velocity.addScaled(direction, (MAG_ACC * DT * action.move));
        velocity.mult(DRAG);

        if (falling)
        {
            applyGravity();
        }

        if (action.move == 1 || action.move == -1) {
            moving = true;
        } else {
            moving = false;
        }

        if (action.move == 1 && canJump)
        {
            currentImage = runRightImage;
        } else if (action.move == -1 && canJump)
        {
            currentImage = runLeftImage;
        } else if (action.move == 1 && !canJump)
        {
            currentImage = jumpRightImage;
        } else if (action.move == -1 && !canJump)
        {
            currentImage = jumpLeftImage;
        }

        if (action.jump && canJump) {
            jump();
            canJump = false;
        }

        System.out.println(falling);
    }

    public boolean hasVerticalCollision()
    {
        for (int i = 0; i < game.blocks.size(); i++)
        {
            Block b = game.blocks.get(i);
            if (getBoundsBottom().intersects(b.getBoundsTop()) && velocity.y > 0 && !hasHorizontalCollision())
            {
                canJump = true;
                falling = false;
                velocity.y = 0;
                if (currentImage == jumpRightImage)
                {
                    currentImage = runRightImage;
                } else if (currentImage == jumpLeftImage)
                {
                    currentImage = runLeftImage;
                }
                //position.y = b.position.y - height;
                return true;
            } else
            {
                falling = true;
            }

            if (getBoundsTop().intersects(b.getBoundsBottom()) && velocity.y < 0)
            {
                velocity.y = 0;
                return true;
            }
        }
        return false;
    }

    public boolean hasHorizontalCollision()
    {
        for (int i = 0; i < game.blocks.size(); i++)
        {
            Block b = game.blocks.get(i);
            if (getBounds().intersects(b.getBoundsRight()) && velocity.x < 0)
            {
                velocity.x = 0;
                position.x = b.position.x + b.width;
                System.out.println("L collision");
                return true;
            }

            if (getBounds().intersects(b.getBoundsLeft()) && velocity.x > 0)
            {
                velocity.x = 0;
                position.x = b.position.x - width;
                System.out.println("R collision");
                return true;
            }
        }

        return false;
    }

    private void applyGravity()
    {
        velocity.addScaled(jumpDirection, (MAG_ACC * DT * -GRAVITY));
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        //g.rotate(rot);
        g.scale(1, 1);
        g.setColor(COLOR);
        //g.fillRect(0, 0, (int)width, (int)height);
        g.setTransform(at);
        g.setColor(Color.ORANGE);
        //g.draw(getBounds());
        //g.draw(getBoundsRight());
        //g.draw(getBoundsLeft());
        //g.draw(getBoundsTop());
        //g.draw(getBoundsBottom());
        g.drawImage(currentImage, (int)position.x, (int)position.y, null);
    }
}
