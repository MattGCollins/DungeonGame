/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Matt
 */
public class RangeTest {

    @Test
    public void testNewRange_emptyConstructor() {
        Range range = new Range();
        assertEquals(range.lower, 0.0f, 0.001f);
        assertEquals(range.upper, 0.0f, 0.001f);
    }

    @Test
    public void testNewRange_smallRange() {
        Range range = new Range(1.0f, 2.0f);
        assertEquals(range.lower, 1.0f, 0.001f);
        assertEquals(range.upper, 2.0f, 0.001f);
    }

    @Test
    public void testNewRange_largeRange() {
        Range range = new Range(1.451f, 499.374f);
        assertEquals(range.lower, 1.451f, 0.001f);
        assertEquals(range.upper, 499.374f, 0.001f);
    }
}
