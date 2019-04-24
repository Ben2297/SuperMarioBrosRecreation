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

    private static final double ACCELERATION = 2400;

    private static final double DRAG = 0.9;

    private static final double JUMP_STRENGTH = 120;

    private static final double GRAVITY = 2.5;

    private static final Color COLOR = Color.red;

    private Vector2D direction;
    private Vector2D jumpDirection;
    private boolean facingRight = true;
    private boolean falling;
    private boolean canJump;
    private boolean superMario = false;
    private boolean endSequence = false;
    private int endSequenceStage = 1;
    private boolean hit;

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

    private BufferedImage climbImage;
    private BufferedImage superClimbImage;

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
        dead = false;
        hit = false;
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

            climbImage = ImageIO.read(new File("MarioClimb.png"));
            superClimbImage = ImageIO.read(new File("SuperMarioClimb.png"));

        } catch (IOException ie)
        {
            System.out.println("Error reading sprite");
        }

        currentImage = runRightImage;
        height = 32;
        width = 32;
    }

    private void jump() {
        velocity.addScaled(jumpDirection, (ACCELERATION * DT * JUMP_STRENGTH));
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
        velocity.addScaled(jumpDirection, (ACCELERATION * DT * JUMP_STRENGTH));
        game.dieSound();
        hit = true;
    }

    public void update() {
        Action action;

        if (position.y > 800 || (position.y + height) < 0)
        {
            this.dead = true;
        }

        if (endSequence)
        {
            if (endSequenceStage == 1)
            {
                position.y += (velocity.y * DT);
                if ((position.y + height) > 680)
                {
                    velocity.y = 0;
                    endSequenceStage = 2;
                }
            } else if (endSequenceStage == 2)
            {
                direction.set(1, 0);
                velocity.addScaled(direction, (ACCELERATION * DT * 60));
                velocity.addScaled(jumpDirection, (ACCELERATION * DT * 120));
                if (superMario)
                {
                    currentImage = superJumpRightImage;
                } else
                {
                    currentImage = jumpRightImage;
                }
                endSequenceStage = 3;
            } else if (endSequenceStage == 3)
            {
                position.x += (velocity.x * DT);
                position.y += (velocity.y * DT);
                applyGravity();
                velocity.mult(DRAG);
                if ((position.y + height) > 720)
                {
                    position.y = (720 - height);
                    velocity.x = 0;
                    velocity.y = 0;
                    if (superMario)
                    {
                        currentImage = superRunRightImage;
                    } else
                    {
                        currentImage = runRightImage;
                    }
                    endSequenceStage = 4;
                }
            } else if (endSequenceStage == 4)
            {
                if (position.x > 8200)
                {
                    velocity.x = 0;
                    if (superMario)
                    {
                        currentImage = superRunRightImage;
                    } else
                    {
                        currentImage = runRightImage;
                    }

                    game.setGameWon();
                    this.dead = true;
                } else
                {
                    velocity.addScaled(direction, (ACCELERATION * DT));
                    position.x += (velocity.x * DT);
                    velocity.mult(DRAG);
                    if (superMario)
                    {
                        if (currentImage == superRunLeftImage || currentImage == superRunRightImage)
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
                    } else
                    {
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
                    }
                }
            }
        } else
        {
            if (!hit)
            {
                action = ctrl.action();
                velocity.addScaled(direction, (ACCELERATION * DT * action.move));
                velocity.mult(DRAG);

                hasEnemyCollision();
                hasPowerUpCollision();

                if (!hasHorizontalCollision())
                {
                    position.x += (velocity.x * DT);
                }

                if (!hasVerticalCollision())
                {
                    position.y += (velocity.y * DT);
                }

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
                        if (currentImage == runLeftImage || currentImage == runLeftImage1 || currentImage == runLeftImage2 ||
                                currentImage == runLeftImage3 || currentImage == runRightImage || currentImage == runRightImage1 ||
                                currentImage == runRightImage2 || currentImage == runRightImage3 ||
                                currentImage == superRunLeftImage || currentImage == superRunRightImage)
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
                        if (currentImage == runLeftImage || currentImage == runLeftImage1 || currentImage == runLeftImage2 ||
                                currentImage == runLeftImage3 || currentImage == runRightImage || currentImage == runRightImage1 ||
                                currentImage == runRightImage2 || currentImage == runRightImage3 ||
                                currentImage == superRunLeftImage || currentImage == superRunRightImage)
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
                    game.jumpSound();
                    canJump = false;
                }
            } else
            {
                position.x += (velocity.x * DT);
                position.y += (velocity.y * DT);
                velocity.mult(DRAG);
                applyGravity();
            }
            if (endSequence)
            {
                gameWin();
            }
        }
    }

    public boolean hasVerticalCollision()
    {
        for (int i = 0; i < game.scenery.size(); i++)
        {
            GameObject b = game.scenery.get(i);

            if (b.position.x > position.x - 400 && b.position.x < position.x + 400 &&
                    b.position.y > position.y - 400 && b.position.y < position.y + 400 && b.getClass() != Flag.class)
            {
                if (getBoundsBottom().intersects(b.getBoundsTop()) && velocity.y > 0)
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
                    position.y = b.position.y - (height - 1);
                    return true;
                } else
                {
                    falling = true;
                }

                if (getBounds().intersects(b.getBoundsBottom()) && velocity.y < 0)
                {
                    velocity.y = 0;
                    position.y = b.position.y + b.height;
                    if (b.getClass() == Block.class)
                    {
                        Block block = (Block)b;
                        if (block.getType() == 3 || block.getType() == 5 || superMario)
                        {
                            b.hit();
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasHorizontalCollision()
    {
        if ((position.x + velocity.x * DT) < 0)
        {
            return true;
        }

        for (int i = 0; i < game.scenery.size(); i++)
        {
            GameObject b = game.scenery.get(i);

            if (b.position.x > position.x - 400 && b.position.x < position.x + 400 &&
                    b.position.y > position.y - 400 && b.position.y < position.y + 400)
            {
                if (b.getClass() == Flag.class)
                {
                    if ((position.x + width) > (b.position.x + 50) && !endSequence)
                    {
                        velocity.x = 0;
                        endSequence = true;
                        return true;
                    }
                } else
                {
                    if (getBoundsLeft().intersects(b.getBoundsRight()) && velocity.x < 0)
                    {
                        velocity.x = 0;
                        return true;
                    }

                    if (getBoundsRight().intersects(b.getBoundsLeft()) && velocity.x > 0)
                    {
                        velocity.x = 0;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void hasEnemyCollision()
    {
        for (int i = 0; i < game.enemies.size(); i++)
        {
            Enemy e = game.enemies.get(i);

            if (e.position.x > position.x - 400 && e.position.x < position.x + 400 &&
                    e.position.y > position.y - 400 && e.position.y < position.y + 400)
            {
                if (getBoundsBottom().intersects(e.getBoundsTop()))
                {
                    if (e.getClass() == KoopaTroopa.class) {
                        KoopaTroopa koopaTroopa = (KoopaTroopa) e;
                        if (koopaTroopa.getInShell() && !koopaTroopa.getMoving())
                        {
                            if (position.x + (width / 2) < koopaTroopa.position.x + (koopaTroopa.width / 2))
                            {
                                position.y = e.position.y - height;
                                velocity.addScaled(jumpDirection, (ACCELERATION * DT * 60));
                                canJump = false;
                                koopaTroopa.spinRight();
                            } else {
                                position.y = e.position.y - height;
                                velocity.addScaled(jumpDirection, (ACCELERATION * DT * 60));
                                canJump = false;
                                koopaTroopa.spinLeft();
                            }
                        } else
                        {
                            position.y = e.position.y - height;
                            velocity.addScaled(jumpDirection, (ACCELERATION * DT * 60));
                            canJump = false;
                            koopaTroopa.hit();
                        }
                    } else
                    {
                        position.y = e.position.y - height;
                        velocity.addScaled(jumpDirection, (ACCELERATION * DT * 60));
                        canJump = false;
                        e.hit();
                        game.incrementScore(100);
                        if (e.dead)
                        {
                            game.enemies.remove(e);
                        }
                    }
                } else if (getBoundsLeft().intersects(e.getBoundsRight()))
                {
                    if (!superMario)
                    {
                        hit();
                    } else
                    {   superMario = false;
                        height = 32;
                        width = 34;
                        velocity.addScaled(jumpDirection, (ACCELERATION * DT * 60));
                        canJump = false;
                        game.powerDownSound();
                    }
                } else if (getBoundsRight().intersects(e.getBoundsLeft()))
                {
                    if (!superMario)
                    {
                        hit();
                    } else
                    {
                        superMario = false;
                        height = 32;
                        width = 34;
                        velocity.addScaled(jumpDirection, (ACCELERATION * DT * 60));
                        canJump = false;
                        game.powerDownSound();

                    }
                } else if (getBoundsTop().intersects(e.getBoundsBottom()))
                {
                    if (!superMario)
                    {
                        hit();
                    } else
                    {
                        superMario = false;
                        height = 32;
                        width = 34;
                        velocity.addScaled(jumpDirection, (ACCELERATION * DT * 60));
                        canJump = false;
                        game.powerDownSound();
                    }
                }
            }
        }
    }

    private void hasPowerUpCollision()
    {
        for (int i = 0; i < game.powerUps.size(); i++)
        {
            PowerUp pu = game.powerUps.get(i);
            if (getBounds().intersects(pu.getBounds()))
            {
                pu.hit();
                game.powerupSound();
                game.incrementScore(1000);

                if (!superMario)
                {
                    superMario = true;
                    height = 64;
                    width = 32;
                    position.y -= 32;
                }
            }
        }
    }

    private void applyGravity()
    {
        velocity.addScaled(jumpDirection, (ACCELERATION * DT * -GRAVITY));
    }

    private void gameWin()
    {
        velocity.x = 0;
        velocity.y = 150;
        if (superMario)
        {
            currentImage = superClimbImage;
        } else
        {
            currentImage = climbImage;
        }
        height = currentImage.getHeight();
        width = currentImage.getWidth();
        game.stopMusic();
        game.stageClearSound();
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.scale(1, 1);
        g.setColor(COLOR);
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
