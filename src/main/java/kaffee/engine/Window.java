package kaffee.engine;

import kaffee.engine.listener.KeyListener;
import kaffee.engine.listener.MouseListener;
import kaffee.engine.scene.LevelEditorScene;
import kaffee.engine.scene.LevelScene;
import kaffee.engine.scene.Scene;
import kaffee.engine.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    public float r, g, b = 0.0f;
    public float alpha = 1.0f;

    private int width, height;
    private String title;
    private long glfwWindow;
    private static Scene currentScene;


    private static Window window = null;

    private Window()
    {
        this.width = 1920;
        this.height = 1080;
        this.title = "Kaffee";
    }

    public static Window get()
    {
        if(Objects.isNull(Window.window))
        {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run()
    {
        System.out.println("Running Kaffee!");
        System.out.println("LWJGL - Version: " + Version.getVersion());
        initialize();

        loop();

        // Free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate and free error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    /**
     * Used to initialize GLFW window.
     */
    private void initialize()
    {
        // Error callback setup
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if(!glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW_TRUE);

        // Create Window
        this.glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL)
        {
            throw new IllegalStateException("Failed to initialize GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable V-Sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        Window.changeScene(0);
    }

    private void loop()
    {
        float beginTime = Time.getTime();
        float dt = -1.0f;
        float endTime;

        while(!glfwWindowShouldClose(glfwWindow))
        {
            // Poll events
            glfwPollEvents();

            glClearColor(get().r, get().g, get().b, get().alpha);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0)
            {
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();

            dt = endTime - beginTime;
            System.out.println(1.0f / dt + "FPS");

            beginTime = endTime;
        }
    }

    public static void changeScene(int newScene)
    {
        switch (newScene)
        {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.initialize();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.initialize();
                break;
            default:
                assert false: "Unknown scene: '" + newScene + "'.";
                break;
        }
    }
}
