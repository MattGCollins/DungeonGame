/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon;

import engine.InputKeeper;
import entities.Player;
import environment.Dungeon;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

/**
 *
 * @author Matt
 */
public class DungeonGame {

    public static final Logger LOGGER = Logger.getLogger(DungeonGame.class.getName());
    //private Dungeon currentDungeon;

    static {
        try {
        LOGGER.addHandler(new FileHandler("errors.log",true));
        }
        catch(IOException ex) {
        LOGGER.log(Level.WARNING,ex.toString(),ex);
        }
    }

    public static void main(String[] args) {
        DungeonGame main = null;
        try {
            System.out.println("Keys:");
            System.out.println("Down/S  - Move Backward");
            System.out.println("Up/W    - Move Forward");
            System.out.println("Left/Q  - Rotate Left");
            System.out.println("Right/E - Rotate Right");
            System.out.println("A  - Strafe Left");
            System.out.println("D - Strafe Right");
            System.out.println("X - Spin 180 Degrees");
            System.out.println("Esc   - Exit");
            main = new DungeonGame();
            main.create();
            main.run();
        }
        catch(Exception ex) {
            LOGGER.log(Level.SEVERE,ex.toString(),ex);
        }
        finally {
            if(main != null) {
                main.destroy();
            }
        }
    }

    public DungeonGame() {        
        Globals.input = new InputKeeper();
        Globals.curFrameTime = System.nanoTime() / 1000000;
    }

    public void create() throws LWJGLException {
        //Display
        try{
            Display.setDisplayMode(new DisplayMode(Globals.DISPLAY_WIDTH,Globals.DISPLAY_HEIGHT));
        }
        catch(LWJGLException o){
            System.out.println(o.getLocalizedMessage());
        }
        Display.setFullscreen(false);
        Display.setTitle("Dungeon!");
        Display.create();

        //Keyboard
        Keyboard.create();

        //Mouse
        Mouse.setGrabbed(false);
        Mouse.create();

        //OpenGL
        initGL();
        resizeGL();
    }

    public void destroy() {
        //Methods already check if created before destroying.
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

    public void initGL() {
        //2D Initialization
        glClearColor(0.0f,0.0f,0.0f,0.0f);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        /*try {
        wallTex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/Brick Wall.png"));
        }
        catch(IOException ex) {
        }*/
        
        Resources.init();
        
        //testBlock = new DungeonBlock(1,0,1,1,1);
        Globals.currentDungeon = new Dungeon();
        Globals.player = new Player();
        Globals.player.position = Globals.currentDungeon.rooms[1].bottomLeft;
        Globals.currentDungeon.exploreSquare(Globals.sightDepth);
        
        // enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glClearDepth (1.0f);
        glDepthFunc (GL_LEQUAL);
        glShadeModel (GL_SMOOTH);
        glHint (GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        //glHint (GL11.GL_M, GL_NICEST);
        glHint (GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        Globals.currentDungeon.renderMap();
    }

    public void render() {
        
        //3D Drawing
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        glPopMatrix();
        glLoadIdentity();
        GLU.gluPerspective(75,(float)Globals.DISPLAY_WIDTH/(float)Globals.DISPLAY_HEIGHT,0.01f,100);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        glPushMatrix();
        
        glClearColor(0.0f,0.0f,0.0f,0.0f);
        glClear(GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
        
        GL11.glColor3f(1.0f,1.0f,1.0f);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL_DEPTH_TEST);
        
        //Move the world to the player
        glPushMatrix();
        GL11.glTranslated(0,0,-0.4);
        glPushMatrix();
        GL11.glRotated((double)Globals.player.facing * 90 + Globals.player.rotationDif,0,1,0);
        glPushMatrix();
        GL11.glTranslated(-Globals.player.position.x,0,Globals.player.position.y);
        GL11.glTranslated(-Globals.player.positionDif.getX(),0,Globals.player.positionDif.getY());
        
        // draw map
        //testBlock.render();
        Globals.currentDungeon.render();
        
        glPopMatrix();
        glPopMatrix();
        glPopMatrix();
        
        GL11.glDisable(GL_DEPTH_TEST);

        //2D HUD Drawing
        
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glLoadIdentity();
        gluOrtho2D(0.0f,Globals.DISPLAY_WIDTH,0.0f,Globals.DISPLAY_HEIGHT);
        glPushMatrix();

        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        if(Globals.mapOn)
            Globals.currentDungeon.renderMap();
        else if(Globals.miniMapOn)
            Globals.currentDungeon.renderMiniMap();
        glPopMatrix();
    }

    public void resizeGL() {
        //2D Scene
        glViewport(0,0,Globals.DISPLAY_WIDTH,Globals.DISPLAY_HEIGHT);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0.0f,Globals.DISPLAY_WIDTH,0.0f,Globals.DISPLAY_HEIGHT);
        glPushMatrix();

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glPushMatrix();
    }

    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            if(Display.isVisible()) {
                Globals.input.update();
                update();
                render();
            }
            else {
                if(Display.isDirty()) {
                render();
                }
                try {
                Thread.sleep(100);
                }
                catch(InterruptedException ex) {
                }
            }
            Display.update();
            Display.sync(60);
        }
    }

    public void update() {
        long currentTime = System.nanoTime() / 1000000;
        if(currentTime <= Globals.curFrameTime)
        {
            currentTime += 1;
        }
        Globals.frameElapsed = (currentTime - Globals.curFrameTime) / 1000.0f;
        Globals.curFrameTime = currentTime;
        
        Globals.player.update();
    }
}
