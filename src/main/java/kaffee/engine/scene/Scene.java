package kaffee.engine;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Scene
{
    public abstract void update(float deltaTime);
}
