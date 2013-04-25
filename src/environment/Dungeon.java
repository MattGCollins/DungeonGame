/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package environment;

import dungeon.Globals;
import engine.Range;
import entities.Player;
import java.awt.Point;
import java.util.Vector;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Matt
 */
public class Dungeon {
    public DungeonBlock[][] map;
    private static final Point dungeonSize = new Point(35, 35);
    private static final Point roomVariance = new Point(3, 3);
    private static final Point minRoomAdd = new Point(1, 1);
    public DungeonRoom[] rooms;
    public int totalRooms = 10;
    public DungeonRoom[] passages;
    
    public Dungeon()
    {
        init();
    }
    
    public void init()
    {
        map = new DungeonBlock[dungeonSize.x][dungeonSize.y];
        for(int x = 0; x < dungeonSize.x; ++x)
        {
            for(int y = 0; y < dungeonSize.y; ++y)
            {
                map[x][y] = new DungeonBlock(DungeonBlock.BRICK_TEXTURE,DungeonBlock.BRICK_TEXTURE,DungeonBlock.BRICK_TEXTURE,DungeonBlock.BRICK_TEXTURE,DungeonBlock.BRICK_TEXTURE);
                map[x][y].setPosition(x,y);
            }
        }
        
        do
        {
            createRooms();
            createPassages();
            
        }while(!checkMap());
        System.out.println("Map creation successful.");
    }
    
    public void createRooms()
    {
        rooms = new DungeonRoom[10];
        Point bottomLeft = new Point(), topRight = new Point();
        
        for(int i = 0; i < totalRooms; ++i)
        {
            DungeonRoom testRoom = new DungeonRoom();
            Point roomSize = new Point((2 * (Globals.rand.nextInt(roomVariance.x) + minRoomAdd.x)), (2 * (Globals.rand.nextInt(roomVariance.y) + minRoomAdd.y)));
            bottomLeft.setLocation((Globals.rand.nextInt((dungeonSize.x - (1 + roomSize.x))/2)*2)+1, (Globals.rand.nextInt((dungeonSize.y - (1 + roomSize.y))/2)*2)+1);
            topRight.setLocation(bottomLeft.x + roomSize.x, bottomLeft.y + roomSize.y);
            testRoom.set(bottomLeft.y, bottomLeft.x, topRight.y, topRight.x);
            
            boolean addRoom = true;
            for(int j = 0; j < i; ++j)
            {
                        DungeonRoom currentRoom = rooms[j];
                        //glPushMatrix();
                        if(currentRoom.collidePerimeter(testRoom))
                        {
                            addRoom = false;
                            j = i - 1;
                        }
                        //glPopMatrix();
            }
            
            if(addRoom)
            {
                rooms[i] = testRoom;
                imprintRoom(testRoom);
                makeDoors(testRoom);
            }
            else
            {
                --i;
            }
        }
    }
    
    private void imprintRoom(DungeonRoom imprint)
    {
        for(int x = imprint.bottomLeft.x - 1; x < imprint.topRight.x + 2; ++x)
        {
            for(int y = imprint.bottomLeft.y - 1; y < imprint.topRight.y + 2; ++y)
            {
                if((x < imprint.bottomLeft.x || x > imprint.topRight.x) || (y < imprint.bottomLeft.y || y > imprint.topRight.y))
                {
                    map[x][y].blockType = DungeonBlock.BLOCK_PERIM;
                }
                else
                {
                    map[x][y].blockType = DungeonBlock.BLOCK_ROOM;
                }
            }
        }
    }
    
