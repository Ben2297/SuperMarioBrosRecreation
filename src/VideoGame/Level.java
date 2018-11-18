package VideoGame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Level {
    private int levelNumber;
    private Block[][] levelLayout;
    BufferedReader bufferedReader;
    private String levelString;

    public Level (int levelNumber)
    {
        this.levelNumber = levelNumber;
        String levelNumString = Integer.toString(levelNumber);
        levelString = "Level" + levelNumString + ".txt";
        File file = new File("OneDrive\\Documents\\Uni Work\\CE301\\CE301 Project\\capstone_project\\" + levelString);
        try
        {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (IOException ie)
        {
            System.out.println("File not found");
        }

        levelLoader();
    }

    private void levelLoader()
    {

    }
}
