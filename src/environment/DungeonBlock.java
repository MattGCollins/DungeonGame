/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package environment;

import dungeon.Globals;
import dungeon.Resources;
import java.awt.Point;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Matt
 */
public class DungeonBlock {
    public static final int NONE = 0;
    public static final int BRICK_TEXTURE = 1;
    public static final int TILE_TEXTURE = 2;
    
    public int[] walls;
    public int floor;
    
    public Point position;
    
    public static final int BLOCK_SOLID = 1;
    public static final int BLOCK_PASSAGE = 2;
    public static final int BLOCK_DOOR = 4;
    public static final int BLOCK_ROOM = 8;
    public static final int BLOCK_PERIM = 16;
    public static final int BLOCK_BLOCKED = BLOCK_PASSAGE | BLOCK_PERIM;
    public static final int BLOCK_DOOR_CONNECT = BLOCK_SOLID | BLOCK_ROOM;
    public static final int BLOCK_SOLID_LOOKING = BLOCK_SOLID | BLOCK_PERIM;
    public static final int BLOCK_PASSABLE = BLOCK_PASSAGE | BLOCK_DOOR | BLOCK_ROOM;
    
    public int blockType = BLOCK_SOLID;
    public boolean traveledTo = false;
    public boolean seen = false;
    
    public DungeonBlock()
    {
        walls = new int[4];
        walls[Globals.FACING_NORTH] = NONE;
        walls[Globals.FACING_EAST] = NONE;
        walls[Globals.FACING_SOUTH] = NONE;
        walls[Globals.FACING_WEST] = NONE;
        floor = 0;
        position = new Point();
    }
    
    public DungeonBlock(int wallN, int wallE, int wallS, int wallW, int dungeonFloor)
    {
        walls = new int[4];
        walls[Globals.FACING_NORTH] = wallN;
        walls[Globals.FACING_EAST] = wallE;
        walls[Globals.FACING_SOUTH] = wallS;
        walls[Globals.FACING_WEST] = wallW;
        floor = dungeonFloor;
        position = new Point();
    }
    
    public void set(int wallN, int wallE, int wallS, int wallW, int dungeonFloor)
    {
        walls[Globals.FACING_NORTH] = wallN;
        walls[Globals.FACING_EAST] = wallE;
        walls[Globals.FACING_SOUTH] = wallS;
        walls[Globals.FACING_WEST] = wallW;
        floor = dungeonFloor;
    }
    
    public void render()
    {
        //System.out.println(blockType);
        if((blockType & BLOCK_SOLID_LOOKING) != 0)
        {
            if(walls[Globals.FACING_NORTH] != NONE)
            {
                Resources.textures[walls[Globals.FACING_NORTH]].bind();
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2d(0,0);
                GL11.glVertex3d(0.5,-0.5,-0.5);
                GL11.glTexCoord2d(1,0);
                GL11.glVertex3d(-0.5,-0.5,-0.5);
                GL11.glTexCoord2d(1,1);
                GL11.glVertex3d(-0.5,0.5,-0.5);
                GL11.glTexCoord2d(0,1);
                GL11.glVertex3d(0.5,0.5,-0.5);
                GL11.glEnd();
            }
            if(walls[Globals.FACING_EAST] != NONE)
            {
                Resources.textures[walls[Globals.FACING_EAST]].bind();
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2d(0,0);
                GL11.glVertex3d(0.5,-0.5,0.5);
                GL11.glTexCoord2d(1,0);
                GL11.glVertex3d(0.5,-0.5,-0.5);
                GL11.glTexCoord2d(1,1);
                GL11.glVertex3d(0.5,0.5,-0.5);
                GL11.glTexCoord2d(0,1);
                GL11.glVertex3d(0.5,0.5,0.5);
                GL11.glEnd();
            }

            if(walls[Globals.FACING_SOUTH] != NONE)
            {
                Resources.textures[walls[Globals.FACING_SOUTH]].bind();
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2d(0,0);
                GL11.glVertex3d(-0.5,-0.5,0.5);
                GL11.glTexCoord2d(1,0);
                GL11.glVertex3d(0.5,-0.5,0.5);
                GL11.glTexCoord2d(1,1);
                GL11.glVertex3d(0.5,0.5,0.5);
                GL11.glTexCoord2d(0,1);
                GL11.glVertex3d(-0.5,0.5,0.5);
                GL11.glEnd();
            }

            if(walls[Globals.FACING_WEST] != NONE)
            {
                Resources.textures[walls[Globals.FACING_WEST]].bind();
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2d(0,0);
                GL11.glVertex3d(-0.5,-0.5,-0.5);
                GL11.glTexCoord2d(1,0);
                GL11.glVertex3d(-0.5,-0.5,0.5);
                GL11.glTexCoord2d(1,1);
                GL11.glVertex3d(-0.5,0.5,0.5);
                GL11.glTexCoord2d(0,1);
                GL11.glVertex3d(-0.5,0.5,-0.5);
                GL11.glEnd();
            }
        }
        //Floor
        Resources.textures[TILE_TEXTURE].bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0,0);
        GL11.glVertex3d(-0.5,-0.5,-0.5);
        GL11.glTexCoord2d(1,0);
        GL11.glVertex3d(-0.5,-0.5,0.5);
        GL11.glTexCoord2d(1,1);
        GL11.glVertex3d(0.5,-0.5,0.5);
        GL11.glTexCoord2d(0,1);
        GL11.glVertex3d(0.5,-0.5,-0.5);
        GL11.glEnd();
        //Ceiling
        Resources.textures[TILE_TEXTURE].bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0,0);
        GL11.glVertex3d(0.5,0.5,-0.5);
        GL11.glTexCoord2d(1,0);
        GL11.glVertex3d(0.5,0.5,0.5);
        GL11.glTexCoord2d(1,1);
        GL11.glVertex3d(-0.5,0.5,0.5);
        GL11.glTexCoord2d(0,1);
        GL11.glVertex3d(-0.5,0.5,-0.5);
        GL11.glEnd();
    }
    
    public void renderMap(int size, boolean playerHere)
    {
        if(seen)
        {
            if(playerHere)
                glColor4f(1.0f,0.0f,0.0f,0.3f);
            else if((blockType & BLOCK_SOLID_LOOKING) != 0)
                glColor4f(0.0f,0.0f,0.0f,0.3f);
            else if((blockType & BLOCK_DOOR) != 0)
                glColor4f(1.0f,0.0f,1.0f,0.3f);
            else if((blockType & (BLOCK_ROOM | BLOCK_PASSAGE)) != 0)
                glColor4f(0.0f,1.0f,1.0f,0.3f);
            else
                glColor4f(0.0f,0.5f,0.5f, 0.3f);
            glBegin(GL_QUADS);
            glTexCoord2f(0.0f,0.0f); glVertex2f(0.0f,0.0f);
            glTexCoord2f(1.0f,0.0f); glVertex2f(size,0.0f);
            glTexCoord2f(1.0f,1.0f); glVertex2f(size,size);
            glTexCoord2f(0.0f,1.0f); glVertex2f(0.0f,size);
            glEnd();
        }
    }
    
    public boolean isPassable()
    {
        return ((blockType & BLOCK_PASSABLE) != 0);
    }
    
    public void setPosition(Point position)
    {
        this.position.x = position.x;
        this.position.y = position.y;
    }
    
    public void setPosition(int x, int y)
    {
        this.position.x = x;
        this.position.y = y;
    }
    
    public int getX()
    {
        return this.position.x;
    }
    
    public int getY()
    {
        return this.position.y;
    }
    
    public Point getPosition()
    {
        return new Point(position);
    }
}