    private void makeDoors(DungeonRoom room)
    {
        int tbDoors = (room.topRight.x - room.bottomLeft.x)/2 + 1;
        int sideDoors = (room.topRight.y - room.bottomLeft.y)/2 + 1;
        int numPossibleDoors = (tbDoors + sideDoors) * 2;
        Point[] possibleDoors = new Point[numPossibleDoors];
        int doorCount = 0;
        int doorsPresent = 0;
        
        
        for(int i = 0; i < tbDoors; ++i)
        {
            if(room.topRight.y < dungeonSize.y - 2)
            {
                if((map[room.bottomLeft.x + (2 * i)][room.topRight.y + 1].blockType & DungeonBlock.BLOCK_DOOR) != 0)
                {
                    doorsPresent += 1;
                }
                else if(((map[room.bottomLeft.x + (2 * i)][room.topRight.y + 1].blockType & DungeonBlock.BLOCK_PERIM) != 0) && ((map[room.bottomLeft.x + (2 * i)][room.topRight.y + 2].blockType & DungeonBlock.BLOCK_DOOR_CONNECT) != 0))
                {
                    possibleDoors[doorCount] = new Point(room.bottomLeft.x + (2 * i), room.topRight.y + 1);
                    doorCount += 1;
                }
            }
            
            if(room.bottomLeft.y > 1)
            {
                if((map[room.bottomLeft.x + (2 * i)][room.bottomLeft.y - 1].blockType & DungeonBlock.BLOCK_DOOR) != 0)
                {
                    doorsPresent += 1;
                }
                else if(((map[room.bottomLeft.x + (2 * i)][room.bottomLeft.y - 1].blockType & DungeonBlock.BLOCK_PERIM) != 0) && ((map[room.bottomLeft.x + (2 * i)][room.bottomLeft.y - 2].blockType & DungeonBlock.BLOCK_DOOR_CONNECT) != 0))
                {
                    possibleDoors[doorCount] = new Point(room.bottomLeft.x + (2 * i), room.bottomLeft.y - 1);
                    doorCount += 1;
                }
            }
        }
        
        for(int i = 0; i < sideDoors; ++i)
        {
            if(room.topRight.x < dungeonSize.x - 2)
            {
                if((map[room.topRight.x + 1][room.bottomLeft.y + (2 * i)].blockType & DungeonBlock.BLOCK_DOOR) != 0)
                {
                    doorsPresent += 1;
                }
                else if(((map[room.topRight.x + 1][room.bottomLeft.y + (2 * i)].blockType & DungeonBlock.BLOCK_PERIM) != 0) && ((map[room.topRight.x + 2][room.bottomLeft.y + (2 * i)].blockType & DungeonBlock.BLOCK_DOOR_CONNECT) != 0))
                {
                    possibleDoors[doorCount] = new Point(room.topRight.x + 1, room.bottomLeft.y + (2 * i));
                    doorCount += 1;
                }
            }
            
            if(room.bottomLeft.x > 1)
            {
                if((map[room.bottomLeft.x - 1][room.bottomLeft.y + (2 * i)].blockType & DungeonBlock.BLOCK_DOOR) != 0)
                {
                    doorsPresent += 1;
                }
                else if(((map[room.bottomLeft.x - 1][room.bottomLeft.y + (2 * i)].blockType & DungeonBlock.BLOCK_PERIM) != 0) && ((map[room.bottomLeft.x - 2][room.bottomLeft.y + (2 * i)].blockType & DungeonBlock.BLOCK_DOOR_CONNECT) != 0))
                {
                    possibleDoors[doorCount] = new Point(room.bottomLeft.x - 1, room.bottomLeft.y + (2 * i));
                    doorCount += 1;
                }
            }
            
            
        }
        
        setDoors(possibleDoors, doorCount, doorsPresent);
    }
    
