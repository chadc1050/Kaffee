package kaffee.engine.renderer;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet
{
    private Texture texture;
    private List<Sprite> sprites;

    /**
     * Constructor for Sprite Sheet, only allows for uniformly sized sprites.
     * @param texture
     *      The texture that comprises the sheet.
     * @param spriteWidth
     *      Width of each sprite.
     * @param spriteHeight
     *      Height of each sprite.
     * @param nSprites
     *      Number of sprites contained in the sheet
     * @param spacing
     *      Uniform spacing between the sprites.
     */
    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int nSprites, int spacing)
    {
        this.sprites = new ArrayList<>();
        this.texture = texture;

        // Start on left
        int currentX = 0;

        // Start at bottom of the top sprite
        int currentY = texture.getHeight() - spriteHeight;

        for(int s = 0; s < nSprites; s++)
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

            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTextureCoordinates(textureCoordinates);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
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

    public int size()
    {
        return sprites.size();
    }
}
