package VideoGame;

import Utilities.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static VideoGame.Constants.DT;

public class KoopaTroopa extends Enemy {

    private static final double MAG_ACC = 700;

    private static final double DRAG = 0.93;

    private static final double GRAVITY = 1.8;

    private BufferedImage koopaRunLeft;
    private BufferedImage koopaRunLeft1;
    private BufferedImage koopaShell;

    public KoopaTroopa (Vector2D pos, Game game)
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
            koopaRunLeft = ImageIO.read(new File("GreenKoopaTroopaRunLeft.png"));
            koopaRunLeft1 = ImageIO.read(new File("GreenKoopaTroopaRunLeft1.png"));
            koopaShell = ImageIO.read(new File("GreenKoopa TroopaShell.png"));
        } catch (IOException ie)
        {
            System.out.println("Image read failed");
        }

        currentImage = koopaRunLeft;
        height = currentImage.getHeight();
        width = currentImage.getWidth();
        position.y -= (height - 40);
    }

    public void update() {
        if(System.currentTimeMillis() - lastAnimationProcessed > 300) {
            if (velocity.x < 1)
            {
                if (currentImage == koopaRunLeft)
                {
                    currentImage = koopaRunLeft1;
                } else if (currentImage == koopaRunLeft1)
                {
                    currentImage = koopaRunLeft;
                }
                lastAnimationProcessed = System.currentTimeMillis();
            }

        }

        if (!hasHorizontalCollision())
        {
            position.x += (velocity.x * DT);
            if (!hasVerticalCollision()) { position.y += (velocity.y * DT); }
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

    public void hit()
    {
        if (currentImage == koopaRunLeft || currentImage == koopaRunLeft1)
        {
            position.y += (currentImage.getHeight() - koopaShell.getHeight());
            currentImage = koopaShell;
        }

        //super.hit();
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