    private void setDoors(Point[] possibleDoors, int doorCount, int doorsPresent)
    {
        int doorsToSetMax = (doorCount + doorsPresent) / 3;
        int doorsToSetMin = doorsToSetMax / 2;
        if(doorsToSetMin < 1)
            doorsToSetMin = 1;
        int doorsToSet;
        if(doorsPresent < doorsToSetMax)
        {
            
            if(doorsPresent > doorsToSetMin)
            {
                doorsToSet = Globals.rand.nextInt(doorsToSetMax - doorsPresent);
            }
            else if(doorsToSetMin == doorsToSetMax)
            {
                doorsToSet = doorsToSetMin;
            }
            else
            {
                doorsToSetMin -= doorsPresent;
                doorsToSet = Globals.rand.nextInt(doorsToSetMax - (doorsToSetMin)) + doorsToSetMin;
            }
            
            for(int i = 0; i < doorsToSet; ++i)
            {
                int doorIndex = Globals.rand.nextInt(doorCount);
                DungeonBlock doorBlock = map[possibleDoors[doorIndex].x][possibleDoors[doorIndex].y];
                if((doorBlock.blockType & DungeonBlock.BLOCK_DOOR) != 0)
                    --i;
                else
                    doorBlock.blockType = DungeonBlock.BLOCK_DOOR;
            }
        }
    }
    
    public void createPassages()
    {
        surveyPassage(1,1);
        createShortcuts(10);
    }
    
    public void surveyPassage(int x, int y)
    {
        //First get a list of directions
        int[] directions = {5,5,5,5};
        int i;
        for(i = 0; i < 4; ++i)
        {
            int tempDir = Globals.rand.nextInt(4);
            if(directions[0] != tempDir && directions[1] != tempDir && directions[2] != tempDir && directions[3] != tempDir)
            {
                directions[i] = tempDir;
            }
            else
            {
                --i;
            }
        }
        for(i = 0; i < 4; ++i)
        {
            testAndDig(x, y, directions[i]);
        }
    }
    
    public void testAndDig(int x, int y, int dir)
    {
        boolean digPassage = false;
        int newX = x, newY = y;
        
        
        switch(dir)
        {
            case 0:
                if((y + 2 < dungeonSize.y) && ((map[x][y+1].blockType & DungeonBlock.BLOCK_BLOCKED) == 0) && ((map[x][y+2].blockType & DungeonBlock.BLOCK_BLOCKED) == 0))
                {
                    if(map[x][y+1].blockType == DungeonBlock.BLOCK_SOLID)
                        map[x][y+1].blockType = DungeonBlock.BLOCK_PASSAGE;
                    else
                        map[x][y+1].blockType |= DungeonBlock.BLOCK_PASSAGE;
                    if(map[x][y+2].blockType == DungeonBlock.BLOCK_SOLID)
                        map[x][y+2].blockType = DungeonBlock.BLOCK_PASSAGE;
                    else
                        map[x][y+2].blockType |= DungeonBlock.BLOCK_PASSAGE;
                    digPassage = true;
                    newY = y + 2;
                }
                break;
            case 1:
                if((x + 2 < dungeonSize.x) && ((map[x+1][y].blockType & DungeonBlock.BLOCK_BLOCKED) == 0) && ((map[x+2][y].blockType & DungeonBlock.BLOCK_BLOCKED) == 0))
                {
                    if(map[x+1][y].blockType == DungeonBlock.BLOCK_SOLID)
                        map[x+1][y].blockType = DungeonBlock.BLOCK_PASSAGE;
                    else
                        map[x+1][y].blockType |= DungeonBlock.BLOCK_PASSAGE;
                    if(map[x+2][y].blockType == DungeonBlock.BLOCK_SOLID)
                        map[x+2][y].blockType = DungeonBlock.BLOCK_PASSAGE;
                    else
                        map[x+2][y].blockType |= DungeonBlock.BLOCK_PASSAGE;
                    digPassage = true;
                    newX = x + 2;
                }
                break;
            case 2:
                if((y - 2 > 0) && ((map[x][y-1].blockType & DungeonBlock.BLOCK_BLOCKED) == 0) && ((map[x][y-2].blockType & DungeonBlock.BLOCK_BLOCKED) == 0))
                {
                    if(map[x][y-1].blockType == DungeonBlock.BLOCK_SOLID)
                        map[x][y-1].blockType = DungeonBlock.BLOCK_PASSAGE;
                    else
                        map[x][y-1].blockType |= DungeonBlock.BLOCK_PASSAGE;
                    if(map[x][y-2].blockType == DungeonBlock.BLOCK_SOLID)
                        map[x][y-2].blockType = DungeonBlock.BLOCK_PASSAGE;
                    else
                        map[x][y-2].blockType |= DungeonBlock.BLOCK_PASSAGE;
                    digPassage = true;
                    newY = y - 2;
                }
                break;
            case 3:
                if((x - 2 > 0) && ((map[x-1][y].blockType & DungeonBlock.BLOCK_BLOCKED) == 0) && ((map[x-2][y].blockType & DungeonBlock.BLOCK_BLOCKED) == 0))
                {
                    if(map[x-1][y].blockType == DungeonBlock.BLOCK_SOLID)
                        map[x-1][y].blockType = DungeonBlock.BLOCK_PASSAGE;
                    else
                        map[x-1][y].blockType |= DungeonBlock.BLOCK_PASSAGE;
                    if(map[x-2][y].blockType == DungeonBlock.BLOCK_SOLID)
                        map[x-2][y].blockType = DungeonBlock.BLOCK_PASSAGE;
                    else
                        map[x-2][y].blockType |= DungeonBlock.BLOCK_PASSAGE;
                    digPassage = true;
                    newX = x - 2;
                }
                break;
        }
        if(digPassage)
            surveyPassage(newX,newY);
    }
    
