package VideoGame;

import Utilities.JEasyFrame;
import Utilities.Vector2D;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static VideoGame.Constants.*;

public class Game {
    public List<GameObject> objects;
    public List<Block> blocks;
    public List<GameObject> scenery;
    public List<Enemy> enemies;
    public List<PowerUp> powerUps;
    public List<GameObject> toBeAdded;
    private Player player;
    private Keys ctrl;
    private int score = 0;
    private int coins = 0;
    public int lives = 0;
    private static BufferedImage myImage;
    private Vector2D[][] grid;
    private Level level;
    private static Camera camera;

    private static JLabel nameText;
    private static JLabel scoreText;
    private static JLabel menuText;
    private static JLabel coinText;
    private static JLabel winText;

    public static Game game;
    private String scoreString;
    private String coinString;
    private static boolean gameStarted;
    private boolean gameWon;

    private static MediaPlayer musicPlayer;
    private MediaPlayer deathSound;
    private MediaPlayer jumpSound;
    private MediaPlayer coinSound;
    private MediaPlayer powerupSpawn;
    private MediaPlayer powerup;
    private MediaPlayer brickBreak;

    private Game() {

        gameStarted = false;
        camera = new Camera();
        camera.position.x = -1000;

        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("MarioFont.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException|FontFormatException e)
        {
            System.out.println("Font not found");
        }

        objects = new ArrayList<>();
        blocks = new ArrayList<>();
        scenery = new ArrayList<>();
        enemies = new ArrayList<>();
        powerUps = new ArrayList<>();
        toBeAdded = new ArrayList<>();
        ctrl = new Keys();
        grid = new Vector2D[GRID_WIDTH][GRID_HEIGHT];

        nameText = new JLabel();
        nameText.setText("Mario");
        nameText.setFont(new Font("Press Start K", Font.PLAIN, 24));
        nameText.setBounds((int)camera.position.x + 20, (int)camera.position.y - 30, 400, 100);

        scoreText = new JLabel();
        scoreString = String.format("%06d" , score);
        scoreText.setText("Score: " + scoreString);
        //scoreText.setFont(new Font("Serif", Font.PLAIN, 32));
        scoreText.setFont(new Font("Press Start K", Font.PLAIN, 24));
        scoreText.setBounds((int)camera.position.x + 20, (int)camera.position.y, 400, 100);

        menuText = new JLabel();
        menuText.setText("Start Game");
        menuText.setFont(new Font("Press Start K", Font.PLAIN, 24));
        menuText.setBounds(-695, 500, 400, 100);

        coinText = new JLabel();
        coinString = String.format("%02d" , coins);
        coinText.setText("x" + coinString);
        coinText.setFont(new Font("Press Start K", Font.PLAIN, 24));
        coinText.setBounds((int)camera.position.x + 430, (int)camera.position.y, 400, 100);

        winText = new JLabel();
        winText.setText("Congratulations!");
        winText.setFont(new Font("Press Start K", Font.PLAIN, 24));
        winText.setBounds((int)camera.position.x + 200, (int)camera.position.y + 100, 400, 400);
        winText.setVisible(false);

        constructGrid();
        Vector2D playerStartPosition = new Vector2D();
        playerStartPosition.set(grid[0][2]);
        player = new Player(ctrl, playerStartPosition, this);
        objects.add(player);
        level = new Level(1, grid, this);
        buildLevel();

        gameWon = false;

        try {
            myImage = ImageIO.read(new File("Background.png"));
        } catch (IOException e) {
            System.out.println("Incorrect file name");
        }
    }

    public static void main(String[] args) throws Exception {
        game = new Game();
        final JFXPanel fxPanel = new JFXPanel();
        String file = "Super Mario Bros. Theme Song.wav";
        Media hit = new Media(new File(file).toURI().toString());
        musicPlayer = new MediaPlayer(hit);
        musicPlayer.setOnEndOfMedia(() -> musicPlayer.seek(Duration.ZERO));
        musicPlayer.play();


        View view = new View(game, myImage, camera);
        view.setLayout(null);
        view.add(scoreText);
        view.add(nameText);
        view.add(menuText);
        view.add(coinText);
        view.add(winText);

        JEasyFrame jEasyFrame = new JEasyFrame(view, "CE301 Game");
        jEasyFrame.addKeyListener(game.ctrl);

        long remainder;
        final long interval = 1000 / 60;
        boolean gameIsRunning = true;

        while (!gameStarted)
        {
            if (game.ctrl.action().enter)
            {
                gameStarted = true;
                camera.position.x = 0;
            }
            view.repaint();
            remainder = interval - (System.currentTimeMillis() % interval);
            Thread.sleep(remainder);
        }

        while (gameIsRunning) {
            game.update();
            view.repaint();
            remainder = interval - (System.currentTimeMillis() % interval);
            Thread.sleep(remainder);
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
                //alive.add(player);
                //System.exit(0);
            }
        }

        if (score != 0 && score % 10000 == 0) {
            lives += 1;
        }

        synchronized (Game.class) {
            alive.addAll(toBeAdded);
            objects.clear();
            objects.addAll(alive);
            toBeAdded.clear();
        }

        scoreString = String.format("%06d" , score);
        scoreText.setText("Score: " + scoreString);
        scoreText.setBounds((int)(camera.position.x + 20 + 0.5), (int)camera.position.y, 400, 100);

        nameText.setBounds((int)(camera.position.x + 20 + 0.5), (int)camera.position.y - 30, 400, 100);

        coinString = String.format("%02d", coins);
        coinText.setText("x" + coinString);
        coinText.setBounds((int)camera.position.x + 430, (int)camera.position.y, 400, 100);

        if (player.dead)
        {
            resetGame();
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
            scenery.add(level.getBlocks().get(i));
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

    public void incrementScore(int toAdd)
    {
        score += toAdd;
    }

    public void coinCollected()
    {
        String file = "coin.wav";
        Media hit = new Media(new File(file).toURI().toString());
        coinSound = new MediaPlayer(hit);
        coinSound.play();
        coins += 1;
    }

    public int getScore()
    {
        return score;
    }

    public void jumpSound()
    {
        String file = "jump.wav";
        Media hit = new Media(new File(file).toURI().toString());
        jumpSound = new MediaPlayer(hit);
        jumpSound.play();
    }

    public void dieSound()
    {
        musicPlayer.stop();
        String file = "mariodie.wav";
        Media hit = new Media(new File(file).toURI().toString());
        deathSound = new MediaPlayer(hit);
        deathSound.play();
    }

    public void powerupSpawnSound()
    {
        String file = "powerupappears.wav";
        Media hit = new Media(new File(file).toURI().toString());
        powerupSpawn = new MediaPlayer(hit);
        powerupSpawn.play();
    }

    public void powerupSound()
    {
        String file = "powerup.wav";
        Media hit = new Media(new File(file).toURI().toString());
        powerup = new MediaPlayer(hit);
        powerup.play();
    }

    public void brickBreakSound()
    {
        String file = "breakblock.wav";
        Media hit = new Media(new File(file).toURI().toString());
        brickBreak = new MediaPlayer(hit);
        brickBreak.play();
    }

    public void setGameWon()
    {
        winText.setBounds((int)camera.position.x + 250, (int)camera.position.y - 100, 400, 400);
        gameWon = true;
        winText.setVisible(true);
    }

    private void resetGame()
    {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        objects.clear();
        scenery.clear();
        enemies.clear();
        powerUps.clear();
        winText.setVisible(false);
        gameWon = false;
        score = 0;
        coins = 0;
        level = new Level(1, grid, this);
        buildLevel();
        Vector2D playerStartPosition = new Vector2D();
        playerStartPosition.set(grid[0][2]);
        player = new Player(ctrl, playerStartPosition, this);
        objects.add(player);
        camera.position.x = 0;
        musicPlayer.play();
    }
}
