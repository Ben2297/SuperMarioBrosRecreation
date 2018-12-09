package VideoGame;

import Utilities.JEasyFrame;
import Utilities.Vector2D;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static VideoGame.Constants.*;

public class Game {
    public List<GameObject> objects;
    private Player player;
    private Keys ctrl;
    private int score = 0;
    public int lives = 3;
    public int levelNumber = 1;
    private static BufferedImage myImage;
    private Vector2D[][] grid;
    private Level level;

    // Sets up the game
    private Game() {
        objects = new ArrayList<>();
        ctrl = new Keys();
        grid = new Vector2D[GRID_WIDTH][GRID_HEIGHT];
        constructGrid();
        Vector2D playerStartPosition = new Vector2D();
        playerStartPosition.set(grid[0][0]);
        player = new Player(ctrl, playerStartPosition);
        objects.add(player);
        Vector2D blockPosition = new Vector2D();
        blockPosition.set(grid[7][0]);
        Block block = new Block(blockPosition, Color.red);
        //objects.add(block);
        blockPosition.set(grid[10][4]);
        Block block2 = new Block(blockPosition, Color.red);
        //objects.add(block2);
        blockPosition.set(grid[24][19]);
        Block block3 = new Block(blockPosition, Color.red);
        //objects.add(block3);
        level = new Level(1, grid);
        buildLevel();
    }

    // The main method which calls the update method to keep the game running
    public static void main(String[] args) throws Exception {
        Game game = new Game();

        try {
            myImage = ImageIO.read(new File("space-background.jpg"));
        } catch (IOException e) {
            //System.out.println("Incorrect file name");
        }
        View view = new View(game, myImage);
        new JEasyFrame(view, "CE301 Game").addKeyListener(game.ctrl);

        final int FPS = 60;
        final int SKIP_TICKS = 1000 / FPS;
        final int MAX_FRAMESKIP = 10;
        double next_game_tick = System.currentTimeMillis();
        int loops;

        boolean game_is_running = true;

        while (game_is_running) {
            loops = 0;
            while( System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) {
                game.update();
                next_game_tick += SKIP_TICKS;
                loops++;
            }
            view.repaint();
        }
    }

    // Updates the game, deletes dead objects, adds new enemies, changes level, updates player lives, score, etc.
    private void update() {
        for (GameObject o : objects) {
            o.resetCollisions();
        }
        for (GameObject o : objects)
        {
            for (GameObject g : objects)
            {
                o.collisionHandling(g);
            }
        }
        List<GameObject> alive = new ArrayList<>();
        for (GameObject o : objects) {
            o.update();
            if (!o.dead) {
                alive.add(o);
            }
        }
        if (player.dead) {
            if (lives > 0) {
                lives -= 1;
                player.dead = false;
                alive.add(player);
            } else {
                System.exit(0);
            }
        }
        if (score != 0 && score % 10000 == 0) {
            lives += 1;
        }
        synchronized (Game.class) {
            objects.clear();
            objects.addAll(alive);
        }
    }

    // Increases score
    private void incScore() {
        score += 100;
    }

    // Returns the current score
    public int getScore() {
        return score;
    }

    private void constructGrid() {
        double xValue = FRAME_WIDTH / GRID_WIDTH ;
        double yValue = FRAME_HEIGHT / GRID_HEIGHT;
        double xTotal = 40;
        double yTotal = 0;

        for (int x = 0; x < GRID_WIDTH; x++)
        {
            for (int y = 0; y < GRID_HEIGHT; y++)
            {
                grid[x][y] = new Vector2D();
            }
        }

        for (int x = 0; x <= GRID_WIDTH - 1; x++)
        {
            //xTotal += xValue;
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
        }
    }
}
