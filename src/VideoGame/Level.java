package VideoGame;

import Utilities.Vector2D;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static VideoGame.Constants.*;

public class Level {
    private List<GameObject> scenery;
    private List<Enemy> enemies;
    private List<PowerUp> powerUps;
    private String levelString;
    private FileReader fileReader;
    private Vector2D[][] grid;
    Game game;

    public Level (int levelNumber, Vector2D[][] grid, Game game)
    {
        this.game = game;
        scenery = new ArrayList<>();
        enemies = new ArrayList<>();
        powerUps = new ArrayList<>();
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
        Vector2D objectPosition = new Vector2D();

        try {
            while ((i = fileReader.read()) != -1)
            {
                System.out.print((char) i);
                if (Character.isDigit((char)i) | Character.isLetter((char)i))
                {
                    if ((char)i == '1') {
                        objectPosition.set(grid[x][y]);
                        Block block = new Block(objectPosition, 1, game);
                        scenery.add(block);
                    } else if ((char)i == '2')
                    {
                        objectPosition.set(grid[x][y]);
                        Block block = new Block(objectPosition, 2, game);
                        scenery.add(block);
                    } else if ((char)i == '3')
                    {
                        objectPosition.set(grid[x][y]);
                        Block block = new Block(objectPosition, 3, game);
                        scenery.add(block);
                    } else if ((char)i == '4')
                    {
                        objectPosition.set(grid[x][y]);
                        Goomba goomba = new Goomba(objectPosition, game);
                        enemies.add(goomba);
                    } else if ((char)i == '5')
                    {
                        objectPosition.set(grid[x][y]);
                        PowerUp powerUp = new PowerUp(objectPosition, game);
                        powerUps.add(powerUp);
                    } else if ((char)i == '6')
                    {
                        objectPosition.set(grid[x][y]);
                        KoopaTroopa koopaTroopa = new KoopaTroopa(objectPosition, game);
                        enemies.add(koopaTroopa);
                    } else if ((char)i == '7')
                    {
                        objectPosition.set(grid[x][y]);
                        Pipe pipe = new Pipe(objectPosition, game, 1);
                        scenery.add(pipe);
                    } else if ((char)i == '8')
                    {
                        objectPosition.set(grid[x][y]);
                        Pipe pipe = new Pipe(objectPosition, game, 2);
                        scenery.add(pipe);
                    } else if ((char)i == '9')
                    {
                        objectPosition.set(grid[x][y]);
                        Pipe pipe = new Pipe(objectPosition, game, 3);
                        scenery.add(pipe);
                    } else if ((char)i == 'A')
                    {
                        objectPosition.set(grid[x][y]);
                        Block block = new Block(objectPosition, 4, game);
                        scenery.add(block);
                    } else if ((char)i == 'B')
                    {
                        objectPosition.set(grid[x][y]);
                        Block block = new Block(objectPosition, 5, game);
                        scenery.add(block);
                    }

                    if (x < (GRID_WIDTH - 1))
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

    public List<GameObject> getBlocks()
    {
        return scenery;
    }

    public List<Enemy> getEnemies()
    {
        return enemies;
    }

    public List<PowerUp> getPowerUps() { return powerUps; }
}
