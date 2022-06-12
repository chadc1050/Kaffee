package kaffee.engine.components;

import kaffee.engine.Component;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{

    private Vector4f color;

    private boolean firstTime = false;

    public SpriteRenderer(Vector4f color)
    {
        this.color = color;
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

    public Vector4f getColor()
    {
        return color;
    }

    public void setColor(Vector4f color)
    {
        this.color = color;
    }
}
