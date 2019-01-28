package VideoGame;

import Utilities.Vector2D;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static VideoGame.Constants.*;

public class Player extends GameObject {
    private static final double MAG_ACC = 1700;

    private static final double DRAG = 0.9;

    private static final double JUMP_STRENGTH = 150;

    private static final double GRAVITY = 2;

    private static final Color COLOR = Color.red;

    private Vector2D direction;
    private Vector2D jumpDirection;
    private boolean facingRight = true;
    private boolean moving;
    private boolean falling;
    private boolean canJump;
    private boolean superMario = false;

    private BufferedImage runRightImage;
    private BufferedImage runRightImage1;
    private BufferedImage runRightImage2;
    private BufferedImage runRightImage3;
    private BufferedImage runLeftImage;
    private BufferedImage runLeftImage1;
    private BufferedImage runLeftImage2;
    private BufferedImage runLeftImage3;
    private BufferedImage jumpRightImage;
    private BufferedImage jumpLeftImage;
    private BufferedImage deadImage;

    private BufferedImage superRunRightImage;
    private BufferedImage superRunRightImage1;
    private BufferedImage superRunRightImage2;
    private BufferedImage superRunRightImage3;
    private BufferedImage superRunLeftImage;
    private BufferedImage superRunLeftImage1;
    private BufferedImage superRunLeftImage2;
    private BufferedImage superRunLeftImage3;
    private BufferedImage superJumpRightImage;
    private BufferedImage superJumpLeftImage;

    private VideoGame.Action action;
    private Game game;
    private Controller ctrl;
    private long lastAnimationProcessed = 0;

    public Player(Controller ctrl, Vector2D pos, Game game) {
        this.ctrl = ctrl;
        Vector2D vel = new Vector2D();
        vel.set(0, 0);
        direction = new Vector2D();
        direction.set(1, 0);
        jumpDirection = new Vector2D();
        jumpDirection.set(0, -1);
        position.set(pos);
        velocity.set(vel);
        moving = false;
        dead = false;
        falling = true;
        this.game = game;

        try
        {
            runRightImage = ImageIO.read(new File("MarioRunRight.png"));
            runRightImage1 = ImageIO.read(new File("MarioRunRight1.png"));
            runRightImage2 = ImageIO.read(new File("MarioRunRight2.png"));
            runRightImage3 = ImageIO.read(new File("MarioRunRight3.png"));

            runLeftImage = ImageIO.read(new File("MarioRunLeft.png"));
            runLeftImage1 = ImageIO.read(new File("MarioRunLeft1.png"));
            runLeftImage2 = ImageIO.read(new File("MarioRunLeft2.png"));
            runLeftImage3 = ImageIO.read(new File("MarioRunLeft3.png"));

            jumpRightImage = ImageIO.read(new File("MarioJumpRight.png"));
            jumpLeftImage = ImageIO.read(new File("MarioJumpLeft.png"));

            deadImage = ImageIO.read(new File("MarioDead.png"));

            superRunRightImage = ImageIO.read(new File("SuperMarioRunRight.png"));
            superRunRightImage1 = ImageIO.read(new File("SuperMarioRunRight1.png"));
            superRunRightImage2 = ImageIO.read(new File("SuperMarioRunRight2.png"));
            superRunRightImage3 = ImageIO.read(new File("SuperMarioRunRight3.png"));

            superRunLeftImage = ImageIO.read(new File("SuperMarioRunLeft.png"));
            superRunLeftImage1 = ImageIO.read(new File("SuperMarioRunLeft1.png"));
            superRunLeftImage2 = ImageIO.read(new File("SuperMarioRunLeft2.png"));
            superRunLeftImage3 = ImageIO.read(new File("SuperMarioRunLeft3.png"));

            superJumpRightImage = ImageIO.read(new File("SuperMarioJumpRight.png"));
            superJumpLeftImage = ImageIO.read(new File("SuperMarioJumpLeft.png"));

        } catch (IOException ie)
        {

        }

        currentImage = runRightImage;
        height = currentImage.getHeight();
        width = currentImage.getWidth();
    }

    private void jump() {
        velocity.addScaled(jumpDirection, (MAG_ACC * DT * JUMP_STRENGTH));
        if (currentImage == runRightImage)
        {
            currentImage = jumpRightImage;
        } else if (currentImage == runLeftImage)
        {
            currentImage = jumpLeftImage;
        } else if (currentImage == superRunRightImage)
        {
            currentImage = superJumpRightImage;
        } else if (currentImage == superRunLeftImage)
        {
            currentImage = superJumpLeftImage;
        }
    }

    public void hit()
    {
        currentImage = deadImage;
        velocity.addScaled(jumpDirection, (MAG_ACC * DT * JUMP_STRENGTH));
        super.hit();
    }

