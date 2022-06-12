package kaffee.engine.scene;

import kaffee.engine.Camera;
import kaffee.engine.GameObject;
import kaffee.engine.Transform;
import kaffee.engine.components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene
{
    public LevelEditorScene()
    {
    }

    @Override
    public void initialize()
    {
        this.camera = new Camera(new Vector2f());

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = ((float) (600 - xOffset * 2));
        float totalHeight = ((float) (300 - yOffset * 2));
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for(int x = 0; x < 100; x++)
        {
            for(int y = 0; y < 100; y++)
            {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject gameObject = new GameObject("Obj" + x + " " + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                gameObject.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObjectToScene(gameObject);
            }
        }
    }

    @Override
    public void update(float deltaTime)
    {
        for(GameObject gameObject: this.gameObjects)
        {
            gameObject.update(deltaTime);
        }
        this.renderer.render();
    }
}
