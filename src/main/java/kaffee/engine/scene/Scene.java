package kaffee.engine.scene;

import imgui.ImGui;
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
    protected GameObject activeGameObject = null;

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

    public void addGameObjectsToScene(GameObject... gameObjectsToAdd)
    {
        for (GameObject gameObject : gameObjectsToAdd)
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
    }

    public void sceneIMGUI()
    {
        if(activeGameObject != null)
        {
            ImGui.begin("Inspector");
            activeGameObject.imGUI();
            ImGui.end();
        }

        imGUI();
    }

    public void imGUI()
    {

    }

    public Camera getCamera()
    {
        return this.camera;
    }
}
