package kaffee.engine;

public abstract class Component
{
    public GameObject gameObject = null;

    public abstract void update(float deltaTime);

    public void start() {}
}
