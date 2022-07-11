package kaffee.engine.renderer;

import kaffee.engine.Window;
import kaffee.engine.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw
{
    private static final int MAX_LINES = 500;

    private static List<Line> lines = new ArrayList<>();

    /**
     * 2 vertices per line that require 6 floats each
     */
    private static float[] vertexArray = new float[MAX_LINES * 2 * 6];

    private static Shader shader = AssetPool.getShader("assets/shaders/line.glsl");

    private static int vaoId;
    private static int vboId;

    private static boolean started = false;

    public static void start()
    {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 4, GL_FLOAT, false, 7 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 4, GL_FLOAT, false, 7 * Float.BYTES, 4 * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    public static void beginFrame()
    {
        if(!started)
        {
            start();
            started = true;
        }

        for(int i = 0; i < lines.size(); i++)
        {
            if(lines.get(i).beginFrame() < 0)
            {
                lines.remove(i);
                i--;
            }
        }
    }

    public static void draw()
    {
        if(!lines.isEmpty())
        {
            int index = 0;
            for (Line line : lines)
            {
                for(int i = 0; i < 2; i++)
                {
                    Vector2f position = i == 0 ? line.getBeginPoint() : line.getEndPoint();
                    Vector4f color = line.getColor();

                    // Position
                    vertexArray[index] = position.x;
                    vertexArray[index + 1] = position.y;

                    // Depth
                    vertexArray[index + 2] = -10.0f;

                    // Color
                    vertexArray[index + 3] = color.x;
                    vertexArray[index + 4] = color.y;
                    vertexArray[index + 5] = color.z;
                    vertexArray[index + 6] = color.w;

                    index += 7;
                }
            }

            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 7 * 2));

            // Use shader
            shader.use();
            shader.uploadMat4f("uProjection", Window.getCurrentScene().getCamera().getProjectionMatrix());
            shader.uploadMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());

            // Bind Vao
            glBindVertexArray(vaoId);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            // Draw
            glDrawArrays(GL_LINES, 0, lines.size());

            // Disable location
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);

            shader.detach();
        }
    }

    public static void addLine(Vector2f beginPoint, Vector2f endPoint)
    {
        addLine(beginPoint, endPoint, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 1);
    }

    public static void addLine(Vector2f beginPoint, Vector2f endPoint, Vector4f color, int framesActive)
    {
        if(!(lines.size() >= MAX_LINES))
        {
            DebugDraw.lines.add(new Line(beginPoint, endPoint, color, framesActive));

        }
    }
}
