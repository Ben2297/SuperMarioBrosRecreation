package VideoGame;

import Utilities.Vector2D;
import jdk.nashorn.internal.runtime.Debug;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Level {
    private int levelNumber;
    private List<Block> blocks;
    private String levelString;
    private FileReader fileReader;
    private Vector2D[][] grid;

    public Level (int levelNumber, Vector2D[][] grid)
    {
        blocks = new ArrayList<>();
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
        int x = 3;
        int y = 3;
        String tempString;

        try {
            while ((i = fileReader.read()) != -1)
            {
                System.out.print((char) i);
                if (i == 1)
                {
                    Vector2D blockPosition = new Vector2D();
                    blockPosition.set(grid[x][y]);
                    Block block = new Block(blockPosition, Color.red);
                    blocks.add(block);
                    x += 1;
                }
            }
        } catch (IOException ie)
        {

        }

    }

    public List<Block> getBlocks()
    {
        return blocks;
    }
}
