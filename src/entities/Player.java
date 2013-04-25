/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import dungeon.Globals;
import environment.DungeonBlock;
import java.awt.Point;
import java.awt.geom.Point2D;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author Matt
 */
public class Player extends Entity{
    
    public static final int MOVE_NONE = 0;
    public static final int MOVE_FORWARD = 1;
    public static final int MOVE_BACKWARD = 2;
    public static final int MOVE_LEFT = 3;
    public static final int MOVE_RIGHT = 4;
    public static final int TURN_LEFT = 5;
    public static final int TURN_RIGHT = 6;
    public static final int SPIN_180 = 7;
    public static Point[] directions;
    
    //public int[] futureMoves;
    public int currentMove;
    //public int totalMoves = 0;
    //public static final int maxMoves = 1;
    
    public static final float movementTime = 0.4f;
    public float curMovementTime = 0.0f;
    public Point2D.Double positionDif;
    public float rotationDif;
    
    public Player()
    {
        Globals.input.addCheck(Keyboard.KEY_UP);
        Globals.input.addCheck(Keyboard.KEY_DOWN);
        Globals.input.addCheck(Keyboard.KEY_LEFT);
        Globals.input.addCheck(Keyboard.KEY_RIGHT);
        Globals.input.addCheck(Keyboard.KEY_W);
        Globals.input.addCheck(Keyboard.KEY_S);
        Globals.input.addCheck(Keyboard.KEY_A);
        Globals.input.addCheck(Keyboard.KEY_D);
        Globals.input.addCheck(Keyboard.KEY_Q);
        Globals.input.addCheck(Keyboard.KEY_E);
        Globals.input.addCheck(Keyboard.KEY_X);
        Globals.input.addCheck(Keyboard.KEY_M);
        Globals.input.addCheck(Keyboard.KEY_TAB);
        
        currentMove = MOVE_NONE;
        
        positionDif = new Point2D.Double();
        positionDif.setLocation(0.0d, 0.0d);
        rotationDif = 0;
        
        directions = new Point[4];
        directions[Globals.FACING_NORTH] = new Point(0,1);
        directions[Globals.FACING_EAST] = new Point(1,0);
        directions[Globals.FACING_SOUTH] = new Point(0,-1);
        directions[Globals.FACING_WEST] = new Point(-1,0);
    }
    
    @Override
    public void update()
    {
        handleInput();
        if(currentMove != MOVE_NONE)
        {
           /* if(curMovementTime == 0)
            {
                
            }*/
            curMovementTime += Globals.frameElapsed;
            if(curMovementTime < movementTime)
            {
                //System.out.println(curMovementTime / movementTime);
                switch(currentMove)
                {
                    case MOVE_FORWARD:
                        getPartialForward(curMovementTime / movementTime);
                        break;
                    case MOVE_BACKWARD:
                        getPartialBackward(curMovementTime / movementTime);
                        break;
                    case MOVE_LEFT:
                        getPartialLeft(curMovementTime / movementTime);
                        break;
                    case MOVE_RIGHT:
                        getPartialRight(curMovementTime / movementTime);
                        break;
                    case TURN_LEFT:
                        rotationDif = -90 * (curMovementTime / movementTime);
                        break;
                    case TURN_RIGHT:
                        rotationDif = 90 * (curMovementTime / movementTime);
                        break;
                    case SPIN_180:
                        rotationDif = -180 * (curMovementTime / movementTime);
                        break;
                }
            }
            else
            {
                curMovementTime = 0;
                switch(currentMove)
                {
                    case MOVE_FORWARD:
                        moveForward();
                        break;
                    case MOVE_BACKWARD:
                        moveBackward();
                        break;
                    case MOVE_LEFT:
                        moveLeft();
                        break;
                    case MOVE_RIGHT:
                        moveRight();
                        break;
                    case TURN_LEFT:
                        turnLeft();
                        break;
                    case TURN_RIGHT:
                        turnRight();
                        break;
                    case SPIN_180:
                        spin180();
                        break;
                }
                currentMove = MOVE_NONE;
                positionDif.setLocation(0, 0);
                rotationDif = 0;
            }
        }
    }
    
