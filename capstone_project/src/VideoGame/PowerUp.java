package VideoGame;

import Utilities.Vector2D;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static VideoGame.Constants.DT;

public class PowerUp extends GameObject {

    private static final double ACCELERATION = 1400;

    private static final double DRAG = 0.93;

    private static final double GRAVITY = 2.5;

    private BufferedImage superMushroomImage;
    private boolean falling;
    private Vector2D direction;
    private Vector2D jumpDirection;
    private boolean inBlock;
    Game game;

    public PowerUp(Vector2D pos, Game game)
    {
        this.game = game;
        Vector2D vel = new Vector2D();
        vel.set(0, 0);
        position.set(pos);
        velocity.set(vel);
        direction = new Vector2D();
        direction.set(1, 0);
        jumpDirection = new Vector2D();
        jumpDirection.set(0, -1);
        dead = false;
        inBlock = true;

        try
        {
            superMushroomImage = ImageIO.read(new File("SuperMushroom.png"));
        } catch (IOException ie)
        {
            System.out.println("Image read failed");
        }

        currentImage = superMushroomImage;
        height = currentImage.getHeight();
        width = currentImage.getWidth();

        velocity.addScaled(jumpDirection, (ACCELERATION * DT * 10));
    }

    public void update() {
        if (inBlock)
        {
            position.y += (velocity.y * DT);
            if (!hasVerticalCollision())
            {
                inBlock = false;
            }
        } else
        {
            if (!hasHorizontalCollision()) { position.x += (velocity.x * DT); }
            if (!hasVerticalCollision()) { position.y += (velocity.y * DT); }

            velocity.addScaled(direction, (ACCELERATION * DT));
            velocity.mult(DRAG);

            if (falling)
            {
                applyGravity();
            }
        }

        height = currentImage.getHeight();
        width = currentImage.getWidth();
    }

    public boolean hasVerticalCollision()
    {
        for (int i = 0; i < game.scenery.size(); i++)
        {
            if (inBlock)
            {
                GameObject b = game.scenery.get(i);
                if (getBounds().intersects(b.getBounds()) && velocity.y < 0)
                {
                    return true;
                }
            } else
            {
                GameObject b = game.scenery.get(i);
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

        }
        return false;
    }

    public boolean hasHorizontalCollision()
    {
        for (int i = 0; i < game.scenery.size(); i++)
        {
            GameObject b = game.scenery.get(i);
            if (getBoundsLeft().intersects(b.getBoundsRight()))
            {
                velocity.x = velocity.x * -1;
                direction.mult(-1);
                position.x = b.position.x + b.width;

                return true;
            }

            if (getBoundsRight().intersects(b.getBoundsLeft()))
            {
                velocity.x = velocity.x * -1;
                direction.mult(-1);
                position.x = b.position.x - width;
                return true;
            }
        }
        return false;
    }

    private void applyGravity()
    {
        velocity.addScaled(jumpDirection, (ACCELERATION * DT * -GRAVITY));
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.scale(1, 1);
        //g.setColor(COLOR);
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
