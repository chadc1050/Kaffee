package kaffee.engine.scene;

import kaffee.engine.Camera;
import kaffee.engine.GameObject;
import kaffee.engine.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene
{
    protected Camera camera;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected Renderer renderer = new Renderer();

    private boolean isRunning = false;

    public Scene() {}

    public void start()
    {
        for(GameObject gameObject : gameObjects)
        {
            gameObject.start();
            this.renderer.add(gameObject);
        }
        isRunning = true;
    }

    public abstract void update(float deltaTime);

    public void initialize() {}

    public void addGameObjectToScene(GameObject gameObject)
    {
        if(!isRunning)
        {
            gameObjects.add(gameObject);
        }
        else {
            gameObjects.add(gameObject);
            gameObject.start();
            this.renderer.add(gameObject);
        }
    }

    public Camera getCamera()
    {
        return this.camera;
    }
}
