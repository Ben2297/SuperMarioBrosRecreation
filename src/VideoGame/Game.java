package VideoGame;

import Utilities.JEasyFrame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static VideoGame.Constants.DELAY;

public class Game {
    private static final int N_INITIAL_ASTEROIDS = 3;
    private int currentAsteroids = 3;
    public List<GameObject> objects;
    private Player player;
    private List<Asteroid> asteroids;
    private Keys ctrl;
    private int score = 0;
    public int lives = 3;
    private Asteroid ranAsteroid;
    public int level = 1;
    private static BufferedImage myImage;

    // Sets up the game
    private Game() {
        objects = new ArrayList<>();
        asteroids = new ArrayList<>();
        ctrl = new Keys();
        player = new Player(ctrl);
        objects.add(player);
        for (int i = 0; i < N_INITIAL_ASTEROIDS; i++) {
            ranAsteroid = Asteroid.makeRandomAsteroid();
            while (ranAsteroid.position.x <= (player.position.x + player.radius) && ranAsteroid.position.y <= (player.position.y + player.radius)
                    && ranAsteroid.position.x >= (player.position.x) && ranAsteroid.position.y <= (player.position.y + player.radius)) {
                ranAsteroid = Asteroid.makeRandomAsteroid();
            }
            objects.add(ranAsteroid);
            asteroids.add(ranAsteroid);
        }
    }

    // The main method which calls the update method to keep the game running
    public static void main(String[] args) throws Exception {
        Game game = new Game();
        try {
            myImage = ImageIO.read(new File("space-background.jpg"));
        } catch (IOException e) {
            System.out.println("Incorrect file name");
        }
        View view = new View(game, myImage);
        new JEasyFrame(view, "Basic Game").addKeyListener(game.ctrl);
        while (true) {
            game.update();
            view.repaint();
            Thread.sleep(DELAY);
        }
    }

    // Updates the game, deletes dead objects, adds new asteroids, changes level, updates player lives, score, etc.
    private void update() {
        boolean asteroidsDead = true;
        for (GameObject o : objects) {
            for (GameObject g : objects) {
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
        for (GameObject o : objects) {
            if (o.dead && o.getClass() == Asteroid.class) {
                if (!player.dead) {
                    incScore();
                }
                asteroids.add((Asteroid) o);
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
        for (Asteroid a : asteroids) {
            for (Asteroid b : a.spawnedAsteroids) {
                if (b != null) {
                    alive.add(b);
                    b = null;
                }
            }

        }
        for (GameObject o : alive) {
            if (o.getClass() == Asteroid.class) {
                asteroidsDead = false;
            }
        }
        if (asteroidsDead) {
            currentAsteroids += 2;
            level += 1;
            for (int i = 0; i < currentAsteroids; i++) {
                ranAsteroid = Asteroid.makeRandomAsteroid();
                while (ranAsteroid.position.x <= (player.position.x + player.radius) && ranAsteroid.position.y <= (player.position.y + player.radius)
                        && ranAsteroid.position.x >= (player.position.x) && ranAsteroid.position.y <= (player.position.y + player.radius)) {
                    ranAsteroid = Asteroid.makeRandomAsteroid();
                }
                alive.add(ranAsteroid);
            }
        }
        if (player.bullet != null) {
            alive.add(player.bullet);
            player.bullet = null;
        }

        if (score != 0 && score % 10000 == 0) {
            lives += 1;
        }
        synchronized (Game.class) {
            objects.clear();
            objects.addAll(alive);
            asteroids.clear();
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
}
