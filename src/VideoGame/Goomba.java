package VideoGame;

import Utilities.Vector2D;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static VideoGame.Constants.DT;

public class Goomba extends Enemy {

    private static final double MAG_ACC = 700;

    private static final double DRAG = 0.93;

    private static final double GRAVITY = 2.5;

    private BufferedImage goombaImage1;
    private BufferedImage goombaImage2;

    public Goomba(Vector2D pos, Game game)
    {
        Vector2D vel = new Vector2D();
        vel.set(0, 0);
        direction = new Vector2D();
        direction.set(-1, 0);
        jumpDirection = new Vector2D();
        jumpDirection.set(0, -1);
        position.set(pos);
        velocity.set(vel);
        dead = false;
        falling = true;
        this.game = game;

        try
        {
            goombaImage1 = ImageIO.read(new File("Goomba1.png"));
            goombaImage2 = ImageIO.read(new File("Goomba2.png"));
        } catch (IOException ie)
        {
            System.out.println("Image read failed");
        }

        currentImage = goombaImage1;
        height = currentImage.getHeight();
        width = currentImage.getWidth();
    }

    public void update() {
        if(System.currentTimeMillis() - lastAnimationProcessed > 300) {
            if (currentImage == goombaImage1)
            {
                currentImage = goombaImage2;
            } else if (currentImage == goombaImage2)
            {
                currentImage = goombaImage1;
            }
            lastAnimationProcessed = System.currentTimeMillis();
        }

        if (!hasHorizontalCollision())
        {
            position.x += (velocity.x * DT);
        }

        if (!hasVerticalCollision())
        {
            position.y += (velocity.y * DT);
        }

        velocity.addScaled(direction, (MAG_ACC * DT));
        velocity.mult(DRAG);

        if (falling)
        {
            applyGravity();
        }

        height = currentImage.getHeight();
        width = currentImage.getWidth();
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
