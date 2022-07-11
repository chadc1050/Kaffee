package kaffee.engine.components;

import imgui.ImGui;
import kaffee.engine.Transform;
import kaffee.engine.renderer.Texture;
import kaffee.engine.renderer.sprites.Sprite;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{
    private Vector4f color = new Vector4f(1,1,1,1);
    private Sprite sprite = new Sprite();

    private Transform lastTransform;
    private boolean isDirty = true;

    @Override
    public void start()
    {
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void stop()
    {
        isDirty = true;
    }

    @Override
    public void update(float deltaTime)
    {
        if(!this.lastTransform.equals(this.gameObject.transform))
        {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void imGUI()
    {
        float[] imColor = {color.x, color.y, color.z, color.w};
        ImGui.colorPicker4("Color Picker: ", imColor);
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
        if(!this.color.equals(color))
        {
            this.isDirty = true;
            this.color.set(color);
        }
    }

    public void setSprite(Sprite sprite)
    {
        this.sprite = sprite;
        this.isDirty = true;
    }

    public boolean isDirty()
    {
        return this.isDirty;
    }

    public void setClean()
    {
        this.isDirty = false;
    }
}