    public void handleInput()
    {
        if(Globals.input.keysDown[Keyboard.KEY_M] && !Globals.input.keysDownPrev[Keyboard.KEY_M])
        {
            Globals.miniMapOn = !Globals.miniMapOn;
        }
        if(Globals.input.keysDown[Keyboard.KEY_TAB] && !Globals.input.keysDownPrev[Keyboard.KEY_TAB])
        {
            Globals.mapOn = !Globals.mapOn;
        }
        if(currentMove == MOVE_NONE)
        {
            if((Globals.input.keysDown[Keyboard.KEY_LEFT] || Globals.input.keysDown[Keyboard.KEY_Q]))
            {
                currentMove = TURN_LEFT;
            }
            else if((Globals.input.keysDown[Keyboard.KEY_RIGHT] || Globals.input.keysDown[Keyboard.KEY_E]))
            {
                currentMove = TURN_RIGHT;
            }
            else if(Globals.input.keysDown[Keyboard.KEY_X])
            {
                currentMove = SPIN_180;
            }
            else if((Globals.input.keysDown[Keyboard.KEY_UP] || Globals.input.keysDown[Keyboard.KEY_W]))
            {
                currentMove = MOVE_FORWARD;
            }
            else if((Globals.input.keysDown[Keyboard.KEY_DOWN] || Globals.input.keysDown[Keyboard.KEY_S]))
            {
                currentMove = MOVE_BACKWARD;
            }
            else if(Globals.input.keysDown[Keyboard.KEY_A])
            {
                currentMove = MOVE_LEFT;
            }
            else if(Globals.input.keysDown[Keyboard.KEY_D])
            {
                currentMove = MOVE_RIGHT;
            }
        }
    }
    
    public void getPartialForward(double amount)
    {
        Point positionTest = new Point(position.x + directions[facing].x, position.y + directions[facing].y);
        if((amount > 0.5) && (Globals.currentDungeon.map[positionTest.x][positionTest.y].blockType & DungeonBlock.BLOCK_PASSABLE) == 0)
        {
            double x = (double)directions[facing].x - (double)directions[facing].x * amount;
            double y = (double)directions[facing].y - (double)directions[facing].y * amount;
            positionDif.setLocation(x, y);
        }
        else
        {
            double x = (double)directions[facing].x * amount;
            double y = (double)directions[facing].y * amount;
            positionDif.setLocation(x, y);
        }
    }
    
    public void getPartialBackward(double amount)
    {
        int altFacing = facing + 2;
        if(altFacing > 3)
            altFacing -= 4;
        Point positionTest = new Point(position.x + directions[altFacing].x, position.y + directions[altFacing].y);
        if((amount > 0.5) && (Globals.currentDungeon.map[positionTest.x][positionTest.y].blockType & DungeonBlock.BLOCK_PASSABLE) == 0)
        {
            double x = (double)directions[altFacing].x - (double)directions[altFacing].x * amount;
            double y = (double)directions[altFacing].y - (double)directions[altFacing].y * amount;
            positionDif.setLocation(x, y);
        }
        else
        {
            double x = (double)directions[altFacing].x * amount;
            double y = (double)directions[altFacing].y * amount;
            positionDif.setLocation(x, y);
        }
    }
    
    public void getPartialLeft(double amount)
    {
        int altFacing = facing + 3;
        if(altFacing > 3)
            altFacing -= 4;
        Point positionTest = new Point(position.x + directions[altFacing].x, position.y + directions[altFacing].y);
        if((amount > 0.5) && (Globals.currentDungeon.map[positionTest.x][positionTest.y].blockType & DungeonBlock.BLOCK_PASSABLE) == 0)
        {
            double x = (double)directions[altFacing].x - (double)directions[altFacing].x * amount;
            double y = (double)directions[altFacing].y - (double)directions[altFacing].y * amount;
            positionDif.setLocation(x, y);
        }
        else
        {
            double x = (double)directions[altFacing].x * amount;
            double y = (double)directions[altFacing].y * amount;
            positionDif.setLocation(x, y);
        }
    }
    
