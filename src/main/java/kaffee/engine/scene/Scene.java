package kaffee.engine.scene;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import kaffee.engine.Camera;
import kaffee.engine.GameObject;
import kaffee.engine.components.Component;
import kaffee.engine.renderer.Renderer;
import kaffee.engine.components.ComponentDeserializer;
import kaffee.engine.util.GameObjectDeserializer;
import org.codehaus.plexus.util.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene
{
    protected Camera camera;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected Renderer renderer = new Renderer();
    protected GameObject activeGameObject = null;

    private boolean isRunning = false;
    protected boolean loadedLevel = false;

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

    public void addGameObjectsToCurrentScene(GameObject... gameObjectsToAdd)
    {
        for (GameObject gameObject : gameObjectsToAdd)
        {
            if(!isRunning)
            {
                gameObjects.add(gameObject);
            }
            else
            {
                gameObject.start();
                gameObjects.add(gameObject);
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

    public void save(String filePath)
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try(FileWriter fileWriter = new FileWriter(filePath))
        {
            fileWriter.write(gson.toJson(this.gameObjects));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveExit()
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try(FileWriter fileWriter = new FileWriter("assets/cachedLevelData.txt"))
        {
            fileWriter.write(gson.toJson(this.gameObjects));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void load()
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try
        {
            inFile = Files.readString(Paths.get("assets/cachedLevelData.txt"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(!StringUtils.isEmpty(inFile))
        {
            addGameObjectsToCurrentScene(gson.fromJson(inFile, GameObject[].class));
        }

        this.loadedLevel = true;
    }
}
