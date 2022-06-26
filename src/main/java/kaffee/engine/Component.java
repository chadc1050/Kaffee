package kaffee.engine;

public abstract class Component
{
    public GameObject gameObject = null;

    public void update(float deltaTime) {}

    public void start() {}

    public void imGUI() {}
}
