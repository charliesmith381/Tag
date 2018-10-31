/*//////////////////////////////////////////////////////////////////////////////
 * Author: Charlie Smith
 * Date: July 26, 2018
 * Description: Used to assist the FightMan class. Mostly keeps track of
 * useful information from the "previous frame".
 * 
 *//////////////////////////////////////////////////////////////////////////////

public class LastFrame {
    
    private boolean upPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean downPressed;
    
    private boolean it;
    private boolean overLapping = false;
    
    private FightMan transferringTo = null;
    
    private int jumpTimer;
    private int health;
    private int jumps;
    private int clock;
    
    public boolean up() {
        return upPressed;
    }
    
    public boolean isIt() {
        return it;
    }
    
    public boolean isOverLapping() {
        return overLapping;
    }
    
    public FightMan transferringTo() {
        return transferringTo;
    }
    
    public void setUp(boolean up) {
        upPressed = up;
    }
    
    public void setIt(boolean t) {
        it = t;
    }
    
    public void setOverLapping(boolean o) {
        overLapping = o;
    }
    
    public void setTransferringTo(FightMan f) {
        transferringTo = f;
    }
    
}