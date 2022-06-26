package kaffee.engine.scene;

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

        GameObject gameObject = new GameObject("Dog0", new Transform(new Vector2f(50, 50), new Vector2f(100, 100)));
        GameObject gameObject1 = new GameObject("Dog1", new Transform(new Vector2f(150, 50), new Vector2f(100, 100)));
        GameObject gameObject2 = new GameObject("Dog2", new Transform(new Vector2f(250, 50), new Vector2f(100, 100)));
        GameObject gameObject3 = new GameObject("Dog3", new Transform(new Vector2f(350, 50), new Vector2f(100, 100)));
        GameObject gameObject4 = new GameObject("Dog4", new Transform(new Vector2f(450, 50), new Vector2f(100, 100)));
        GameObject gameObject5 = new GameObject("Dog5", new Transform(new Vector2f(550, 50), new Vector2f(100, 100)));
        this.obj1 = new GameObject("Dog6", new Transform(new Vector2f(650, 50), new Vector2f(100, 100)));

        gameObject.addComponent(new SpriteRenderer(sprites.getSprite(28)));
        gameObject1.addComponent(new SpriteRenderer(sprites.getSprite(29)));
        gameObject2.addComponent(new SpriteRenderer(sprites.getSprite(30)));
        gameObject3.addComponent(new SpriteRenderer(sprites.getSprite(31)));
        gameObject4.addComponent(new SpriteRenderer(sprites.getSprite(32)));
        gameObject5.addComponent(new SpriteRenderer(sprites.getSprite(33)));
        this.obj1.addComponent(new SpriteRenderer(sprites.getSprite(34)));

        this.addGameObjectsToScene(gameObject, gameObject1, gameObject2, gameObject3, gameObject4, gameObject5, this.obj1);
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
}
