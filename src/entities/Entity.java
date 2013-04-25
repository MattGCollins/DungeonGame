/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.awt.Point;
import dungeon.Globals;

/**
 *
 * @author Matt
 */
public class Entity {
    public Point position;
    public int facing;
    
    public Entity()
    {
        facing = Globals.FACING_NORTH;
        
        position = new Point(0,0);
    }
    
    public void update()
    {
        
    }
    
    public void moveTo(int x, int y)
    {
        position.setLocation(x,y);
    }
    
    public void moveForward()
    {
        switch(facing)
        {
            case Globals.FACING_NORTH:
                position.y += 1;
                break;
            case Globals.FACING_EAST:
                position.x += 1;
                break;
            case Globals.FACING_SOUTH:
                position.y -= 1;
                break;
            case Globals.FACING_WEST:
                position.x -= 1;
                break;
        }
    }
    
    public void moveBackward()
    {
        switch(facing)
        {
            case Globals.FACING_NORTH:
                position.y -= 1;
                break;
            case Globals.FACING_EAST:
                position.x -= 1;
                break;
            case Globals.FACING_SOUTH:
                position.y += 1;
                break;
            case Globals.FACING_WEST:
                position.x += 1;
                break;
        }
    }
    
    public void moveLeft()
    {
        switch(facing)
        {
            case Globals.FACING_NORTH:
                position.x -= 1;
                break;
            case Globals.FACING_EAST:
                position.y += 1;
                break;
            case Globals.FACING_SOUTH:
                position.x += 1;
                break;
            case Globals.FACING_WEST:
                position.y -= 1;
                break;
        }
    }
    
    public void moveRight()
    {
        switch(facing)
        {
            case Globals.FACING_NORTH:
                position.x += 1;
                break;
            case Globals.FACING_EAST:
                position.y -= 1;
                break;
            case Globals.FACING_SOUTH:
                position.x -= 1;
                break;
            case Globals.FACING_WEST:
                position.y += 1;
                break;
        }
    }
    
    public void turnLeft()
    {
        facing -= 1;
        if(facing < 0)
        {
            facing = 3;
        }
    }
    
    public void turnRight()
    {
        facing += 1;
        if(facing > 3)
        {
            facing = 0;
        }
    }
    
    public void spin180()
    {
        facing += 2;
        if(facing > 3)
        {
            facing -= 4;
        }
    }
}
