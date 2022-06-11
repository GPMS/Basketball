package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

public class Game extends JComponent implements Runnable {
    
    public Player p1;
    public Player p2;
    public Ball ball;
    private int floor;
    private int frames;
    private int minutes;
    private int seconds;
    private boolean running;
    private boolean end;
    private String sc1;
    private String sc2;
    private String countDown;
    private String endMessage;
    private final int width;
    private final int height;
    private final int boardSize;
    private final Interface frame;
    private final Rectangle col1basketIn;
    private final Rectangle col2basketIn;
    private final Rectangle col1basketOut;
    private final Rectangle col2basketOut;
    private final Rectangle col1pole;
    private final Rectangle col2pole;
    private final Image aiming1;
    private final Image aiming2;
    private Image normal1;
    private Image normal2;
    
    Game()
    {
        running = true;
        frame = new Interface(this);
        
        width = 640; height = 480;
        boardSize = 159;
        floor = 148+boardSize;
        
        p1 = new Player((width-31)/2+55, floor-64);
        normal1 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/normalL1.png"));
        aiming1 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/aiming1.png"));
        
        p2 = new Player((width-31)/2-55, floor-64);
        normal2 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/normalR2.png"));
        aiming2 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/aiming2.png"));
        
        ball = new Ball( (width-31)/2, 100);
        col1basketIn = new Rectangle(78, 180, 31, 2);
        col1basketOut = new Rectangle(78, 182, 33, 14);
        col1pole = new Rectangle(64, 148, 14, boardSize);
        col2basketIn = new Rectangle(526, 180, 33, 2);
        col2basketOut = new Rectangle(526, 182, 33, 14);
        col2pole = new Rectangle(559, 148, 14, boardSize);
        
        frames = 0;
        minutes = 1;
        seconds = 0;
        
        end = false;
        
        new Thread(this).start();
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Color purple = new Color(125, 48, 173);
        Color blue = new Color(101, 111, 228);
        Color green = new Color(158, 208, 101);
        Color brown = new Color(198, 108, 58);
        
        //Draw black background
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        
        //Draw scores and Countdown
        //Score 2
        g.setColor(green);
        g.setFont( new Font("ARIAL", Font.BOLD, 60) );
        this.sc2 = (p2.score < 10? "0" : "") + p2.score;
        g.drawString(sc2, 50, 90);
        //Countdown
        g.setColor(blue);
        g.setFont( new Font("ARIAL", Font.BOLD, 60) );
        this.countDown = "0" + minutes + ":" + (seconds<10? "0"+seconds : seconds);
        g.drawString(countDown, 190, 90);
        //Score 1
        g.setColor(purple);
        g.setFont( new Font("ARIAL", Font.BOLD, 60) );
        this.sc1 = (p1.score < 10? "0" : "") + p1.score;
        g.drawString(sc1, 450, 90);
        
        //Draw court
        g.setColor(brown);
        g.fillRect(31, 216, width-31, 181);
        
        //Draw backboards and baskets
        //Left
        g.setColor(blue);
        g.fillRect(64, 148, 14, boardSize);
        g.fillRect(78, 180, 33, 16);
        g.setColor(green);
        g.drawLine(125, 217, 36, 400);
        //Right
        g.setColor(blue);
        g.fillRect(559, 148, 14, boardSize);
        g.fillRect(526, 180, 33, 16);
        g.setColor(purple);
        g.drawLine(516, 217, 604, 400);
        
        //Draw players
        //2
        if (p2.aiming)
            g.drawImage(aiming2, p2.x, p2.y, null);
        else
            g.drawImage(normal2, p2.x, p2.y, null);
        //1
        if (p1.aiming)
            g.drawImage(aiming1, p1.x, p1.y, null);
        else
            g.drawImage(normal1, p1.x, p1.y, null);
        
        //Draw ball
        g.setColor(blue);
        g.fillRect(ball.x, ball.y, ball.size, ball.size);
        
        if (end) {
            g.setColor(Color.WHITE);
            g.setFont( new Font("ARIAL", Font.BOLD, 40) );
            if (p1.score > p2.score)
                endMessage = "Player 1 wins!\n";
            else if (p1.score < p2.score)
                endMessage = "Player 2 wins!\n";
            else
                endMessage = "Draw!\n";
            g.drawString(endMessage, 100, 150);
        }
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    @Override
    public void run()
    {
        while(running) {
            try {
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
            long currentTime = System.nanoTime();
            
            frames++;
            
            //Countdown
            if (frames == 25) {
                frames = 0;
                if (seconds == 0) {
                    minutes--;
                    seconds = 59;
                } else
                    seconds--;
                
                // Time's up!
                if (minutes == 0 && seconds == 0) {
                    //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    end = true;
                    running = false;
                }
            }
            
            if (ball.vy > 20f)
                ball.vy = 20f;
            if (ball.vy < -20f)
                ball.vy = -20f;
            ball.x += (int)ball.vx;
            ball.y += (int)ball.vy;
            
            p1.handleMovement();
            p2.handleMovement();
            
            AffineTransform tx;
            if (!p1.holding && ball.x > p1.x) {
                normal1 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/normalR1.png"));
                p1.holdBox.setLocation(p1.x+10, p1.y+p1.size/2);
            } else {
                normal1 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/normalL1.png"));
                p1.holdBox.setLocation(p1.x-5, p1.y+p1.size/2);
            }
            
            if (!p2.holding && ball.x < p2.x) {
                normal2 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/normalL2.png"));
                p2.holdBox.setLocation(p2.x-5, p2.y+p2.size/2);
            } else {
                normal2 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/normalR2.png"));
                p2.holdBox.setLocation(p2.x+20, p2.y+p2.size/2);
            }
            
            p1.aimBox.setLocation(p1.x-5, p1.y-ball.size);
            p2.aimBox.setLocation(p2.x+18, p2.y-ball.size);
            Rectangle ballCol = new Rectangle(ball.x, ball.y, ball.size, ball.size);
            
            // Ball stays inside boundary
            if (ball.x < 64) {
                if (ball.vx < 0)
                    ball.vx *= -1;
            } else if (ball.x > 559+14) {
                if (ball.vx > 0)
                    ball.vx *= -1;
            }
            
            // Ball Collision
            if ( ballCol.intersects(col1basketIn) ) {
                p1.score += 2;
                ball.x = (width-31)/2;
                ball.y = 100;
                ball.vx = 0;
                ball.vy = 0;
                floor = 148+boardSize;
                p2.x = (width-31)/2-55;
                p2.y = floor-64;
                p1.x = (width-31)/2+90;
                p1.y = floor-64;
                ball.thrown = false;
            } else if ( ballCol.intersects(col2basketIn) ) {
                p2.score += 2;
                ball.x = (width-31)/2;
                ball.y = 100;
                ball.vx = 0;
                ball.vy = 0;
                floor = 148+boardSize;
                p2.x = (width-31)/2-55;
                p2.y = floor-64;
                p1.x = (width-31)/2+90;
                p1.y = floor-64;
                ball.thrown = false;
            }
            if ( ballCol.intersects(col1basketOut) || ballCol.intersects(col2basketOut) ) {
                ball.vx *= -1;
                ball.vy *= -1;
            }
            if ( ballCol.intersects(col1pole) || ballCol.intersects(col2pole) ) {
                ball.vx *= -1;
            }
            
            int floor1, floor2;
            floor1 = p1.handleBall(p2, ballCol, ball, floor);
            floor2 = p2.handleBall(p1, ballCol, ball, floor);
            
            if (p1.holding || p1.aiming)
                floor = floor1;
            else if (p2.holding || p2.aiming)
                floor = floor2;
            
            ball.handleGravity(p1, p2, floor);
            
            repaint();
        }
    }
}
