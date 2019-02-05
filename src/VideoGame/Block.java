package VideoGame;

import Utilities.Vector2D;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static VideoGame.Constants.DT;

public class Block extends GameObject {
    private static final int HEIGHT = 40;

    private static final int WIDTH = 40;

    private static final double MAG_ACC = 300;

    private static final double JUMP_STRENGTH = 40;

    private static final double GRAVITY = 2.5;

    private Vector2D direction;
    private BufferedImage image;
    private BufferedImage emptyBlockImage;
    private Vector2D jumpDirection;
    private double lowerBound;
    private int type;
    private Vector2D powerUpPosition = new Vector2D();
    private boolean powerUpSpawned;
    Game game;

    public Block(Vector2D pos, int type, Game game)
    {
        direction = new Vector2D();
        direction.set(1, 0);
        position = new Vector2D();
        position.set(pos);
        velocity = new Vector2D();
        velocity.set(0, 0);
        height = HEIGHT;
        width = WIDTH;
        jumpDirection = new Vector2D();
        jumpDirection.set(0, -1);
        lowerBound = position.y;
        powerUpSpawned = false;
        this.type = type;
        this.game = game;

        try
        {

            emptyBlockImage = ImageIO.read(new File("EmptyBlock.png"));
            if (type == 1)
            {
                image = ImageIO.read(new File("Bricks.png"));
            } else if (type == 2)
            {
                image = ImageIO.read(new File("Ground.png"));
            } else if (type == 3)
            {
                image = ImageIO.read(new File("Question.png"));

            }

        } catch (IOException ie)
        {
            System.out.println("Image file not found");
        }
    }

    public void update()
    {
        if (position.y < lowerBound)
        {
            applyGravity();
        } else if (position.y > lowerBound)
        {
            velocity.y = 0;
        }

        super.update();
    }

    private void applyGravity()
    {
        velocity.addScaled(jumpDirection, (MAG_ACC * DT * -GRAVITY));
    }

    public void hit()
    {

        if (type == 3 && !powerUpSpawned)
        {
            image = emptyBlockImage;
            velocity.addScaled(jumpDirection, (MAG_ACC * DT * JUMP_STRENGTH));
            powerUpPosition.set(this.position);
            powerUpPosition.x += 4;
            PowerUp powerUp = new PowerUp(powerUpPosition, game);
            game.powerUps.add(powerUp);
            game.toBeAdded.add(powerUp);
            powerUpSpawned = true;
        } else if (type == 1)
        {
            dead = true;
            game.blocks.remove(this);
        }
    }

    public void draw(Graphics2D g)
    {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.scale(1, 1);
        //g.fillRect(0, 0, (int)width, (int)height);
        g.setTransform(at);
        g.drawImage(image, (int)position.x, (int)position.y, null);
        //g.draw(getBounds());
//        g.draw(getBoundsRight());
//        g.draw(getBoundsLeft());
//        g.draw(getBoundsTop());
//        g.draw(getBoundsBottom());
    }
}
