/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package environment;

import engine.Range;
import java.awt.Point;

/**
 *
 * @author Matt
 */
public class LOSBlock {
    public Range bounds;
    public boolean crosses0;
    public Range bounds2;
    public Point position;
    public float distanceSq;
    public boolean inRange;
    public boolean blocking;
    
    public LOSBlock()
    {
        bounds = new Range();
        bounds2 = new Range();
        bounds.lower = 360;
        bounds.upper = -360;
        crosses0 = false;
        bounds2.lower = 360;
        bounds2.upper = 0;
        position = new Point();
        inRange = false;
        blocking = false;
        distanceSq = 0;
    }
}
