package kaffee.engine.components;

import kaffee.engine.GameObject;
import kaffee.engine.Transform;
import kaffee.engine.renderer.Sprite;
import kaffee.engine.util.Time;
import org.joml.Vector2f;

public class GameObjectGenerator
{
    public static GameObject generateSprite(Sprite sprite, float sizeX, float sizeY)
    {
        GameObject gameObject = new GameObject(
                "Generated_Game_Object_" + Time.getTime(),
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)),
                0);

        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        gameObject.addComponent(renderer);

        return gameObject;
    }
}
