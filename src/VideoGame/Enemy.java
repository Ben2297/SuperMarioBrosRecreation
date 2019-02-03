package VideoGame;

import Utilities.Vector2D;
import java.awt.*;

public abstract class Enemy extends GameObject{

    public long lastAnimationProcessed = 0;
    public Vector2D direction;
    public Vector2D jumpDirection;
    public boolean falling;
    Game game;

    public Enemy()
    {
        super();

    }

    public void update() { super.update(); }

    public boolean hasVerticalCollision()
    {
        for (int i = 0; i < game.blocks.size(); i++)
        {
            Block b = game.blocks.get(i);
            if (getBoundsBottom().intersects(b.getBoundsTop()) && velocity.y > 0 && !hasHorizontalCollision())
            {
                falling = false;
                velocity.y = 0;
                //position.y = b.position.y - height;
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

    public abstract void draw(Graphics2D g);
}
