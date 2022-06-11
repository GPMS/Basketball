
package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class Interface extends JFrame implements KeyListener {
    
    Game game;
    
    Interface(Game game)
    {
        super("Basketball");
        
        this.add(game);
        
        this.setSize(640, 480);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.addKeyListener(this);
        this.game = game;
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        switch(key) {
            case KeyEvent.VK_UP:
                game.p1.up = true;
                break;
            case KeyEvent.VK_DOWN:
                game.p1.down = true;
                break;
            case KeyEvent.VK_RIGHT:
                game.p1.right = true;
                break;
            case KeyEvent.VK_LEFT:
                game.p1.left = true;
                break;
            case KeyEvent.VK_ENTER:
                if (game.p1.holding) {
                    game.p1.holding = false;
                    game.p1.aiming = true;
                }
                break;
            case KeyEvent.VK_W:
                game.p2.up = true;
                break;
            case KeyEvent.VK_S:
                game.p2.down = true;
                break;
            case KeyEvent.VK_D:
                game.p2.right = true;
                break;
            case KeyEvent.VK_A:
                game.p2.left = true;
                break;
            case KeyEvent.VK_SPACE:
                if (game.p2.holding) {
                    game.p2.holding = false;
                    game.p2.aiming = true;
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();
        switch(key) {
            case KeyEvent.VK_UP:
                game.p1.up = false;
                break;
            case KeyEvent.VK_DOWN:
                game.p1.down = false;
                break;
            case KeyEvent.VK_RIGHT:
                game.p1.right = false;
                break;
            case KeyEvent.VK_LEFT:
                game.p1.left = false;
                break;
            case KeyEvent.VK_ENTER:
                if (game.p1.aiming) {
                    game.p1.aiming = false;
                    game.ball.thrown = true;
                    game.p1.holding = false;
                    game.ball.vx = -10;
                    game.ball.vy = -15;
                }
                break;
            case KeyEvent.VK_W:
                game.p2.up = false;
                break;
            case KeyEvent.VK_S:
                game.p2.down = false;
                break;
            case KeyEvent.VK_D:
                game.p2.right = false;
                break;
            case KeyEvent.VK_A:
                game.p2.left = false;
                break;
            case KeyEvent.VK_SPACE:
                if (game.p2.aiming) {
                    game.p2.aiming = false;
                    game.ball.thrown = true;
                    game.p2.holding = false;
                    game.ball.vx = 10;
                    game.ball.vy = -15;
                }
                break;
        }
    }
}
