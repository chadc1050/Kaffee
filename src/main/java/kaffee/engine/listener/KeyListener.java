package kaffee.engine.listener;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyListener
{
    private static KeyListener instance;
    private boolean[] keysPressed = new boolean[350];

    public static KeyListener get()
    {
        if(KeyListener.instance == null)
        {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scanCode, int action, int modifiers)
    {
        if(action == GLFW_PRESS)
        {
            get().keysPressed[key] = true;
        }
        else if (action == GLFW_RELEASE)
        {
            get().keysPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode)
    {
        return isValidKeyCode(keyCode) && get().keysPressed[keyCode];
    }

    public static boolean isValidKeyCode(int keyCode)
    {
        return keyCode < get().keysPressed.length;
    }
}
