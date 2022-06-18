package kaffee.engine.renderer;

import kaffee.engine.Window;
import kaffee.engine.components.SpriteRenderer;
import kaffee.engine.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class BatchRenderer
{
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEXTURE_COORDINATES_SIZE = 2;
    private final int TEXTURE_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEXTURE_COORDINATES_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEXTURE_ID_OFFSET = TEXTURE_COORDINATES_OFFSET + TEXTURE_ID_SIZE * Float.BYTES;

    private List<Texture> textures;
    private SpriteRenderer[] sprites;
    private int nSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] textureSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public BatchRenderer(int maxBatchSize)
    {
        shader = AssetPool.getShader("assets/shaders/default.glsl");
        shader.compile();
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.textures = new ArrayList<>();
        this.maxBatchSize = maxBatchSize;

        // Four vertex quads
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.nSprites = 0;
        this.hasRoom = true;
    }

    public void start()
    {
        // Generate and bind a vertex array object.
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEXTURE_COORDINATES_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_COORDINATES_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEXTURE_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addSprite(SpriteRenderer spriteRenderer)
    {

        // Get index and add render Object
        int index = this.nSprites;
        this.sprites[index] = spriteRenderer;
        this.nSprites++;

        // Add texture to global list of textures if not present
        if(spriteRenderer.getTexture() != null && !textures.contains(spriteRenderer.getTexture()))
        {
            textures.add(spriteRenderer.getTexture());
        }

        // Add properties to local vertices array
        loadVertexProperties(index);

        if (nSprites >= this.maxBatchSize)
        {
            this.hasRoom = false;
        }
    }

    private void loadVertexProperties(int index)
    {
        SpriteRenderer spriteRenderer = this.sprites[index];

        // Find the offest within array (4 vertices per array)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = spriteRenderer.getColor();
        Vector2f[] textureCoordinates = spriteRenderer.getTextureCoordinates();

        int textureID = 0;
        if(spriteRenderer.getTexture() != null)
        {
            for(int i = 0; i < textures.size(); i++)
            {
                if(textures.get(i).equals(spriteRenderer.getTexture()))
                {
                    textureID = i + 1;
                    break;
                }
            }
        }

        // Add vertices with the appropriate properties
        float xAdd = 0.5f;
        float yAdd = 0.5f;
        for(int i = 0; i < 4; i++)
        {
            if(i == 1)
            {
                yAdd = -0.5f;
            }
            else if(i == 2)
            {
                xAdd = -0.5f;
            }
            else if(i == 3)
            {
                yAdd = 0.5f;
            }

            // Load position
            vertices[offset] = spriteRenderer.gameObject.transform.position.x + (xAdd * spriteRenderer.gameObject.transform.scale.x);
            vertices[offset + 1] = spriteRenderer.gameObject.transform.position.y + (yAdd * spriteRenderer.gameObject.transform.scale.y);

            // Load Color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // Load Texture Coordinates
            vertices[offset + 6] = textureCoordinates[i].x;
            vertices[offset + 7] = textureCoordinates[i].y;

            // Load Texture ID
            vertices[offset + 8] = textureID;

            offset += VERTEX_SIZE;
        }
    }

    public void render()
    {
        // For now, we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());

        for(int i = 0; i < textures.size(); i++)
        {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }

        shader.uploadIntArray("uTextures", textureSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.nSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for(int i = 0; i < textures.size(); i++)
        {
            textures.get(i).unbind();
        }

        shader.detach();
    }

    private int[] generateIndices()
    {
        // Six indices per quad, three per triangle
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++)
        {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index)
    {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom()
    {
        return this.hasRoom;
    }

    public boolean hasTextureRoom()
    {
        return this.textures.size() < 8;
    }

    public boolean hasTexture(Texture texture)
    {
        return this.textures.contains(texture);
    }
}
