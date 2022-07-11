package kaffee.engine.renderer.sprites;

import kaffee.engine.renderer.Texture;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

@Getter
@Setter
public class Sprite
{
    private float width, height;

    private Texture texture = null;

    /**
     * These are the normalized coordinates that will tell the renderer where on the texture the sprite is located.
     */
    private Vector2f[] textureCoordinates  = new Vector2f[] {
            // Top Right
            new Vector2f(1,1),
            // Bottom right
            new Vector2f(1,0),
            // Bottom left
            new Vector2f(0,0),
            // Top left
            new Vector2f(0,1)
    };

    public int getTextureId()
    {
        return texture == null ? -1 : texture.getTextureID();
    }
}
