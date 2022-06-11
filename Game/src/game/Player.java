package game;

import java.awt.Rectangle;

public class Player {
    public int score;
    public int x,y;
    public int vx, vy;
    public int size;
    public boolean jumping;
    public boolean holding;
    public boolean aiming;
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    Rectangle holdBox;
    Rectangle aimBox;
    
    Player(int x, int y)
    {
        this.x = x;
        this.score = 0;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.holding = false;
        this.size = 64;
        this.holdBox = new Rectangle(x, 15, 15, 15);
        this.aimBox = new Rectangle(x, y+15, 15, 15);
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
    }
    
    public void handleMovement()
    {
        if (this.up && this.y+this.size > 217)
            this.y -= 5;
        if (this.down && this.y+this.size < 397)
            this.y += 5;
        if (this.right && this.x+32 < 640)
            this.x += 5;
        if (this.left && this.x > 31)
            this.x -= 5;
    }
    
    public int handleBall(Player oponent, Rectangle ballCol, Ball ball, int floor)
    {   
        if (!this.holding && ballCol.intersects(this.holdBox)) {
            this.holding = true;
            oponent.holding = false;
            ball.thrown = false;
            ball.y = (int)this.holdBox.getY();
            floor = this.y + this.size;
        }
        if (this.aiming) {
            floor = this.y + this.size;
            ball.x = (int)this.aimBox.getX();
            ball.y = (int)this.aimBox.getY();
        }else if (this.holding) {
            floor = this.y + this.size;
            ball.x = (int)this.holdBox.getX();
            if (ball.goDown) ball.vy = 10; else ball.vy = -10;
            if (ball.y < this.y+2*ball.size) {
                ball.y = this.y+2*ball.size;
                ball.goDown = true;
            } if (ball.y+ball.size > floor) {
                ball.y = floor-ball.size;
                ball.goDown = false;
            }
        }
        return floor;
    }
}
