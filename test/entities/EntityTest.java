/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import dungeon.Globals;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Matt
 */
public class EntityTest {
    Entity testedClass;
    
    @Before
    public void setUp() {
        testedClass = new Entity();
    }
    
    @After
    public void tearDown() {
        testedClass = null;
    }

    /**
     * Test of moveTo method, of class Entity.
     */
    @Test
    public void testMoveTo() {
        System.out.println("moveTo");
        int x = 4;
        int y = 5;
        assertEquals(testedClass.position.x, 0);
        assertEquals(testedClass.position.y, 0);
        
        testedClass.moveTo(x, y);
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 5);
    }

    /**
     * Test of moveForward method, of class Entity.
     */
    @Test
    public void testMoveForward_NorthSouth() {
        System.out.println("moveForward_NorthSouth");
        testedClass.facing = Globals.FACING_NORTH;
        testedClass.moveTo(4, 5);
        
        testedClass.moveForward();
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 6);
        
        testedClass.facing = Globals.FACING_SOUTH;
        
        testedClass.moveForward();
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 5);
    }

    /**
     * Test of moveForward method, of class Entity.
     */
    @Test
    public void testMoveForward_EastWest() {
        System.out.println("moveForward_EastWest");
        testedClass.facing = Globals.FACING_EAST;
        testedClass.moveTo(4, 5);
        
        testedClass.moveForward();
        
        assertEquals(testedClass.position.x, 5);
        assertEquals(testedClass.position.y, 5);
        
        testedClass.facing = Globals.FACING_WEST;
        
        testedClass.moveForward();
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 5);
    }


    /**
     * Test of moveBackward method, of class Entity.
     */
    @Test
    public void testMoveBackward_NorthSouth() {
        System.out.println("moveBackward_NorthSouth");
        testedClass.facing = Globals.FACING_NORTH;
        testedClass.moveTo(4, 5);
        
        testedClass.moveBackward();
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 4);
        
        testedClass.facing = Globals.FACING_SOUTH;
        
        testedClass.moveBackward();
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 5);
    }

    /**
     * Test of moveBackward method, of class Entity.
     */
    @Test
    public void testMoveBackward_EastWest() {
        System.out.println("moveBackward_EastWest");
        testedClass.facing = Globals.FACING_EAST;
        testedClass.moveTo(4, 5);
        
        testedClass.moveBackward();
        
        assertEquals(testedClass.position.x, 3);
        assertEquals(testedClass.position.y, 5);
        
        testedClass.facing = Globals.FACING_WEST;
        
        testedClass.moveBackward();
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 5);
    }


    /**
     * Test of moveLeft method, of class Entity.
     */
    @Test
    public void testMoveLeft_NorthSouth() {
        System.out.println("moveLeft_NorthSouth");
        testedClass.facing = Globals.FACING_NORTH;
        testedClass.moveTo(4, 5);
        
        testedClass.moveLeft();
        
        assertEquals(testedClass.position.x, 3);
        assertEquals(testedClass.position.y, 5);
        
        testedClass.facing = Globals.FACING_SOUTH;
        
        testedClass.moveLeft();
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 5);
    }

    /**
     * Test of moveLeft method, of class Entity.
     */
    @Test
    public void testMoveLeft_EastWest() {
        System.out.println("moveLeft_EastWest");
        testedClass.facing = Globals.FACING_EAST;
        testedClass.moveTo(4, 5);
        
        testedClass.moveLeft();
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 6);
        
        testedClass.facing = Globals.FACING_WEST;
        
        testedClass.moveLeft();
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 5);
    }


    /**
     * Test of moveRight method, of class Entity.
     */
    @Test
    public void testMoveRight_NorthSouth() {
        System.out.println("moveRight_NorthSouth");
        testedClass.facing = Globals.FACING_NORTH;
        testedClass.moveTo(4, 5);
        
        testedClass.moveRight();
        
        assertEquals(testedClass.position.x, 5);
        assertEquals(testedClass.position.y, 5);
        
        testedClass.facing = Globals.FACING_SOUTH;
        
        testedClass.moveRight();
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 5);
    }

    /**
     * Test of moveRight method, of class Entity.
     */
    @Test
    public void testMoveRight_EastWest() {
        System.out.println("moveRight_EastWest");
        testedClass.facing = Globals.FACING_EAST;
        testedClass.moveTo(4, 5);
        
        testedClass.moveRight();
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 4);
        
        testedClass.facing = Globals.FACING_WEST;
        
        testedClass.moveRight();
        
        assertEquals(testedClass.position.x, 4);
        assertEquals(testedClass.position.y, 5);
    }

    /**
     * Test of turnLeft method, of class Entity.
     */
    @Test
    public void testTurnLeft() {
        System.out.println("turnLeft");
        testedClass.facing = Globals.FACING_EAST;
        testedClass.turnLeft();
        assertEquals(testedClass.facing, Globals.FACING_NORTH);
        testedClass.turnLeft();
        assertEquals(testedClass.facing, Globals.FACING_WEST);
        testedClass.turnLeft();
        assertEquals(testedClass.facing, Globals.FACING_SOUTH);
        testedClass.turnLeft();
        assertEquals(testedClass.facing, Globals.FACING_EAST);
    }

    /**
     * Test of turnRight method, of class Entity.
     */
    @Test
    public void testTurnRight() {
        System.out.println("turnRight");
        testedClass.facing = Globals.FACING_EAST;
        testedClass.turnRight();
        assertEquals(testedClass.facing, Globals.FACING_SOUTH);
        testedClass.turnRight();
        assertEquals(testedClass.facing, Globals.FACING_WEST);
        testedClass.turnRight();
        assertEquals(testedClass.facing, Globals.FACING_NORTH);
        testedClass.turnRight();
        assertEquals(testedClass.facing, Globals.FACING_EAST);
    }

    /**
     * Test of spin180 method, of class Entity.
     */
    @Test
    public void testSpin180() {
        System.out.println("spin180");
        testedClass.facing = Globals.FACING_EAST;
        testedClass.spin180();
        assertEquals(testedClass.facing, Globals.FACING_WEST);
        testedClass.spin180();
        assertEquals(testedClass.facing, Globals.FACING_EAST);
        
        testedClass.facing = Globals.FACING_NORTH;
        testedClass.spin180();
        assertEquals(testedClass.facing, Globals.FACING_SOUTH);
        testedClass.spin180();
        assertEquals(testedClass.facing, Globals.FACING_NORTH);
    }
}
