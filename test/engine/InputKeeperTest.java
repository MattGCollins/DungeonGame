/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Matt
 */
public class InputKeeperTest {
    
    private InputKeeper testedClass;
    
    @Before
    public void setUp() {
        testedClass = new InputKeeper();
    }
    
    @After
    public void tearDown() {
        testedClass = null;
    }

    /**
     * Test of addCheck method, of class InputKeeper.
     */
    @Test
    public void testAddCheck() {
        System.out.println("addCheck");
        int i = 32;
        assertEquals(testedClass.keysToCheck.size(), 0);
        
        testedClass.addCheck(i);
        
        assertEquals(testedClass.keysToCheck.size(), 1);
    }

    /**
     * Test of removeCheck method, of class InputKeeper.
     */
    @Test
    public void testRemoveCheck() {
        System.out.println("removeCheck");
        int i = 32;
        assertEquals(testedClass.keysToCheck.size(), 0);
        
        testedClass.addCheck(i);
        
        assertEquals(testedClass.keysToCheck.size(), 1);
        
        testedClass.removeCheck(i);
        
        assertEquals(testedClass.keysToCheck.size(), 0);
    }
}
