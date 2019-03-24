package VideoGame;

import Utilities.Vector2D;
import static VideoGame.Constants.FRAME_WIDTH;

public class Camera {
    public Vector2D position;

    public Camera()
    {
        position = new Vector2D();
        position.set(0, 120);
    }

    public void update(Player player)
    {
        boolean following;

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
    }
}
