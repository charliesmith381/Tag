/*//////////////////////////////////////////////////////////////////////////////
 * Author: Charlie Smith
 * Date: July 2018
 * Description: Versatile simple platform gaming character.
 * 
 *//////////////////////////////////////////////////////////////////////////////

import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;

public class FightMan extends SolidRect {
    
    private final double size; // side length
    private double speed; // max horizontal speed
    private final double jumpPower; // determines min jump height
    private final double jumpTime; // determines max jump height
    private final double maxHealth = 500;
    private final int maxJumps = 2;
    private final int left, up, right, down; // ascii for directions
    private final Color color;
    private final Color itColor = Color.RED;
    
    private double xPos, yPos, xVel, yVel; // x & y positions and velocities
    private double jumpTimer; // running track of jump left
    private double jumps; // running track of jumps left
    private double health; // keeps track of lifelength basically
    private boolean it; // keeps track of whether "it" or not
    private boolean in = true; // keeps track of whether still in or not in 3+ matches
    
    private int playerNumber; // player number
    private LastFrame prev = new LastFrame(); // contains "last frame" variables
    
    // simple constructor
    public FightMan(double xp, double yp, boolean isIt, Color col, int pn,
                    int l, int u, int r, int d) {
        xPos = xp;
        yPos = yp;
        xVel = 0;
        yVel = 0;
        size = 0.05;
        it = isIt;
        color = col;
        
        left = l;
        up = u;
        right = r;
        down = d;
        
        speed = 0.02;
        jumpPower = 0.03;
        jumpTime = 7;
        jumpTimer = jumpTime;
        health = maxHealth;
        
        playerNumber = pn;
        prev.setIt(isIt);
    }    
    
    // complete constructor
    public FightMan(double xp, double yp, double xv, double yv, double sz, 
                    boolean isIt, Color col, int l, int u, int r, int d, 
                    double sp, double jp, double jt, double h) {
        xPos = xp;
        yPos = yp;
        xVel = xv;
        yVel = yv;
        size = sz;
        it = isIt;
        color = col;
        
        left = l;
        up = u;
        right = r;
        down = d;
        
        speed = sp;
        jumpPower = jp;
        jumpTime = jt;
        jumpTimer = jumpTime;
        health = h;
        
        prev.setIt(isIt);
        
    }
    
    public String toString() {
        return "Player " + playerNumber;
    }
    
    public boolean isIt() {
        return it;
    }
    
    public void setIt(boolean isIt) {
        it = isIt;
    }
    
    public double getHealth() {
        return health;
    }
    
    public void setHealth(double h) {
        health = h;
    }
    
    public boolean isIn() {
        return in;
    }
    
    public void setIn(boolean n) {
        in  = n;
    }
    
    public double getMaxHealth() {
        return maxHealth;
    }
    
    public void setX(double x) {
        xPos = x;
    }
    
    public void setY(double y) {
        yPos = y;
    }
    
    public void setVelZero() {
        xVel = 0;
        yVel = 0;
    }
    
    public void setSpeed(double s) {
        speed = s;
    }
    
    public void draw() {
        if (!in) return;
        if (it) {
            StdDraw.setPenColor(itColor);
            StdDraw.filledSquare(xPos, yPos, size / 2);
            StdDraw.setPenColor(color);
            StdDraw.filledSquare(xPos, yPos, size * 0.35);
        } else {
            StdDraw.setPenColor(color);
            StdDraw.filledSquare(xPos, yPos, size / 2);
        }
        
    }
    
    public void move() {
        xPos += xVel;
        yPos += yVel;
    }
    
    public void fall(double gt) {
        yVel = yVel - gt;
    }
    
    public void groundFriction(double friction) {
        if (yPos <= size / 2) {
            if (!(StdDraw.isKeyPressed(left) || StdDraw.isKeyPressed(right))) {
                xVel = xVel * friction;
            }
        }
    }
    
    public void airFriction(double drag) {
        if (yPos > size / 2) {
            xVel = xVel * drag;
        }
    }
    
    public void containInBorder() {
        if (yPos - size / 2 < 0) {
            yPos = size / 2;
            yVel = 0;
        }
        if (xPos - size / 2 < 0) {
            xPos = size / 2;
            xVel = 0;
        }
        if (xPos + size / 2 > 1) {
            xPos = 1 - size / 2;
            xVel = 0;
        }
    }
    
    public void collisionDetect(FightMan f) {
        if (!this.isIt() || !f.isIn()) return;
        
        double dist = this.size / 2 + f.size / 2;
        
        // checks for overlap
        if (Math.abs(this.xPos - f.xPos) <= dist 
                && Math.abs(this.yPos - f.yPos) <= dist) {
            
            if (prev.transferringTo() == null || !prev.transferringTo().equals(f)) {
                this.setIt(false);
                f.setIt(true);
            }
            prev.setTransferringTo(f);
            f.prev.setTransferringTo(this);
        } else if (prev.transferringTo() == null) {
            return;
        } else if (prev.transferringTo().equals(f)) {
            
            prev.setTransferringTo(null);
            f.prev.setTransferringTo(null);
        }
    }
    
