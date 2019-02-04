package VideoGame;

import Utilities.Vector2D;
import static VideoGame.Constants.FRAME_WIDTH;

public class Camera {
    private Player player;
    private boolean following = false;

    public Vector2D position;

    public Camera()
    {
        //this.player = player;
        position = new Vector2D();
        position.set(0, 0);
    }

    public void update(Player player)
    {
        if (player.position.x >= FRAME_WIDTH / 2)
        {
            following = true;
        } else
        {
            following = false;
        }

        if (following)
        {
            position.x = player.position.x - FRAME_WIDTH / 2;
        }

        //position.y = player.position.y - FRAME_HEIGHT / 2;
    }
}
