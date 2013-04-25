/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 *
 * @author Matt
 */
public class InputKeeper {
    public Point mousePos;
    public Point mousePosPrev;
    
    public boolean leftDown;
    public boolean leftDownPrev;
    
    public boolean rightDown;
    public boolean rightDownPrev;
    
    public boolean[] keysDown;
    public boolean[] keysDownPrev;
    public LinkedList<Integer> keysToCheck;
    
    public InputKeeper()
    {
        mousePos = new Point();
        mousePos.x = 0;
        mousePos.y = 0;
        
        mousePosPrev = new Point();
        mousePosPrev.x = 0;
        mousePosPrev.y = 0;
        
        leftDown = false;
        leftDownPrev = false;
        
        rightDown = false;
        rightDownPrev = false;
        
        keysToCheck = new LinkedList<>();
        
        keysDown = new boolean[1024];
        keysDownPrev = new boolean[1024];
        
    }
    
    public void update()
    {
        mousePosPrev.x = mousePos.x;
        mousePosPrev.y = mousePos.y;
        mousePos.x = Mouse.getX();
        mousePos.y = Mouse.getY();
        
        leftDownPrev = leftDown;
        leftDown = Mouse.isButtonDown(0);
        
        rightDownPrev = rightDown;
        rightDown = Mouse.isButtonDown(1);
        
        Iterator i = keysToCheck.iterator();
        if(keysToCheck.size() > 0)
        {
            do
            {
                int currentCheck = (int)i.next();
                keysDownPrev[currentCheck] = keysDown[currentCheck];
                keysDown[currentCheck] = Keyboard.isKeyDown(currentCheck);
            }while(i.hasNext());
        }
    }
    
    public void addCheck(int i)
    {
        if(!keysToCheck.contains(i))
        {
            keysToCheck.add(i);
        }
    }
    public void removeCheck(int i)
    {
        keysToCheck.remove(keysToCheck.indexOf(i));
    }
}
