package kaffee.engine.renderer.sprites;

import kaffee.engine.renderer.Texture;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SpriteSheet
{
    private Texture texture;
    private List<Sprite> sprites;
    private String name;

    /**
     * Constructor for Sprite Sheet, only allows for uniformly sized sprites. Parses from left to right.
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
    public SpriteSheet(String name, Texture texture, int spriteWidth, int spriteHeight, int nSprites, int spacing)
    {
        this.sprites = new ArrayList<>();
        this.texture = texture;
        this.name = name;

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

    /**
     * Used for sprite sheet with non-uniform in size sprites.
     * @param texture
     *      Texture for sprite sheet.
     * @param metadataFilePath
     *      File path to the metadata to determine sprite coordinates.
     */
    public SpriteSheet(Texture texture, String metadataFilePath)
    {
        this.texture = texture;
        SpriteSheetMetadataProcessor spriteSheetMetadataProcessor = new SpriteSheetMetadataProcessor(metadataFilePath, texture);
        spriteSheetMetadataProcessor.parse();
        this.sprites = spriteSheetMetadataProcessor.getSprites();
        this.name = spriteSheetMetadataProcessor.getName();
    }
}
