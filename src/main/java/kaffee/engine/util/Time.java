package kaffee.engine.util;

import lombok.experimental.UtilityClass;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

@UtilityClass
public class Time
{
    public static float getTime()
    {
        return ((float) glfwGetTime());
    }
}
