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

    public Transform copy()
    {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(Transform to)
    {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Transform transform))
        {
            return false;
        }
        return transform.position.equals(this.position) && transform.scale.equals(this.scale);
    }
}