    public void createShortcuts(int numShortcuts)
    {
        Point shortcutPos = new Point(0,0);
        for(int i = 0; i < numShortcuts; ++i)
        {
            boolean horizontal = Globals.rand.nextBoolean();
            if(horizontal)
            {
                shortcutPos.x = 2 + (Globals.rand.nextInt((dungeonSize.x - 5) / 2) * 2);
                shortcutPos.y = 1 + (Globals.rand.nextInt((dungeonSize.y - 3) / 2) * 2);
                if(map[shortcutPos.x][shortcutPos.y].blockType == DungeonBlock.BLOCK_SOLID &&
                        map[shortcutPos.x - 1][shortcutPos.y].blockType == DungeonBlock.BLOCK_PASSAGE &&
                        map[shortcutPos.x + 1][shortcutPos.y].blockType == DungeonBlock.BLOCK_PASSAGE)
                    map[shortcutPos.x][shortcutPos.y].blockType = DungeonBlock.BLOCK_PASSAGE;
                else
                    --i;
            }
            else
            {
                shortcutPos.x = 1 + (Globals.rand.nextInt((dungeonSize.x - 3) / 2) * 2);
                shortcutPos.y = 2 + (Globals.rand.nextInt((dungeonSize.y - 5) / 2) * 2);
                if(map[shortcutPos.x][shortcutPos.y].blockType == DungeonBlock.BLOCK_SOLID &&
                        map[shortcutPos.x][shortcutPos.y - 1].blockType == DungeonBlock.BLOCK_PASSAGE &&
                        map[shortcutPos.x][shortcutPos.y + 1].blockType == DungeonBlock.BLOCK_PASSAGE)
                    map[shortcutPos.x][shortcutPos.y].blockType = DungeonBlock.BLOCK_PASSAGE;
                else
                    --i;
            }
        }
    }
    
    public boolean checkMap()
    {
        boolean mapOkay = true;
        for(int i = 0; i < totalRooms; ++i)
        {
            if((map[rooms[i].bottomLeft.x][rooms[i].bottomLeft.y].blockType & (DungeonBlock.BLOCK_PASSAGE)) == 0)
            {
                mapOkay = false;
                System.out.println("Map creation failed.  Rooms unconnected to dungeon.");
                resetMap();
                i = totalRooms;
            }
        }
        return mapOkay;
    }
    
