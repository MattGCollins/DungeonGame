/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package environment;

import java.awt.Point;

/**
 *
 * @author Matt
 */
public class DungeonRoom {
    public Point bottomLeft;
    public Point topRight;
    
    public DungeonRoom()
    {
        bottomLeft = new Point(0,0);
        topRight = new Point(0,0);
    }
    
    public void set(int bottom, int left, int top, int right)
    {
        bottomLeft.x = left;
        bottomLeft.y = bottom;
        topRight.x = right;
        topRight.y = top;
    }
    
    public boolean collidePerimeter(DungeonRoom room2)
    {
        boolean returnValue = false;
        if(bottomLeft.x - 1 < room2.topRight.x && topRight.x + 1 > room2.bottomLeft.x && bottomLeft.y - 1 < room2.topRight.y && topRight.y + 1 > room2.bottomLeft.y)
        {
            returnValue = true;
        }
        return returnValue;
    }
}
