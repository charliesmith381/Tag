/*//////////////////////////////////////////////////////////////////////////////
 * Author: Charlie Smith
 * Date: July 2018
 * Description: Simple game of tag. Plays up to 9 players (in theory), user
 * controls their character with four keys of their choosing. Play ends when
 * each players' health bar reaches zero except for the last player's. The last
 * remaining player becomes the winner.
 * 
 *//////////////////////////////////////////////////////////////////////////////

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;
import java.awt.Color;
import java.util.ArrayList;

public class FightGame {
    
    private int in; // how many players are not eliminated in 3+ games
    
    public ArrayList<FightMan> playerSelection() {
        int p = 0;
        boolean undecided = true;
        while (undecided) {
            StdDraw.clear();
            StdDraw.text(0.5, 0.5, "How many players? (use keyboard)");
            
            for (int n = 50; n < 58; n++) {
                if (StdDraw.isKeyPressed(n)) {
                    in = n - 48;
                    while (undecided) if (!StdDraw.isKeyPressed(n)) {
                        p = in;
                        undecided = false;
                    }
                }
            }
            
            StdDraw.show();
            StdDraw.pause(30);
        }
        
        ArrayList<FightMan> tagPlayers = new ArrayList<FightMan>();
        int it = (int) (Math.random() * p);
        double xSpacing = 1.0 / (p + 1);
        
        for (int i = 0; i < p; i++) {
            Color col = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
            int l = controlSelect("Player " + (i + 1) + ": left");
            int u = controlSelect("Player " + (i + 1) + ": up");
            int r = controlSelect("Player " + (i + 1) + ": right");
            int d = controlSelect("Player " + (i + 1) + ": down");
            tagPlayers.add(new FightMan(xSpacing * (i + 1), 0.1, i == it, col, i + 1, l, u, r, d));
        }
        
        return tagPlayers;
    }
    
    public int controlSelect(String string) {
        while (true) {
            StdDraw.clear();
            StdDraw.text(0.5, 0.5, string);
            StdDraw.text(0.5, 0.45, "(press key to make selection)");
            for (int n = 0; n < 256; n++) {
                if (StdDraw.isKeyPressed(n)) {
                    while (true) if (!StdDraw.isKeyPressed(n)) return n;
                }
            }
            StdDraw.show();
        }
    }
    
    public void resetGame(ArrayList<FightMan> tagPlayers) {
        double xSpacing = 1.0 / (tagPlayers.size() + 1);
        
        int i = 1;
        for (FightMan f : tagPlayers) {
            f.setHealth(f.getMaxHealth());
            f.setX(xSpacing * i);
            f.setY(0.1);
            f.setVelZero();
            i++;
        }
    }
    
    public FightMan runGame(ArrayList<FightMan> tagPlayers) {

        double xSpacing = 1.0 / (tagPlayers.size() + 1);
        
        while (true) {
            StdDraw.clear();
            int i = 1;
            in = 0;
            for (FightMan f : tagPlayers) {
                f.playFrame(0.006, 0.55, 0.9);
                f.healthBar(xSpacing * i, 0.8);
                i++;
                if (f.isIn()) in++;
            }
            
            for (FightMan a : tagPlayers) {
                for (FightMan b : tagPlayers) {
                    if (!a.equals(b)) {
                        a.collisionDetect(b);
                    }
                }
            }
            
            FightMan winner = gameCheck(tagPlayers);
            if (winner != null) return winner;
            
            StdDraw.show();
            StdDraw.pause(30);
        }
    }
    
    public FightMan gameCheck(Iterable<FightMan> list) {
        int it = 0;
        for (FightMan f : list) {
            if (f.isIt()) it++;
        }
        if (it != 1) {
            int set = (int) (Math.random() * in);
            int i = 0;
            for (FightMan f : list) {
                if (f.isIn()) {
                    f.setIt(i == set);
                    i++;
                }
            }
        }
        if (in <= 1) {
            for (FightMan f : list) {
                if (f.isIn()) return f;
            }
        }
        return null;
    }
    
    public boolean endGameScreen(FightMan w) {
        while (true) {
            StdDraw.clear();
            StdDraw.setPenColor();
            StdDraw.text(0.5, 0.5, "Winner: " + w.toString());
            StdDraw.text(0.5, 0.4, "Play again? Press spacebar");
            StdDraw.text(0.5, 0.3, "Return to main menu? Press 1");
            if (StdDraw.isKeyPressed(49)) return false;
            if (StdDraw.isKeyPressed(32)) return true;
            StdDraw.show();
        }
    }
    
    public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(750, 750);
        
        FightGame tag = new FightGame();
        
        while (true) {
            ArrayList<FightMan> players = tag.playerSelection();
            boolean playing = true;
            while (playing) {
                FightMan winner = tag.runGame(players);
                playing = tag.endGameScreen(winner);
                tag.resetGame(players);
            }
        }
    }
}