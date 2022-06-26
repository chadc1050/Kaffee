package kaffee.engine.scene;

import imgui.ImGui;
import kaffee.engine.Camera;
import kaffee.engine.GameObject;
import kaffee.engine.Transform;
import kaffee.engine.components.SpriteRenderer;
import kaffee.engine.components.SpriteSheet;
import kaffee.engine.util.AssetPool;
import org.joml.Vector2f;

public class LevelEditorScene extends Scene
{
    private GameObject obj1;
    public LevelEditorScene()
    {
    }

    @Override
    public void initialize()
    {
        loadResources();

        this.camera = new Camera(new Vector2f(0, 0));

        SpriteSheet sprites = AssetPool.getSpriteSheet("assets/sprites/kaffee/Stardew Valley - Dog Blonde.png");

        this.obj1 = new GameObject("Dog6", new Transform(new Vector2f(650, 50), new Vector2f(100, 100)), 1);

        this.obj1.addComponent(new SpriteRenderer(sprites.getSprite(34)));

        this.addGameObjectsToScene(this.obj1);
    }

    private void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/sprites/kaffee/Stardew Valley - Dog Blonde.png",
                new SpriteSheet(AssetPool.getTexture("assets/sprites/kaffee/Stardew Valley - Dog Blonde.png"),32, 32, 36, 0));
    }

    @Override
    public void update(float deltaTime)
    {
        obj1.transform.position.x += 10 * deltaTime;

        for(GameObject gameObject: this.gameObjects)
        {
            gameObject.update(deltaTime);
        }
        this.renderer.render();
    }

    @Override
    public void imGUI()
    {
        ImGui.begin("Test Window.");
        ImGui.text("Some random text.");
        ImGui.end();
    }
}
