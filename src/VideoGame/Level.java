package VideoGame;

import Utilities.Vector2D;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static VideoGame.Constants.*;

public class Level {
    private int levelNumber;
    private List<Block> blocks;
    private List<Enemy> enemies;
    private List<PowerUp> powerUps;
    private String levelString;
    private FileReader fileReader;
    private Vector2D[][] grid;
    Game game;

    public Level (int levelNumber, Vector2D[][] grid, Game game)
    {
        this.game = game;
        blocks = new ArrayList<>();
        enemies = new ArrayList<>();
        powerUps = new ArrayList<>();
        this.levelNumber = levelNumber;
        String levelNumString = Integer.toString(levelNumber);
        levelString = "Level" + levelNumString + ".txt";
        File file = new File(levelString);
        try
        {
            fileReader = new FileReader(file);
            System.out.println("Success");
        } catch (IOException ie)
        {
            System.out.println("File not found");
        }

        this.grid = grid;
        levelLoader();
    }

    private void levelLoader()
    {
        int i;
        int x = 0;
        int y = GRID_HEIGHT - 1;
        Vector2D blockPosition = new Vector2D();
        Vector2D enemyPosition = new Vector2D();
        Vector2D powerUpPosition = new Vector2D();

        try {
            while ((i = fileReader.read()) != -1)
            {
                System.out.print((char) i);
                if (Character.isDigit((char)i))
                {
                    if ((char)i == '1')
                    {
                        blockPosition.set(grid[x][y]);
                        Block block = new Block(blockPosition);
                        blocks.add(block);
                    } else if ((char)i == '2')
                    {
                        enemyPosition.set(grid[x][y]);
                        Goomba goomba = new Goomba(enemyPosition, game);
                        enemies.add(goomba);
                    } else if ((char)i == '3')
                    {
                        powerUpPosition.set(grid[x][y]);
                        PowerUp powerUp = new PowerUp(powerUpPosition, game);
                        powerUps.add(powerUp);
                    }
                    if (x < 24)
                    {
                        x += 1;
                    } else
                    {
                        if (y > 0)
                        {
                            y -= 1;
                            x = 0;
                        }
                    }
                }


            }
        } catch (IOException ie)
        {

        }
        try
        {
            fileReader.close();
        } catch (IOException ie)
        {

        }

    }

    public List<Block> getBlocks()
    {
        return blocks;
    }

    public List<Enemy> getEnemies()
    {
        return enemies;
    }

    public List<PowerUp> getPowerUps() { return powerUps; }
}
