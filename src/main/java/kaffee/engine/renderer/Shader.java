package kaffee.engine.renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11C.GL_FALSE;
import static org.lwjgl.opengl.GL20C.*;

public class Shader
{
    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;

    private boolean beingUsed = false;

    private String filePath;

    public Shader(String filepath)
    {
        this.filePath = filepath;
        try
        {
            parseShaderFile(filePath);
        }
        catch (IOException e)
        {
            System.out.println("ERROR: Could not open shader file: " + filepath);
        }
    }

    public void compile()
    {
        // Create and Compile shaders
        int vertexID = glCreateShader(GL_VERTEX_SHADER);

        // Pass the shader source code to GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for compilation errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: An unexpected error occurred when compiling shader.");
            System.out.println("\t Shader File: " + filePath);
            System.out.println(glGetShaderInfoLog(vertexID, length));
            assert false: "";
        }

        // Create and Compile fragment
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        // Pass the shader source code to GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Check for compilation errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: An unexpected error occurred when compiling fragment.");
            System.out.println("\t Shader File: " + filePath);
            System.out.println(glGetShaderInfoLog(fragmentID, length));
            assert false: "";
        }

        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE)
        {
            int length = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: An unexpected error occurred when linking shaders:");
            System.out.println("\t Shader File: " + filePath);
            System.out.println(glGetProgramInfoLog(shaderProgramID, length));
            assert false: "";
        }
    }

    public void uploadMat4f(String varName, Matrix4f mat4)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        // Make sure the shader is being used.
        use();

        // Flatten 4x4 matrix to a 1x16 using Float Buffer
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);

        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        // Make sure the shader is being used.
        use();

        // Flatten 4x4 matrix to a 1x16 using Float Buffer
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);

        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadMat2f(String varName, Matrix2f mat2)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        // Make sure the shader is being used.
        use();

        // Flatten 4x4 matrix to a 1x16 using Float Buffer
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4);
        mat2.get(matBuffer);

        glUniformMatrix2fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        // Make sure the shader is being used.
        use();

        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        // Make sure the shader is being used.
        use();

        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        // Make sure the shader is being used.
        use();

        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadInt(String varName, int value)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        // Make sure the shader is being used.
        use();

        glUniform1i(varLocation, value);
    }

    public void uploadFloat(String varName, float value)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        // Make sure the shader is being used.
        use();

        glUniform1f(varLocation, value);
    }

    public void uploadTexture(String varName, int slot)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        // Make sure the shader is being used.
        use();

        glUniform1i(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] array)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);

        // Make sure the shader is being used.
        use();

        glUniform1iv(varLocation, array);
    }

    public void use()
    {
        if(!beingUsed)
        {
            // Bind shader program
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    public void detach()
    {
        glUseProgram(0);
        beingUsed = false;
    }

    private void parseShaderFile(String filePath) throws IOException
    {
        String source = new String(Files.readAllBytes(Paths.get(filePath)));
        String[] stringSplit = source.split("(#type)( )+([a-zA-Z)]+)");

        // Get the index of the type attribute
        int index = source.indexOf("#type") + 6;
        int eol = source.indexOf("\r\n", index);

        String firstPattern = source.substring(index, eol).trim();

        index = source.indexOf("#type", eol) + 6;
        eol = source.indexOf("\r\n", index);

        String secondPattern = source.substring(index, eol).trim();

        try
        {
            if (firstPattern.equals("vertex"))
            {
                vertexSource = stringSplit[1];
            }
            else if (firstPattern.equals("fragment"))
            {
                fragmentSource = stringSplit[1];
            }
            else
            {
                throw new IOException("ERROR: Unexpected token '" + firstPattern + "' in file path: " + filePath);
            }

            if (secondPattern.equals("vertex"))
            {
                vertexSource = stringSplit[2];
            }
            else if (secondPattern.equals("fragment"))
            {
                fragmentSource = stringSplit[2];
            }
            else
            {
                throw new IOException("ERROR: Unexpected token '" + secondPattern + "' in file path: " + filePath);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            assert false : e.getMessage();
        }
    }
}
