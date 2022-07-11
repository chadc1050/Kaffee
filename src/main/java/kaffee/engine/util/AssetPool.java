package kaffee.engine.util;

import kaffee.engine.renderer.Shader;
import kaffee.engine.renderer.sprites.SpriteSheet;
import kaffee.engine.renderer.Texture;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

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

        Shader shader = new Shader(resourceName);
        shader.compile();
        AssetPool.shaders.put(file.getAbsolutePath(), shader);
        return shader;

    }

    public static List<Shader> getAllShaders(String directory)
    {
        return Arrays.stream(Objects.requireNonNull(new File(directory).listFiles()))
                .filter(file -> file != null && file.getName().endsWith(".glsl"))
                .map(file -> getShader(file.getAbsolutePath()))
                .collect(Collectors.toList());
    }

    public static Texture getTexture(String resourceName)
    {
        File file = new File(resourceName);
        if(AssetPool.textures.containsKey(file.getAbsolutePath()))
        {
            return AssetPool.textures.get(file.getAbsolutePath());
        }

        Texture texture = new Texture();
        texture.initialize(resourceName);
        AssetPool.textures.put(file.getAbsolutePath(), texture);
        return texture;
    }

    public static List<Texture> getAllTextures(String directory)
    {
        return Arrays.stream(Objects.requireNonNull(new File(directory).listFiles()))
                .filter(Objects::nonNull)
                .map(file -> getTexture(file.getAbsolutePath()))
                .collect(Collectors.toList());
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
