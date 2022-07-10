package kaffee.engine.renderer;

import kaffee.engine.GameObject;
import kaffee.engine.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer
{
    private final int MAX_BATCH_SIZE = 1000;
    private List<BatchRenderer> batchRenderers;

    public Renderer()
    {
        this.batchRenderers = new ArrayList<>();
    }

    public void add(GameObject gameObject)
    {
        SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
        if(spriteRenderer != null)
        {
            add(spriteRenderer);
        }
    }

    private void add(SpriteRenderer spriteRenderer)
    {
        boolean added = false;
        for(BatchRenderer batchRenderer: batchRenderers)
        {
            if(batchRenderer.hasRoom())
            {
                Texture texture = spriteRenderer.getTexture();
                if(texture == null || (batchRenderer.hasTexture(texture) || batchRenderer.hasTextureRoom()))
                {
                    batchRenderer.addSprite(spriteRenderer);
                    added = true;
                    break;
                }
            }
        }

        if(!added)
        {
            BatchRenderer newBatchRenderer = new BatchRenderer(MAX_BATCH_SIZE, spriteRenderer.gameObject.getZIndex());
            newBatchRenderer.start();
            batchRenderers.add(newBatchRenderer);
            newBatchRenderer.addSprite(spriteRenderer);
            Collections.sort(batchRenderers);
        }
    }

    public void render()
    {
        batchRenderers.forEach(BatchRenderer::render);
    }

}