    public void resetMap()
    {
        for(int x = 0; x < dungeonSize.x; ++x)
        {
            for(int y = 0; y < dungeonSize.y; ++y)
            {
                map[x][y].blockType = DungeonBlock.BLOCK_SOLID;
            }
        }
    }
    
    public void render()
    {
        for(int x = 0; x <= dungeonSize.x; ++x)
        {
            if(x >= 0 && x < dungeonSize.x)
            {
                for(int y = 0; y <= dungeonSize.y; ++y)
                {
                    if(y >= 0 && y < dungeonSize.y)
                    {
                        //glPushMatrix();
                        GL11.glTranslated(x,0,-y);
                        map[x][y].render();
                        GL11.glTranslated(-x,0,y);
                        //glPopMatrix();
                        
                    }
                }
            }
        }
    }
    
    public void exploreSquare(int depth)
    {
        Player player = Globals.player;
        if(!map[player.position.x][player.position.y].traveledTo)
        {
            map[player.position.x][player.position.y].seen = true;
            map[player.position.x][player.position.y].traveledTo = true;
            int squareSize = depth * 2 + 1;
            Point startPos = new Point(player.position.x - depth, player.position.y - depth);
            LOSBlock[][] sightBlocks = new LOSBlock[squareSize][squareSize];
            for(int x = 0; x < squareSize; ++x)
            {
                sightBlocks[x] = new LOSBlock[squareSize];
                for(int y = 0; y < squareSize; ++y)
                {
                    sightBlocks[x][y] = new LOSBlock();
                    LOSBlock curBlock = sightBlocks[x][y];
                    Point pos = new Point(x + startPos.x, y + startPos.y);
                    curBlock.position.x = pos.x;
                    curBlock.position.y = pos.y;
                    if(pos.x >= 0 && pos.y >= 0 && pos.x < dungeonSize.x && pos.y < dungeonSize.y)
                    {
                        Point relPos = new Point(x - depth, y - depth);
                        if(((relPos.x * relPos.x) + (relPos.y * relPos.y)) <= (depth * depth))
                        {
                            curBlock.inRange = true;
                            curBlock.distanceSq = (relPos.x * relPos.x) + (relPos.y * relPos.y);
                            if((map[pos.x][pos.y].blockType & DungeonBlock.BLOCK_SOLID_LOOKING) != 0)
                                curBlock.blocking = true;
                            if(relPos.x != 0 || relPos.y != 0)
                            {
                                float newAngle = calculateAngle((float)relPos.x - 0.5f, (float)relPos.y - 0.5f);
                                curBlock.bounds.lower = Math.min(curBlock.bounds.lower, newAngle);
                                curBlock.bounds.upper = Math.max(curBlock.bounds.upper, newAngle);
                                newAngle = calculateAngle((float)relPos.x + 0.5f, (float)relPos.y - 0.5f);
                                curBlock.bounds.lower = Math.min(curBlock.bounds.lower, newAngle);
                                curBlock.bounds.upper = Math.max(curBlock.bounds.upper, newAngle);
                                newAngle = calculateAngle((float)relPos.x + 0.5f, (float)relPos.y + 0.5f);
                                curBlock.bounds.lower = Math.min(curBlock.bounds.lower, newAngle);
                                curBlock.bounds.upper = Math.max(curBlock.bounds.upper, newAngle);
                                newAngle = calculateAngle((float)relPos.x - 0.5f, (float)relPos.y + 0.5f);
                                curBlock.bounds.lower = Math.min(curBlock.bounds.lower, newAngle);
                                curBlock.bounds.upper = Math.max(curBlock.bounds.upper, newAngle);
                                if(curBlock.bounds.upper - curBlock.bounds.lower > 180)
                                {
                                    curBlock.bounds.lower = 360;
                                    curBlock.bounds.upper = -360;
                                    newAngle = calculateAngle((float)relPos.x - 0.5f, (float)relPos.y - 0.5f);
                                    if(newAngle > 180)
                                        newAngle -= 360;
                                    curBlock.bounds.lower = Math.min(curBlock.bounds.lower, newAngle);
                                    curBlock.bounds.upper = Math.max(curBlock.bounds.upper, newAngle);
                                    newAngle = calculateAngle((float)relPos.x + 0.5f, (float)relPos.y - 0.5f);
                                    if(newAngle > 180)
                                        newAngle -= 360;
                                    curBlock.bounds.lower = Math.min(curBlock.bounds.lower, newAngle);
                                    curBlock.bounds.upper = Math.max(curBlock.bounds.upper, newAngle);
                                    newAngle = calculateAngle((float)relPos.x + 0.5f, (float)relPos.y + 0.5f);
                                    if(newAngle > 180)
                                        newAngle -= 360;
                                    curBlock.bounds.lower = Math.min(curBlock.bounds.lower, newAngle);
                                    curBlock.bounds.upper = Math.max(curBlock.bounds.upper, newAngle);
                                    newAngle = calculateAngle((float)relPos.x - 0.5f, (float)relPos.y + 0.5f);
                                    if(newAngle > 180)
                                        newAngle -= 360;
                                    curBlock.bounds.lower = Math.min(curBlock.bounds.lower, newAngle);
                                    curBlock.bounds.upper = Math.max(curBlock.bounds.upper, newAngle);
                                    curBlock.crosses0 = true;
                                    curBlock.bounds2.upper = curBlock.bounds.upper + 360;
                                    curBlock.bounds2.lower = curBlock.bounds.lower + 360;
                                }
                            }
                        }
                    }
                }
            }
            for(int x = 0; x < squareSize; ++x)
            {
                for(int y = 0; y < squareSize; ++y)
                {
                    LOSBlock curBlock = sightBlocks[x][y];
                    if(curBlock.position.x >= 0 && curBlock.position.x < dungeonSize.x && curBlock.position.y >= 0 && curBlock.position.y < dungeonSize.y)
                        if(curBlock.inRange && checkLOS(curBlock, sightBlocks, squareSize))
                            map[curBlock.position.x][curBlock.position.y].seen = true;
                }
            }
        }
    }
    
