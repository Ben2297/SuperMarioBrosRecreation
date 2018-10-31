package VideoGame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Handles keyboard input from the user
public class Keys extends KeyAdapter implements Controller {
    private long lastPressProcessed = 0;

    private Action action;
    public Keys() {
        action = new Action();
    }

    public Action action() {
        return action;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                action.thrust = 1;
                break;
            case KeyEvent.VK_LEFT:
                action.move = -1;
                break;
            case KeyEvent.VK_RIGHT:
                action.move = +1;
                break;
            case KeyEvent.VK_SPACE:
                if(System.currentTimeMillis() - lastPressProcessed > 500) {
                    action.shoot = true;
                    lastPressProcessed = System.currentTimeMillis();
                    break;
                }
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                action.thrust = 0;
                break;
            case KeyEvent.VK_LEFT:
                action.move = 0;
                break;
            case KeyEvent.VK_RIGHT:
                action.move = 0;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = false;
                break;
        }
    }
}
