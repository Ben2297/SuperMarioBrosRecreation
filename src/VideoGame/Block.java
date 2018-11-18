package VideoGame;

import Utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Block extends GameObject {
    private static final int RADIUS = 20;

    private static final Color COLOR = Color.red;

    private Vector2D direction;

    private Color blockColor;

    public Block(Vector2D pos, Color col)
    {
        direction = new Vector2D();
        direction.set(1, 0);
        position = new Vector2D();
        position.set(pos);
        velocity = new Vector2D();
        velocity.set(0, 0);
        radius = RADIUS;
        blockColor = col;
    }

    public void draw(Graphics2D g)
    {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        g.scale(1, 1);
        g.setColor(blockColor);
        g.fillRect(0, 0, (int)radius * 2, (int)radius * 2);
        g.setTransform(at);
    }
}
