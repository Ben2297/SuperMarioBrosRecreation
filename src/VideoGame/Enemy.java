package VideoGame;

import Utilities.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static VideoGame.Constants.DT;
import static VideoGame.Constants.FRAME_HEIGHT;
import static VideoGame.Constants.FRAME_WIDTH;

public class Enemy extends GameObject{
    private static final double MAG_ACC = 700;

    private static final double DRAG = 0.93;

    private static final double GRAVITY = 1.8;

    private Vector2D direction;
    private Vector2D jumpDirection;
    private boolean falling;
    private BufferedImage currentImage;
    private BufferedImage goombaImage1;

    Game game;

    public Enemy(Vector2D pos, Game game) {
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
        dead = false;
        falling = true;
        this.game = game;

        try
        {
            goombaImage1 = ImageIO.read(new File("Goomba1.png"));
        } catch (IOException ie)
        {
            System.out.println("Image read failed");
        }

        currentImage = goombaImage1;
        height = currentImage.getHeight();
        width = currentImage.getWidth();
    }

    public void update() {
        prevX = position.x;
        prevY = position.y;

        if (!hasHorizontalCollision()) { position.x += (velocity.x * DT); }
        if (!hasVerticalCollision()) { position.y += (velocity.y * DT); }

        position.wrap(FRAME_WIDTH, FRAME_HEIGHT);
        velocity.addScaled(direction, (MAG_ACC * DT));
        velocity.mult(DRAG);

        if (falling)
        {
            applyGravity();
        }

        height = currentImage.getHeight();
        width = currentImage.getWidth();

        //System.out.println(velocity.y);
    }

    public boolean hasVerticalCollision()
    {
        for (int i = 0; i < game.blocks.size(); i++)
        {
            Block b = game.blocks.get(i);
            if (getBoundsBottom().intersects(b.getBoundsTop()) && velocity.y > 0 && !hasHorizontalCollision())
            {
                falling = false;
                velocity.y = 0;
                return true;
            } else
            {
                falling = true;
            }

            if (getBounds().intersects(b.getBoundsBottom()) && velocity.y < 0)
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
            if (getBoundsLeft().intersects(b.getBoundsRight()))
            {
                velocity.x = 0;
                direction.mult(-1);
                position.x = b.position.x + b.width;
                System.out.println("L collision");

                return true;
            }

            if (getBoundsRight().intersects(b.getBoundsLeft()))
            {
                velocity.x = 0;
                direction.mult(-1);
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
        g.scale(1, 1);
        //g.setColor(COLOR);
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
