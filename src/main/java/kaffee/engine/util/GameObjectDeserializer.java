package kaffee.engine.util;

import com.google.gson.*;
import kaffee.engine.GameObject;
import kaffee.engine.Transform;
import kaffee.engine.components.Component;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject>
{
    @Override
    public GameObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");
        Transform transform = jsonDeserializationContext.deserialize(jsonObject.get("transform"), Transform.class);
        int zIndex = jsonDeserializationContext.deserialize(jsonObject.get("zIndex"), int.class);

        GameObject gameObject = new GameObject(name, transform, zIndex);
        for (JsonElement element : components)
        {
            Component component = jsonDeserializationContext.deserialize(element, Component.class);
            gameObject.addComponent(component);
        }

        return gameObject;
    }
}