    public void getPartialRight(double amount)
    {
        int altFacing = facing + 1;
        if(altFacing > 3)
            altFacing -= 4;
        Point positionTest = new Point(position.x + directions[altFacing].x, position.y + directions[altFacing].y);
        if((amount > 0.5) && (Globals.currentDungeon.map[positionTest.x][positionTest.y].blockType & DungeonBlock.BLOCK_PASSABLE) == 0)
        {
            double x = (double)directions[altFacing].x - (double)directions[altFacing].x * amount;
            double y = (double)directions[altFacing].y - (double)directions[altFacing].y * amount;
            positionDif.setLocation(x, y);
        }
        else
        {
            double x = (double)directions[altFacing].x * amount;
            double y = (double)directions[altFacing].y * amount;
            positionDif.setLocation(x, y);
        }
    }
    
    @Override
    public void moveForward()
    {
        Point newPosition = new Point(position.x, position.y);
        switch(facing)
        {
            case Globals.FACING_NORTH:
                newPosition.y += 1;
                break;
            case Globals.FACING_EAST:
                newPosition.x += 1;
                break;
            case Globals.FACING_SOUTH:
                newPosition.y -= 1;
                break;
            case Globals.FACING_WEST:
                newPosition.x -= 1;
                break;
        }
        if((Globals.currentDungeon.map[newPosition.x][newPosition.y].blockType & DungeonBlock.BLOCK_PASSABLE) != 0)
        {
            position.setLocation(newPosition);
            Globals.currentDungeon.exploreSquare(Globals.sightDepth);
        }
    }
    
    @Override
    public void moveBackward()
    {
        Point newPosition = new Point(position.x, position.y);
        switch(facing)
        {
            case Globals.FACING_NORTH:
                newPosition.y -= 1;
                break;
            case Globals.FACING_EAST:
                newPosition.x -= 1;
                break;
            case Globals.FACING_SOUTH:
                newPosition.y += 1;
                break;
            case Globals.FACING_WEST:
                newPosition.x += 1;
                break;
        }
        if((Globals.currentDungeon.map[newPosition.x][newPosition.y].blockType & DungeonBlock.BLOCK_PASSABLE) != 0)
        {
            position.setLocation(newPosition);
            Globals.currentDungeon.exploreSquare(Globals.sightDepth);
        }
    }
    
    @Override
    public void moveLeft()
    {
        Point newPosition = new Point(position.x, position.y);
        switch(facing)
        {
            case Globals.FACING_NORTH:
                newPosition.x -= 1;
                break;
            case Globals.FACING_EAST:
                newPosition.y += 1;
                break;
            case Globals.FACING_SOUTH:
                newPosition.x += 1;
                break;
            case Globals.FACING_WEST:
                newPosition.y -= 1;
                break;
        }
        if((Globals.currentDungeon.map[newPosition.x][newPosition.y].blockType & DungeonBlock.BLOCK_PASSABLE) != 0)
        {
            position.setLocation(newPosition);
            Globals.currentDungeon.exploreSquare(Globals.sightDepth);
        }
    }
    
    @Override
    public void moveRight()
    {
        Point newPosition = new Point(position.x, position.y);
        switch(facing)
        {
            case Globals.FACING_NORTH:
                newPosition.x += 1;
                break;
            case Globals.FACING_EAST:
                newPosition.y -= 1;
                break;
            case Globals.FACING_SOUTH:
                newPosition.x -= 1;
                break;
            case Globals.FACING_WEST:
                newPosition.y += 1;
                break;
        }
        if((Globals.currentDungeon.map[newPosition.x][newPosition.y].blockType & DungeonBlock.BLOCK_PASSABLE) != 0)
        {
            position.setLocation(newPosition);
            Globals.currentDungeon.exploreSquare(Globals.sightDepth);
        }
    }
    
}
