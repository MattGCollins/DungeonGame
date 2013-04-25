/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

/**
 *
 * @author Matt
 */
public class Range {
    public float lower;
    public float upper;
    
    public Range()
    {
        lower = 0;
        upper = 0;
    }
    
    public Range(float lowerIn, float upperIn)
    {
        lower = lowerIn;
        upper = upperIn;
    }
}