    public static boolean checkLOS(LOSBlock curBlock, LOSBlock[][] blocks, int squareSize)
    {
        boolean seen = true;
        Vector<Range> sightRanges = new Vector<>();
        sightRanges.add(new Range(curBlock.bounds.lower,curBlock.bounds.upper));
        boolean useUpper = (curBlock.bounds.lower > 180);
        for(int x = 0; x < squareSize; ++x)
        {
            for(int y = 0; y < squareSize; ++y)
            {
                LOSBlock checkBlock = blocks[x][y];
                if(checkBlock.inRange && checkBlock.blocking && (checkBlock.distanceSq < curBlock.distanceSq))
                {
                    Range checkRange;
                    if(useUpper && checkBlock.crosses0)
                        checkRange = new Range(checkBlock.bounds2.lower, checkBlock.bounds2.upper);
                    else
                        checkRange = new Range(checkBlock.bounds.lower, checkBlock.bounds.upper);
                    for(int i = 0; i < sightRanges.size(); ++i)
                    {
                        Range changeRange = sightRanges.get(i);
                        if(checkRange.lower < changeRange.upper && checkRange.upper > changeRange.lower)
                        {
                            if(checkRange.upper < changeRange.upper)
                            {
                                if(checkRange.lower > changeRange.lower)
                                {
                                    //Split range into two ranges
                                    sightRanges.add(new Range(changeRange.lower, checkRange.lower));
                                    changeRange.lower = checkRange.upper;
                                    System.out.println("Things have changed.  Range is splitting.");
                                }
                                else
                                {
                                    changeRange.lower = checkRange.upper;
                                }
                            }
                            else if(checkRange.lower > changeRange.lower)
                            {
                                changeRange.upper = checkRange.lower;
                            }
                            else
                            {
                                sightRanges.remove(changeRange);
                            }
                        }
                        else
                        {
                        }
                    }
                }
                if(sightRanges.size() <= 0)
                {
                    x = squareSize;
                    y = squareSize;
                    seen = false;
                }
            }
        }
        return seen;
    }
    