    public void update() {

        hasEnemyCollision();

        if (!dead)
        {
            if (!hasHorizontalCollision())
            {
                position.x += (velocity.x * DT);
                if (!hasVerticalCollision()) { position.y += (velocity.y * DT); }
            }
            hasPowerUpCollision();

            action = ctrl.action();
            velocity.addScaled(direction, (MAG_ACC * DT * action.move));
            velocity.mult(DRAG);

            if (falling)
            {
                applyGravity();
            }

            if (!superMario)
            {
                if (action.move == 1 && canJump)
                {
                    facingRight = true;
                    if (currentImage == runLeftImage || currentImage == runRightImage)
                    {
                        currentImage = runRightImage1;
                        lastAnimationProcessed = System.currentTimeMillis();
                    } else if (currentImage == runRightImage1 && System.currentTimeMillis() - lastAnimationProcessed > 120)
                    {
                        currentImage = runRightImage2;
                        lastAnimationProcessed = System.currentTimeMillis();
                    } else if (currentImage == runRightImage2 && System.currentTimeMillis() - lastAnimationProcessed > 120)
                    {
                        currentImage = runRightImage3;
                        lastAnimationProcessed = System.currentTimeMillis();
                    } else if (currentImage == runRightImage3 && System.currentTimeMillis() - lastAnimationProcessed > 120)
                    {
                        currentImage = runRightImage1;
                        lastAnimationProcessed = System.currentTimeMillis();
                    }
                } else if (action.move == -1 && canJump)
                {
                    facingRight = false;
                    if (currentImage == runLeftImage || currentImage == runRightImage)
                    {
                        currentImage = runLeftImage1;
                        lastAnimationProcessed = System.currentTimeMillis();
                    } else if (currentImage == runLeftImage1 && System.currentTimeMillis() - lastAnimationProcessed > 120)
                    {
                        currentImage = runLeftImage2;
                        lastAnimationProcessed = System.currentTimeMillis();
                    } else if (currentImage == runLeftImage2 && System.currentTimeMillis() - lastAnimationProcessed > 120)
                    {
                        currentImage = runLeftImage3;
                        lastAnimationProcessed = System.currentTimeMillis();
                    } else if (currentImage == runLeftImage3 && System.currentTimeMillis() - lastAnimationProcessed > 120)
                    {
                        currentImage = runLeftImage1;
                        lastAnimationProcessed = System.currentTimeMillis();
                    }
                } else if (action.move == 1 && !canJump)
                {
                    facingRight = true;
                    currentImage = jumpRightImage;
                } else if (action.move == -1 && !canJump)
                {
                    facingRight = false;
                    currentImage = jumpLeftImage;
                } else
                {
                    if (facingRight && canJump)
                    {
                        currentImage = runRightImage;
                    } else if (!facingRight && canJump)
                    {
                        currentImage = runLeftImage;
                    } else if (facingRight && !canJump)
                    {
                        currentImage = jumpRightImage;
                    } else if (!facingRight && !canJump)
                    {
                        currentImage = jumpLeftImage;
                    }
                }
            } else
            {
                if (action.move == 1 && canJump)
                {
                    facingRight = true;
                    if (currentImage == runLeftImage || currentImage == runRightImage ||
                            currentImage == superRunLeftImage | currentImage == superRunRightImage)
                    {
                        currentImage = superRunRightImage1;
                        lastAnimationProcessed = System.currentTimeMillis();
                    } else if (currentImage == superRunRightImage1 && System.currentTimeMillis() - lastAnimationProcessed > 120)
                    {
                        currentImage = superRunRightImage2;
                        lastAnimationProcessed = System.currentTimeMillis();
                    } else if (currentImage == superRunRightImage2 && System.currentTimeMillis() - lastAnimationProcessed > 120)
                    {
                        currentImage = superRunRightImage3;
                        lastAnimationProcessed = System.currentTimeMillis();
                    } else if (currentImage == superRunRightImage3 && System.currentTimeMillis() - lastAnimationProcessed > 120)
                    {
                        currentImage = superRunRightImage1;
                        lastAnimationProcessed = System.currentTimeMillis();
                    }
                } else if (action.move == -1 && canJump)
                {
                    facingRight = false;
                    if (currentImage == runLeftImage || currentImage == runRightImage ||
                            currentImage == superRunLeftImage | currentImage == superRunRightImage)
                    {
                        currentImage = superRunLeftImage1;
                        lastAnimationProcessed = System.currentTimeMillis();
                    } else if (currentImage == superRunLeftImage1 && System.currentTimeMillis() - lastAnimationProcessed > 120)
                    {
                        currentImage = superRunLeftImage2;
                        lastAnimationProcessed = System.currentTimeMillis();
                    } else if (currentImage == superRunLeftImage2 && System.currentTimeMillis() - lastAnimationProcessed > 120)
                    {
                        currentImage = superRunLeftImage3;
                        lastAnimationProcessed = System.currentTimeMillis();
                    } else if (currentImage == superRunLeftImage3 && System.currentTimeMillis() - lastAnimationProcessed > 120)
                    {
                        currentImage = superRunLeftImage1;
                        lastAnimationProcessed = System.currentTimeMillis();
                    }
                } else if (action.move == 1 && !canJump)
                {
                    facingRight = true;
                    currentImage = superJumpRightImage;
                } else if (action.move == -1 && !canJump)
                {
                    facingRight = false;
                    currentImage = superJumpLeftImage;
                } else
                {
                    if (facingRight && canJump)
                    {
                        currentImage = superRunRightImage;
                    } else if (!facingRight && canJump)
                    {
                        currentImage = superRunLeftImage;
                    } else if (facingRight && !canJump)
                    {
                        currentImage = superJumpRightImage;
                    } else if (!facingRight && !canJump)
                    {
                        currentImage = superJumpLeftImage;
                    }
                }
            }

            if (action.jump && canJump && !falling)
            {
                jump();
                canJump = false;
            }

            height = currentImage.getHeight();
            width = currentImage.getWidth();
        } else
        {
            position.x += (velocity.x * DT);
            position.y += (velocity.y * DT);
            velocity.mult(DRAG);
            applyGravity();
        }
    }

