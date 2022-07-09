package kaffee.engine.renderer;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

@Getter
@Setter
public class Sprite
{
    private float width, height;

    private Texture texture = null;

    private Vector2f[] textureCoordinates  = new Vector2f[] {
            new Vector2f(1,1),
            new Vector2f(1,0),
            new Vector2f(0,0),
            new Vector2f(0,1)
    };

    public int getTextureId()
    {
        return texture == null ? -1 : texture.getTextureID();
    }
}
