package kaffee.engine.components;

import kaffee.engine.Component;
import kaffee.engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{

    private Vector4f color;
    private Vector2f[] textureCoordinates;
    private Texture texture;
    private Sprite sprite;

    private boolean firstTime = false;

    public SpriteRenderer(Vector4f color)
    {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite)
    {
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
    }

    @Override
    public void start()
    {

    }

    @Override
    public void update(float deltaTime) {
        if(!firstTime)
        {
            firstTime = true;
        }
    }

    public Texture getTexture()
    {
        return sprite.getTexture();
    }

    public Vector2f[] getTextureCoordinates()
    {
        return sprite.getTextureCoordinates();
    }

    public Vector4f getColor()
    {
        return color;
    }

    public void setColor(Vector4f color)
    {
        this.color = color;
    }
}
