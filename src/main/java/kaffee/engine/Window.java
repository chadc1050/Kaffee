package kaffee.engine;

import kaffee.engine.listeners.KeyListener;
import kaffee.engine.listeners.MouseListener;
import kaffee.engine.scene.LevelEditorScene;
import kaffee.engine.scene.LevelScene;
import kaffee.engine.scene.Scene;
import kaffee.engine.util.Time;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    public float r, g, b = 1.0f;
    public float alpha = 1.0f;

    private int width, height;
    private float fps;
    private String title;
    private long glfwWindow;
    private static Scene currentScene;
    private IMGuiLayer imGuiLayer;


    private static Window window = null;

    private Window()
    {
        this.width = 1920;
        this.height = 1080;
        this.title = "Kaffee";
    }

    /**
     * Retrieves the window singleton instance.
     * @return
     *      Window instance.
     */
    public static Window get()
    {
        return Window.window = Objects.isNull(Window.window) ? new Window() : Window.window;
    }

    public static Scene getCurrentScene()
    {
        return currentScene;
    }

    public void run()
    {
        // Log version information
        try
        {
            System.out.println("\u001B[31m" + new String(Files.readAllBytes(Paths.get("assets/resources/KaffeeBootText"))) + "\u001B[0m");
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader("pom.xml"));
            System.out.println("Kaffee - Version: " + model.getVersion());
        }
        catch (IOException e)
        {
            throw new RuntimeException("ERROR: Missing Kaffee resource.");
        }
        catch (XmlPullParserException e)
        {
            throw new RuntimeException("ERROR: Missing dependencies in POM file.");
        }
        System.out.println("LWJGL - Version: " + Version.getVersion());

        // Initialize window
        initialize();

        // Loop window instance
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
        System.out.println("INFO: Initializing GLFW Window.");
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
        System.out.println("INFO: Creating window.");
        this.glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL)
        {
            throw new IllegalStateException("Failed to initialize GLFW window.");
        }

        // Set input callbacks for mouse and keyboard so user input can be accepted
        System.out.println("INFO: Setting input listener callbacks.");

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable V-Sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        // Config blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        // IMGUI
        this.imGuiLayer = new IMGuiLayer(glfwWindow);
        this.imGuiLayer.initImGui();

        // Initial scene
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

            // Set the clear color for the window
            glClearColor(r, g, b, alpha);
            glClear(GL_COLOR_BUFFER_BIT);

            // Update the scene.
            if(dt >= 0)
            {
                currentScene.update(dt);
            }

            this.imGuiLayer.update(dt, currentScene);

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();

            dt = endTime - beginTime;
            this.fps = 1.0f / dt;

            beginTime = endTime;
        }
        currentScene.saveExit();
    }

    public static void changeScene(int newScene)
    {
        switch (newScene)
        {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.initialize();
                currentScene.load();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.initialize();
                currentScene.load();
                currentScene.start();
                break;
            default:
                assert false: "Unknown scene: '" + newScene + "'.";
                break;
        }
    }

    public static int getWidth()
    {
        return get().width;
    }

    public static void setWidth(int width)
    {
        get().width = width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setHeight(int height)
    {
        get().height = height;
    }

    public String getTitle()
    {
        return get().title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public float getFPS()
    {
        return this.fps;
    }
}
