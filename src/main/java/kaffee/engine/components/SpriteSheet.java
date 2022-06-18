package kaffee.engine.components;

import kaffee.engine.renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet
{
    private Texture texture;
    private List<Sprite> sprites;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int nSprites, int spacing)
    {
        this.sprites = new ArrayList<>();
        this.texture = texture;

        // Start on left
        int currentX = 0;

        // Start at bottom of the top sprite
        int currentY = texture.getHeight() - spriteHeight;

        for(int i = 0; i < nSprites; i++)
        {
            float topY = (currentY + spriteHeight) / (float) texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float leftX = currentX / (float) texture.getWidth();
            float bottomY = currentY / (float) texture.getHeight();

            Vector2f[] textureCoordinates = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite(this.texture, textureCoordinates);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if(currentX >= texture.getWidth())
            {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index)
    {
        return this.sprites.get(index);
    }
}
