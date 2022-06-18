package kaffee.engine;

import org.joml.Vector2f;

public class Transform
{

    public Vector2f position;
    public Vector2f scale;

    public Transform()
    {
        initialize(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position)
    {
        initialize(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale)
    {
        initialize(position, scale);
    }

    private void initialize(Vector2f position, Vector2f scale)
    {
        this.position = position;
        this.scale = scale;
    }
}