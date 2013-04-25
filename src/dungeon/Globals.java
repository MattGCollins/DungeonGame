/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon;

import engine.InputKeeper;
import entities.Player;
import environment.Dungeon;
import java.util.Random;

/**
 *
 * @author Matt
 */
public class Globals {
    public static final int FACING_NORTH = 0;
    public static final int FACING_EAST = 1;
    public static final int FACING_SOUTH = 2;
    public static final int FACING_WEST = 3;
    
    public static final int DISPLAY_HEIGHT = 600;
    public static final int DISPLAY_WIDTH = 800;
    
    public static Random rand = new Random();
    public static InputKeeper input;
    
    public static long curFrameTime = 0;
    public static float frameElapsed = 1.0f / 60.0f;
    public static boolean mapOn = false;
    public static boolean miniMapOn = false;
    public static int sightDepth = 7;
    
    public static Dungeon currentDungeon;
    public static Player player;
}