    public boolean hasVerticalCollision()
    {
        for (int i = 0; i < game.blocks.size(); i++)
        {
            Block b = game.blocks.get(i);
            if (getBoundsBottom().intersects(b.getBoundsTop()) && velocity.y > 0 && !hasHorizontalCollision())
            {
                canJump = true;
                falling = false;
                velocity.y = 0;
                if (currentImage == jumpRightImage)
                {
                    currentImage = runRightImage;
                } else if (currentImage == jumpLeftImage)
                {
                    currentImage = runLeftImage;
                } else if (currentImage == superJumpRightImage)
                {
                    currentImage = superRunRightImage;
                } else if (currentImage == superJumpLeftImage)
                {
                    currentImage = superRunLeftImage;
                }
                position.y = b.position.y - height;
                return true;
            } else
            {
                falling = true;
            }

            if (getBounds().intersects(b.getBoundsBottom()) && velocity.y < 0)
            {
                velocity.y = 0;
                return true;
            }
        }
        return false;
    }

    public boolean hasHorizontalCollision()
    {
        for (int i = 0; i < game.blocks.size(); i++)
        {
            Block b = game.blocks.get(i);
            if (getBoundsLeft().intersects(b.getBoundsRight()) && velocity.x < 0)
            {
                velocity.x = 0;
                position.x = b.position.x + b.width;
                return true;
            }

            if (getBoundsRight().intersects(b.getBoundsLeft()) && velocity.x > 0)
            {
                velocity.x = 0;
                position.x = b.position.x - width;
                return true;
            }
        }

        return false;
    }

    private void hasEnemyCollision()
    {
        for (int i = 0; i < game.enemies.size(); i++)
        {
            Enemy e = game.enemies.get(i);
            if (getBoundsBottom().intersects(e.getBoundsTop()))
            {
                jump();
                canJump = false;
            } else if (getBoundsLeft().intersects(e.getBoundsRight()))
            {
                if (!superMario)
                {
                    hit();
                } else
                {
                    superMario = false;
                    jump();
                    canJump = false;
                }

            } else if (getBoundsRight().intersects(e.getBoundsLeft()))
            {
                if (!superMario)
                {
                    hit();
                } else
                {
                    superMario = false;
                    jump();
                    canJump = false;
                }
            } else if (getBoundsTop().intersects(e.getBoundsBottom()))
            {
                if (!superMario)
                {
                    hit();
                } else
                {
                    superMario = false;
                    jump();
                    canJump = false;
                }
            }
        }
    }

    private void hasPowerUpCollision()
    {
        for (int i = 0; i < game.powerUps.size(); i++)
        {
            PowerUp p = game.powerUps.get(i);
            if (getBounds().intersects(p.getBounds()))
            {
                p.hit();
                //game.powerUps.remove(p);
                superMario = true;
                jump();
                canJump = false;
            }
        }
    }

    private void applyGravity()
    {
        velocity.addScaled(jumpDirection, (MAG_ACC * DT * -GRAVITY));
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.scale(1, 1);
        g.setColor(COLOR);
//        g.fillRect(0, 0, (int)width, (int)height);
        g.setTransform(at);
        g.setColor(Color.ORANGE);
//        g.draw(getBounds());
//        g.draw(getBoundsRight());
//        g.draw(getBoundsLeft());
//        g.draw(getBoundsTop());
//        g.draw(getBoundsBottom());
        g.drawImage(currentImage, (int)position.x, (int)position.y, null);
    }
}
