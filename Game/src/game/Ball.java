package game;

public class Ball {
    public float vx;
    public float vy;
    public boolean onAir;
    public boolean thrown;
    public boolean goDown;
    public int x;
    public int y;
    public int size;
    private int bounce;
    
    Ball(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.vx = 0.0f;
        this.vy = 0.0f;
        this.size = 15;
        this.bounce = 0;
        this.onAir = true;
        this.thrown = false;
        this.goDown = true;
    }
    
    public void handleGravity(Player p1, Player p2, int floor)
    {
        if (!p1.holding && !p1.aiming && !p2.holding && !p2.aiming && this.onAir) {
                if (this.x < 31 || this.x > 610)
                    this.vx *= -1;
                if ( (this.y+this.size > floor)) {
                    this.y = floor-this.size;
                    this.vy -= this.vy*2+this.bounce*2;
                    if (this.thrown)
                        this.bounce++;
                } else
                    this.vy += 1f;
        }
    }
}
