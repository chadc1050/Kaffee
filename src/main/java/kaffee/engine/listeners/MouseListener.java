package kaffee.engine.listeners;

import kaffee.engine.Window;
import org.joml.Vector4f;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener
{
    private static MouseListener instance;
    private double xScroll;
    private double yScroll;
    private double xPos;
    private double yPos;
    private double xLast;
    private double yLast;
    private boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    private MouseListener()
    {
        this.xScroll = 0.0;
        this.yScroll = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.xLast = 0.0;
        this.yLast = 0.0;
    }

    public static MouseListener get()
    {
        if(Objects.isNull(instance))
        {
            instance = new MouseListener();
        }
        return instance;
    }

    public static void mousePositionCallback(long window, double xPos, double yPos)
    {
        get().xLast = get().xPos;
        get().yLast = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
        get().isDragging = isAnyMouseButtonPressed();
    }

    public static void mouseButtonCallback(long window, int button, int action, int modifier)
    {
        if(isValidMouseButton(button))
        {
            if(action == GLFW_PRESS)
            {
                get().mouseButtonPressed[button] = true;
            }
            else if (action == GLFW_RELEASE)
            {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset)
    {
        get().xScroll = xOffset;
        get().yScroll = yOffset;
    }

    public static void endFrame()
    {
        get().xScroll = 0.0;
        get().yScroll = 0.0;
        get().xLast = get().xPos;
        get().yLast = get().yPos;
    }

    private static boolean isAnyMouseButtonPressed()
    {
        for(boolean button : get().mouseButtonPressed)
        {
            if(button)
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isMouseButtonPressed(int button)
    {
        return isValidMouseButton(button) && get().mouseButtonPressed[button];
    }

    public static boolean isValidMouseButton(int button)
    {
        return button < get().mouseButtonPressed.length;
    }

    public static float getOrthogonalX()
    {
        float currentX = (getXPos() / (float) Window.getWidth()) * 2.0f - 1.0f;
        return new Vector4f(currentX, 0, 0, 1)
                .mul(Window.getCurrentScene().getCamera().getInverseProjection())
                .mul(Window.getCurrentScene().getCamera().getInverseView())
                .x;
    }

    public static float getOrthogonalY()
    {
        float currentY = ((Window.getHeight() - getYPos()) / (float) Window.getHeight()) * 2.0f - 1.0f;
        return new Vector4f(0, currentY, 0, 1).mul(Window.getCurrentScene().getCamera().getInverseProjection())
                .mul(Window.getCurrentScene().getCamera().getInverseView())
                .y;
    }

    public static float getXPos()
    {
        return ((float) get().xPos);
    }

    public static float getYPos()
    {
        return ((float) get().yPos);
    }

    public static float getDX()
    {
        return ((float) (get().xPos - get().xLast));
    }

    public static float getDY()
    {
        return ((float) (get().yPos - get().yLast));
    }

    public static float getXScroll()
    {
        return ((float) get().xScroll);
    }

    public static float geYScroll()
    {
        return ((float) get().yScroll);
    }

    public static boolean isDragging()
    {
        return get().isDragging;
    }
}
