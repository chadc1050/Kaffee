package kaffee.engine.renderer.sprites;

import lombok.Getter;

@Getter
public enum SpriteSheetMappings
{
    DOG("assets/sprites/kaffee/textures/Stardew Valley - Dog Blonde.png", ""),
    SPRING_OUTDOORS("assets/sprites/kaffee/textures/Stardew Valley - Outdoors Spring.png", "assets/sprites/kaffee/metadata/Spring_Outdoors.xml");

    private final String texturePath;
    private final String spriteSheetMetadataPath;

    SpriteSheetMappings(String texturePath, String spriteSheetMetadataPath)
    {
        this.texturePath = texturePath;
        this.spriteSheetMetadataPath = spriteSheetMetadataPath;
    }
}
