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

    private double ACCELERATION = 900;

    private static final double DRAG = 0.93;

    private static final double GRAVITY = 2.5;

    private boolean moving;
    private boolean inShell;

    private BufferedImage koopaRunLeft;
    private BufferedImage koopaRunLeft1;
    private BufferedImage koopaRunRight;
    private BufferedImage koopaRunRight1;
    private BufferedImage koopaShell;
    private BufferedImage koopaShell1;

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
        moving = true;
        inShell = false;
        this.game = game;

        try
        {
            koopaRunLeft = ImageIO.read(new File("GreenKoopaTroopaRunLeft.png"));
            koopaRunLeft1 = ImageIO.read(new File("GreenKoopaTroopaRunLeft1.png"));

            koopaRunRight = ImageIO.read(new File("GreenKoopaTroopaRunRight.png"));
            koopaRunRight1 = ImageIO.read(new File("GreenKoopaTroopaRunRight1.png"));

            koopaShell = ImageIO.read(new File("GreenKoopa TroopaShell.png"));
            koopaShell1 = ImageIO.read(new File("GreenKoopaTroopaShell1.png"));
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
            if (velocity.x < 0 && !inShell)
            {
                if (currentImage == koopaRunLeft)
                {
                    currentImage = koopaRunLeft1;
                } else if (currentImage == koopaRunLeft1)
                {
                    currentImage = koopaRunLeft;
                }
            } else if (velocity.x > 0 && !inShell)
            {
                if (currentImage == koopaRunRight )
                {
                    currentImage = koopaRunRight1;
                } else if (currentImage == koopaRunRight1)
                {
                    currentImage = koopaRunRight;
                }
            }
            lastAnimationProcessed = System.currentTimeMillis();
        }

        if (!hasHorizontalCollision())
        {
            position.x += (velocity.x * DT);
        } else
        {
            if (currentImage == koopaRunLeft || currentImage == koopaRunLeft1)
            {
                currentImage = koopaRunRight;
                lastAnimationProcessed = System.currentTimeMillis();

            } else if (currentImage == koopaRunRight || currentImage == koopaRunRight1)
            {
                currentImage = koopaRunLeft;
                lastAnimationProcessed = System.currentTimeMillis();
            }
        }

        if (!hasVerticalCollision())
        {
            position.y += (velocity.y * DT);
        }

        if (moving)
        {
            if (inShell)
            {
                velocity.addScaled(direction, (ACCELERATION * DT * 3));
                velocity.mult(DRAG);
                hasEnemyCollision();
            } else
            {
                velocity.addScaled(direction, (ACCELERATION * DT));
                velocity.mult(DRAG);
            }

        }


        if (falling)
        {
            applyGravity();
        }

        if (velocity.y > 50)
        {
            velocity.y = 0;
            velocity.x = velocity.x * -1;
            direction.mult(-1);
        }

        if (currentImage == koopaShell)
        {
            height = 30;
        }
    }

    private void hasEnemyCollision()
    {
        for (int i = 0; i < game.enemies.size(); i++)
        {
            Enemy e = game.enemies.get(i);
            if (getBounds().intersects(e.getBounds()) && e != this)
            {
                e.hit();
                game.enemies.remove(e);
            }
        }
    }

    private void applyGravity()
    {
        velocity.addScaled(jumpDirection, (ACCELERATION * DT * -GRAVITY));
    }

    public void hit()
    {
        if (currentImage == koopaRunLeft || currentImage == koopaRunLeft1)
        {
            position.y += 16;
            currentImage = koopaShell;
            velocity.x = 0;
            velocity.y = 0;
            moving = false;
            inShell = true;
        } else if (inShell)
        {
            if (!moving)
            {
                currentImage = koopaShell1;
                moving = true;
            } else
            {
                currentImage = koopaShell;
                velocity.x = 0;
                moving = false;
            }
        }
    }

    public boolean getInShell()
    {
        return inShell;
    }

    public void spinLeft()
    {
        currentImage = koopaShell1;
        moving = true;
        direction.set(-1, 0);
    }

    public void spinRight()
    {
        currentImage = koopaShell1;
        moving = true;
        direction.set(1, 0);
    }

    public boolean getMoving()
    {
        return moving;
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.scale(1, 1);
        //g.setColor(COLOR);
        //g.fillRect(0, 0, (int)width, (int)height);
        g.setTransform(at);
        g.setColor(Color.ORANGE);
        g.draw(getBounds());
        //g.draw(getBoundsRight());
        //g.draw(getBoundsLeft());
        //g.draw(getBoundsTop());
        //g.draw(getBoundsBottom());
        g.drawImage(currentImage, (int)position.x, (int)position.y, null);
    }
}
