package kaffee.engine.renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{
    private int textureID;
    private String filePath;
    private int width, height;

    public Texture(String filePath)
    {
        this.filePath = filePath;

        // Generate texture on GPU
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Set Texture Parameters
        // Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // Pixelating (Since this isn't supposed to be a realism engine)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // Textures are inverted on load, flip them
        stbi_set_flip_vertically_on_load(true);

        ByteBuffer image = stbi_load(filePath, width, height, channels, 0);

        if(image != null)
        {
            this.width = width.get(0);
            this.height = height.get(0);

            if(channels.get(0) == 3)
            {
                System.out.println("INFO: Texture: " + filePath + " is using 3 color channels.");
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }
            else if(channels.get(0) == 4)
            {
                System.out.println("INFO: Texture: " + filePath + " is using 4 color channels.");
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }
            else
            {
                assert false: "ERROR: Number of channels for file, '" + filePath + "' is not supported.";
            }
        }
        else
        {
            assert false: "ERROR: Texture could not load image: " + filePath;
        }

        stbi_image_free(image);
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public String getFilePath()
    {
        return filePath;
    }
}
