package VideoGame;

import Utilities.JEasyFrame;
import Utilities.Vector2D;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static VideoGame.Constants.*;

public class Game {
    public List<GameObject> objects;
    public List<Block> blocks;
    public List<Enemy> enemies;
    public List<PowerUp> powerUps;
    public List<GameObject> toBeAdded;
    private Player player;
    private Keys ctrl;
    private int score = 0;
    public int lives = 0;
    public int levelNumber = 1;
    private static BufferedImage myImage;
    private Vector2D[][] grid;
    private Level level;
    private static Camera camera;

    private Game() {
        objects = new ArrayList<>();
        blocks = new ArrayList<>();
        enemies = new ArrayList<>();
        powerUps = new ArrayList<>();
        toBeAdded = new ArrayList<>();
        ctrl = new Keys();
        grid = new Vector2D[GRID_WIDTH][GRID_HEIGHT];
        constructGrid();
        Vector2D playerStartPosition = new Vector2D();
        playerStartPosition.set(grid[0][2]);
        player = new Player(ctrl, playerStartPosition, this);
        objects.add(player);
        level = new Level(1, grid, this);
        buildLevel();
        //camera = new Camera(player);
    }

    public static void main(String[] args) throws Exception {
        Game game = new Game();
        camera = new Camera();

        try {
            myImage = ImageIO.read(new File("Background.png"));
        } catch (IOException e) {
            System.out.println("Incorrect file name");
        }
        View view = new View(game, myImage, camera);
        new JEasyFrame(view, "CE301 Game").addKeyListener(game.ctrl);

        long remainder;
        final int interval = 1000 / 60;
        boolean gameIsRunning = true;

        while (gameIsRunning) {
            game.update();
            view.repaint();
            remainder = interval - (System.currentTimeMillis() % interval);
            Thread.sleep(remainder);
            System.out.println(remainder);
        }
    }

    private void update() {
        List<GameObject> alive = new ArrayList<>();

        for (GameObject o : objects)
        {
            o.update();
            if (!o.dead) {
                alive.add(o);
            }
        }

        camera.update(player);

        powerUps.clear();

        for (GameObject o : objects) {
            if (!o.dead && o.getClass() == PowerUp.class) {
                powerUps.add((PowerUp) o);
            }
        }

        if (player.dead) {
            if (lives > 0) {
                lives -= 1;
                player.dead = false;
                alive.add(player);
            } else {
                alive.add(player);
                //System.exit(0);
            }
        }

        if (score != 0 && score % 10000 == 0) {
            lives += 1;
        }

//        for (GameObject o : toBeAdded)
//        {
//            alive.add(o);
//        }

        alive.addAll(toBeAdded);

        synchronized (Game.class) {
            objects.clear();
            objects.addAll(alive);
            toBeAdded.clear();
        }
    }

    private void constructGrid() {
        double xValue = FRAME_WIDTH / 25 ;
        double yValue = FRAME_HEIGHT / GRID_HEIGHT;
        double xTotal = 0;
        double yTotal = 0;

        for (int x = 0; x < (GRID_WIDTH); x++)
        {
            for (int y = 0; y < GRID_HEIGHT; y++)
            {
                grid[x][y] = new Vector2D();
            }
        }

        for (int x = 0; x <= GRID_WIDTH - 1; x++)
        {
            for (int y = GRID_HEIGHT - 1; y >= 0; y--)
            {
                grid[x][y].set(xTotal, yTotal);
                System.out.println(xTotal);
                System.out.println(yTotal);
                yTotal += yValue;
            }
            xTotal += xValue;
            yTotal = 0;
        }
    }

    private void buildLevel()
    {
        for (int i = 0; i < level.getBlocks().size(); i++)
        {
            objects.add(level.getBlocks().get(i));
            blocks.add(level.getBlocks().get(i));
        }

        for (int i = 0; i < level.getEnemies().size(); i++)
        {
            objects.add(level.getEnemies().get(i));
            enemies.add(level.getEnemies().get(i));
        }

        for (int i = 0; i < level.getPowerUps().size(); i++)
        {
            objects.add(level.getPowerUps().get(i));
            powerUps.add(level.getPowerUps().get(i));
        }
    }
}