    public void keyListen() {
        // UP
        if (StdDraw.isKeyPressed(up)) {
            jump();
            prev.setUp(true);
        } else {
            prev.setUp(false);
        }
        
        // LEFT and not RIGHT
        if (StdDraw.isKeyPressed(left) && !StdDraw.isKeyPressed(right)) {
            goLeft();
        }
        
        // RIGHT and not LEFT
        if (StdDraw.isKeyPressed(right) && !StdDraw.isKeyPressed(left)) {
            goRight();
        }
    }
    
    public void jump() {
        
        // reset state
        if (yPos <= size / 2) {
            jumps = maxJumps;
            jumpTimer = jumpTime;
            prev.setUp(false);
        }
        
        // double jump mechanism
        if (!prev.up()) {
            jumps--;
            jumpTimer = jumpTime;
        }
        
        // does the jumping
        if (jumps >= 0 && jumpTimer > 0) {
            yVel = jumpPower;
        }
        
        jumpTimer--;
    }
    
    public void goLeft() {
        xVel = -speed;
    }
    
    public void goRight() {
        xVel = speed;
    }
    
    public void healthBar(double xLoc, double yLoc) {
        
        StdDraw.setPenColor(Color.GRAY);
        StdDraw.setPenRadius(0.01);
        StdDraw.rectangle(xLoc, yLoc, .015, .15);
        StdDraw.setPenColor(color);
        if (health > 0) {
            double bar = .15 * (health / maxHealth); // half of health height
            StdDraw.filledRectangle(xLoc, yLoc - .15 + bar, 0.012, bar);
            setIn(true);
        } else {
            setIn(false);
            setIt(false);
        }
        
        if (it) health--;
        setSpeed(0.02 + (0.01 * (1 - (health/maxHealth))));
    }
    
    public void playFrame(double gt, double friction, double drag) {
        fall(gt);
        groundFriction(friction);
        airFriction(drag);
        keyListen();
        move();
        containInBorder();
        draw();
    }
    
    public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();
        
        int it = (int) (Math.random() * 3);
        
        // lp'; <-- left, up, right, down
        FightMan fm = new FightMan(0.7, 0.1, (0 == it), new Color(100, 100, 150), 1, 76, 80, 222, 186);
        // awds <-- left, up, right, down
        FightMan fb = new FightMan(0.3, 0.1, (1 == it), new Color(100, 150, 100), 2, 65, 87, 68, 83);
        // gyjh <-- left, up, right, down
        FightMan fc = new FightMan(0.5, 0.1, (2 == it), StdDraw.PRINCETON_ORANGE, 3, 71, 89, 74, 72);
        
        while (true) {
            
            fm.healthBar(0.95, 0.8);
            fb.healthBar(0.05, 0.8);
            fc.healthBar(0.5, 0.8);
            
            fm.playFrame(0.006, 0.55, 0.9);
            fb.playFrame(0.006, 0.55, 0.9);
            fc.playFrame(0.006, 0.55, 0.9);
            
            fm.collisionDetect(fb);
            fm.collisionDetect(fc);
            fb.collisionDetect(fm);
            fb.collisionDetect(fc);
            fc.collisionDetect(fm);
            fc.collisionDetect(fb);
            
//            fm.prev.setIt(fm.isIt());
//            fb.prev.setIt(fb.isIt());
//            fc.prev.setIt(fc.isIt());
            
//            int c = 0;
//            {
//                if (fm.isIt()) c++;
//                if (fb.isIt()) c++;
//                if (fc.isIt()) c++;
//                if (c != 1) {
//                    int num = (int) (Math.random() * 3);
//                    fm.setIt(0 == num);
//                    fb.setIt(1 == num);
//                    fc.setIt(2 == num);
//                }
//            }
            
            // fixes both it at same time glitch
//            if (fm.isIt() == fb.isIt()) {
//                boolean temp = Math.random() < 0.5;
//                fm.setIt(temp);
//                fb.setIt(!temp);
//            }
            
            // Game win screen
            if (fm.getHealth() <= 0 || fb.getHealth() <= 0 || fc.getHealth() <= 0) {
                
                String winner;
                
                if (fm.getHealth() <= 0) winner = "GREEN!";
                else winner = "BLUE!";
                
                StdDraw.clear();
                StdDraw.setPenColor();
                StdDraw.text(0.5, 0.5, "winner is: " + winner);
                StdDraw.text(0.5, 0.4, "click space bar for new game");
                
                if (StdDraw.isKeyPressed(32)) {
                    fm.setHealth(fm.maxHealth);
                    fb.setHealth(fb.maxHealth);
                }
            }
            
            StdDraw.show();
            StdDraw.pause(30);
            StdDraw.clear();
        }
        
    }
}