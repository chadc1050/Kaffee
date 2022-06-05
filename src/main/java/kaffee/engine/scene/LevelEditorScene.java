package kaffee.engine.scene;

import static org.lwjgl.opengl.GL11C.GL_FALSE;
import static org.lwjgl.opengl.GL20C.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20C.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20C.glAttachShader;
import static org.lwjgl.opengl.GL20C.glCompileShader;
import static org.lwjgl.opengl.GL20C.glCreateProgram;
import static org.lwjgl.opengl.GL20C.glCreateShader;
import static org.lwjgl.opengl.GL20C.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20C.glGetProgrami;
import static org.lwjgl.opengl.GL20C.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20C.glGetShaderi;
import static org.lwjgl.opengl.GL20C.glLinkProgram;
import static org.lwjgl.opengl.GL20C.glShaderSource;

public class LevelEditorScene extends Scene
{
    private String vertexShaderSource = "#version 460 core\n" +
            "layout(location=0) in vec3 aPos;\n" +
            "layout(location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    glPosition = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSource = "#version 460 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;

    public LevelEditorScene()
    {

    }

    @Override
    public void initialize() {
        // Create and Compile shaders
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        // Pass the shader source code to GPU
        glShaderSource(vertexID, vertexShaderSource);
        glCompileShader(vertexID);

        // Check for compilation errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("SHADER COMPILATION ERROR:");
            System.out.println(glGetShaderInfoLog(vertexID, length));
            assert false: "";
        }

        // Create and Compile fragment
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        // Pass the shader source code to GPU
        glShaderSource(fragmentID, fragmentShaderSource);
        glCompileShader(fragmentID);

        // Check for compilation errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("FRAGMENT COMPILATION ERROR:");
            System.out.println(glGetShaderInfoLog(fragmentID, length));
            assert false: "";
        }

        // Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE)
        {
            int length = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("LINKING SHADERS COMPILATION ERROR:");
            System.out.println(glGetProgramInfoLog(shaderProgram, length));
            assert false: "";
        }
    }

    @Override
    public void update(float deltaTime)
    {

    }
}