    public static float calculateAngle(float x, float y)
    {
        float returnAngle;
        if(x > 0)
        {
            if(y > 0)
            {
                returnAngle = (float)(Math.atan(x/y) * 180.0f / Math.PI);
            }
            else
            {
                returnAngle = 180 - (float)(Math.atan(x/-y) * 180.0f / Math.PI);
            }
        }
        else
        {
            if(y < 0)
            {
                returnAngle = 180 + (float)(Math.atan(-x/-y) * 180.0f / Math.PI);
            }
            else
            {
                returnAngle = 360 - (float)(Math.atan(-x/y) * 180.0f / Math.PI);
            }
        }
        return returnAngle;
    }
    
    public void renderMap()
    {
        Player player = Globals.player;
        Point center = new Point(Globals.DISPLAY_WIDTH/2, Globals.DISPLAY_HEIGHT/2);
        int blockSize = Math.min(Globals.DISPLAY_WIDTH/dungeonSize.x, Globals.DISPLAY_HEIGHT/dungeonSize.y);
        Point halfMapSize = new Point((blockSize * dungeonSize.x) / 2, (blockSize * dungeonSize.y) / 2);
        Point bottomLeft = new Point(center.x - halfMapSize.x, center.y - halfMapSize.y);
        for(int x = 0; x <= dungeonSize.x; ++x)
        {
            if(x >= 0 && x < dungeonSize.x)
            {
                for(int y = 0; y <= dungeonSize.y; ++y)
                {
                    if(y >= 0 && y < dungeonSize.y)
                    {
                        Point position = new Point(bottomLeft.x + blockSize * x,bottomLeft.y + blockSize * y);
                        //glPushMatrix();
                        GL11.glTranslated(position.x,position.y,0);
                        map[x][y].renderMap(blockSize, ((x == player.position.x) && (y == player.position.y)));
                        GL11.glTranslated(-position.x,-position.y,0);
                        //glPopMatrix();
                        
                    }
                }
            }
        }
    }
    
    public void renderMiniMap()
    {
        Player player = Globals.player;
        int blockSize = Math.min(Globals.DISPLAY_WIDTH/dungeonSize.x, Globals.DISPLAY_HEIGHT/dungeonSize.y);
        Point miniMapBuffer = new Point(10,10);
        Point miniMapSize = new Point(4,4);
        Point bottomLeft = new Point(miniMapBuffer.x, Globals.DISPLAY_HEIGHT - (miniMapBuffer.y + (((2 * miniMapSize.y) + 1) * blockSize)));
        Point startBlock = new Point(Math.min(Math.max(0, player.position.x - miniMapSize.x), (dungeonSize.x - 1) - (miniMapSize.x * 2)),Math.min(Math.max(0, player.position.y - miniMapSize.y), (dungeonSize.y - 1) - (miniMapSize.y * 2)));
        for(int x = startBlock.x; x <= (startBlock.x + (2 * miniMapSize.x)); ++x)
        {
            for(int y = startBlock.y; y <= (startBlock.y + (2 * miniMapSize.y)); ++y)
            {
                Point position = new Point(bottomLeft.x + blockSize * (x - startBlock.x),bottomLeft.y + blockSize * (y - startBlock.y));
                //glPushMatrix();
                GL11.glTranslated(position.x,position.y,0);
                map[x][y].renderMap(blockSize, ((x == player.position.x) && (y == player.position.y)));
                GL11.glTranslated(-position.x,-position.y,0);
                //glPopMatrix();

            }
        }
    }
    
    public int getDungeonSizeX()
    {
        return dungeonSize.x;
    }
    
    public int getDungeonSizeY()
    {
        return dungeonSize.y;
    }
}
