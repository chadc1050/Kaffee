package kaffee.engine.util;

import lombok.experimental.UtilityClass;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

@UtilityClass
public class Time
{
    public static final float TIME_STARTED = System.nanoTime();

    public static float getTime()
    {
        return ((float) glfwGetTime());
    }
}
