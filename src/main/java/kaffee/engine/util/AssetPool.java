package kaffee.engine.util;

import kaffee.engine.components.SpriteSheet;
import kaffee.engine.renderer.Shader;
import kaffee.engine.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Used for interfacing with the assets package, loads in and stores assets for fast and easy access.
 */
public class AssetPool
{
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();


    /**
     * Retrieves shader from asset pool.
     * @param resourceName
     *      File path of Shader resource.
     * @return
     *      Shader resource located at directory location.
     */
    public static Shader getShader(String resourceName)
    {
        File file = new File(resourceName);
        if(AssetPool.shaders.containsKey(file.getAbsolutePath()))
        {
            return AssetPool.shaders.get(file.getAbsolutePath());
        }
        else
        {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName)
    {
        File file = new File(resourceName);
        if(AssetPool.textures.containsKey(file.getAbsolutePath()))
        {
            return AssetPool.textures.get(file.getAbsolutePath());
        }
        else
        {
            Texture texture = new Texture(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet)
    {
        File file = new File(resourceName);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath()))
        {
            AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String resourceName)
    {
        File file = new File(resourceName);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath()))
        {
            throw new IllegalArgumentException("ERROR: Could not find sprite sheet with resource name: " + resourceName);
        }
        return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(), null);
    }
}
