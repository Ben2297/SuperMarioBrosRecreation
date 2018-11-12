package VideoGame;

import Utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Block extends GameObject {
    private static final int RADIUS = 15;

    private static final Color COLOR = Color.red;

    public Block(Vector2D pos)
    {
        position = pos;
        radius = RADIUS;
        //Vector2D vel = new Vector2D();
        //vel.set(0, 0);
        velocity = new Vector2D();
        velocity.set(0, 0);
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.scale(1, 1);
        g.setColor(COLOR);
        g.fillRect(0, 0, (int)radius * 2, (int)radius * 2);
        g.setTransform(at);
    }
}
