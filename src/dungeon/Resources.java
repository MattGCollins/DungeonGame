/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.MipMap;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Matt
 */
public class Resources {
    public static Texture[] textures;
    
    public static void init()
    {
        textures = new Texture[3];
        int components, width, height;
        
        try {textures[0] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/Empty Space.png"), GL11.GL_LINEAR_MIPMAP_LINEAR);}
        catch(IOException ex) {}
        loadTexture(1, "assets/Brick Wall.png", "PNG");
        loadTexture(2, "assets/Tile Floor.png", "PNG");
    }
    
    public static void loadTexture(int index, String filename, String format)
    {
        int components, width, height;
        try {textures[index] = TextureLoader.getTexture(format, ResourceLoader.getResourceAsStream(filename), GL11.GL_LINEAR_MIPMAP_LINEAR);}
        catch(IOException ex) {}
        //textures[2].bind();        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL14.GL_MIN,GL11.GL_LINEAR_MIPMAP_LINEAR);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL14.GL_GENERATE_MIPMAP,GL11.GL_TRUE);
        ByteBuffer textureData = ByteBuffer.allocateDirect(textures[index].getTextureData().length);
        textureData.put(textures[index].getTextureData(), 0, textures[index].getTextureData().length);
        textureData.rewind();
        width = textures[index].getImageWidth();
        height = textures[index].getImageHeight();
        components = textures[index].getTextureData().length / (width * height);
        MipMap.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, components, width, height, components == 3 ? GL11.GL_RGB : GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureData);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 8);
    }
}
