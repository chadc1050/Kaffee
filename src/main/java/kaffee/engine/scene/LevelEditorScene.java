package kaffee.engine.scene;

import imgui.ImGui;
import imgui.ImVec2;
import kaffee.engine.Camera;
import kaffee.engine.GameObject;
import kaffee.engine.renderer.Sprite;
import kaffee.engine.renderer.SpriteSheet;
import kaffee.engine.util.AssetPool;
import org.joml.Vector2f;

public class LevelEditorScene extends Scene
{
    private GameObject levelEditor;

    private SpriteSheet sprites;

    public LevelEditorScene()
    {
    }

    @Override
    public void initialize()
    {
        loadResources();

        if(loadedLevel)
        {
            return;
        }

        sprites = AssetPool.getSpriteSheet("assets/sprites/kaffee/Stardew Valley - Dog Blonde.png");

        this.camera = new Camera(new Vector2f(0, 0));
    }

    private void loadResources()
    {
        AssetPool.getAllShaders("assets/shaders");
        AssetPool.addSpriteSheet("assets/sprites/kaffee/Stardew Valley - Dog Blonde.png",
                new SpriteSheet(AssetPool.getTexture("assets/sprites/kaffee/Stardew Valley - Dog Blonde.png"),32, 32, 36, 0));
    }

    @Override
    public void update(float deltaTime)
    {
        for(GameObject gameObject: this.gameObjects)
        {
            gameObject.update(deltaTime);
        }
        this.renderer.render();
    }

    @Override
    public void imGUI()
    {
        ImGui.begin("Level Editor");
        ImVec2 windowPos = ImGui.getWindowPos();
        ImVec2 windowSize = ImGui.getWindowSize();
        ImVec2 itemSpacing = ImGui.getStyle().getItemSpacing();

        float windowX2 = windowPos.x + windowSize.x;
        for(int i =0; i < sprites.size(); i++)
        {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTextureId();
            Vector2f[] textureCoordinates = sprite.getTextureCoordinates();

            if(ImGui.imageButton(id, spriteWidth, spriteHeight, textureCoordinates[0].x, textureCoordinates[0].y, textureCoordinates[2].x, textureCoordinates[2].y))
            {
                System.out.println("Button " + i + " clicked.");
            }

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

            if(i + 1 < sprites.size() && nextButtonX2 < windowX2)
            {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}
