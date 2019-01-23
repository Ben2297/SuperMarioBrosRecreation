package VideoGame;

import Utilities.Vector2D;

import java.awt.*;

import static VideoGame.Constants.FRAME_HEIGHT;
import static VideoGame.Constants.FRAME_WIDTH;

public class Camera {
    private Player player;

    public Vector2D position;

    public Camera()
    {
        //this.player = player;
        position = new Vector2D();
        position.set(0, 0);
    }

    public void update(Player player)
    {
        position.x = player.position.x - FRAME_WIDTH / 2;
        //position.y = player.position.y - FRAME_HEIGHT / 2;
    }
}
