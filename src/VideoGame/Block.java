package VideoGame;

import Utilities.Vector2D;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static VideoGame.Constants.DT;

public class Block extends GameObject
{

    private static final double MAG_ACC = 300;

    private static final double JUMP_STRENGTH = 40;

    private static final double GRAVITY = 2.5;

    private Vector2D direction;
    private BufferedImage emptyBlockImage;
    private Vector2D jumpDirection;
    private double lowerBound;
    private int type;
    private Vector2D powerUpPosition;
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
        jumpDirection = new Vector2D();
        jumpDirection.set(0, -1);
        lowerBound = position.y;
        powerUpSpawned = false;
        powerUpPosition = new Vector2D();
        this.type = type;
        this.game = game;

        try
        {
            emptyBlockImage = ImageIO.read(new File("EmptyBlock.png"));
            if (type == 1)
            {
                currentImage = ImageIO.read(new File("Bricks.png"));
            } else if (type == 2)
            {
                currentImage = ImageIO.read(new File("Ground.png"));
            } else if (type == 3 || type == 5)
            {
                currentImage = ImageIO.read(new File("Question.png"));
            } else if (type == 4)
            {
                currentImage = ImageIO.read(new File("Block.png"));
            }

        } catch (IOException ie)
        {
            System.out.println("Image file not found");
        }

        height = currentImage.getHeight();
        width = currentImage.getWidth();
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

        if (type == 3 && !powerUpSpawned) {
            currentImage = emptyBlockImage;
            velocity.addScaled(jumpDirection, (MAG_ACC * DT * JUMP_STRENGTH));
            powerUpPosition.set(this.position);
            powerUpPosition.x += 4;
            PowerUp powerUp = new PowerUp(powerUpPosition, game);
            game.powerUps.add(powerUp);
            game.toBeAdded.add(powerUp);
            game.powerupSpawnSound();
            powerUpSpawned = true;
        } else if (type == 5 && !powerUpSpawned)
        {
            currentImage = emptyBlockImage;
            velocity.addScaled(jumpDirection, (MAG_ACC * DT * JUMP_STRENGTH));
            powerUpPosition.set(this.position);
            powerUpPosition.x += 12;
            Coin coin = new Coin(powerUpPosition, game);
            game.toBeAdded.add(coin);
            game.coinCollected();
            powerUpSpawned = true;
        } else if (type == 1)
        {
            dead = true;
            game.scenery.remove(this);
        }
    }

    public int getType()
    {
        return type;
    }

    public void draw(Graphics2D g)
    {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.scale(1, 1);
        //g.fillRect(0, 0, (int)width, (int)height);
        g.setTransform(at);
        g.drawImage(currentImage, (int)position.x, (int)position.y, null);
        //g.draw(getBounds());
//        g.draw(getBoundsRight());
//        g.draw(getBoundsLeft());
//        g.draw(getBoundsTop());
//        g.draw(getBoundsBottom());
    }
}
