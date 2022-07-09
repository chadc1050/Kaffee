package kaffee.engine.util;

import com.google.gson.*;
import kaffee.engine.components.Component;

import java.lang.reflect.Type;

public class ComponentDeserializer implements JsonDeserializer<Component>, JsonSerializer<Component>
{

    @Override
    public Component deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try
        {
            return jsonDeserializationContext.deserialize(element, Class.forName(type));
        }
        catch (ClassNotFoundException e)
        {
            throw new JsonParseException("Could not deserialize class of type: '" + type + "'");
        }
    }

    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext)
    {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(component.getClass().getCanonicalName()));
        result.add("properties", jsonSerializationContext.serialize(component, component.getClass()));
        return result;
    }
}
