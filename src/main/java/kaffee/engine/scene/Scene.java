package kaffee.engine.scene;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Scene
{
    public abstract void update(float deltaTime);

    public void initialize()
    {

    }
}
