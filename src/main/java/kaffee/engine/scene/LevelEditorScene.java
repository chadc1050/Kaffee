package kaffee.engine.scene;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiTabBarFlags;
import kaffee.engine.Camera;
import kaffee.engine.GameObject;
import kaffee.engine.Window;
import kaffee.engine.components.GameObjectGenerator;
import kaffee.engine.components.VirtualComponent;
import kaffee.engine.listeners.MouseListener;
import kaffee.engine.renderer.sprites.Sprite;
import kaffee.engine.renderer.sprites.SpriteSheet;
import kaffee.engine.renderer.sprites.SpriteSheetMappings;
import kaffee.engine.util.AssetPool;
import org.joml.Vector2f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LevelEditorScene extends Scene
{
    private VirtualComponent virtualComponent = new VirtualComponent();
    private List<SpriteSheet> sprites = new ArrayList<>();

    public LevelEditorScene()
    {

    }

    @Override
    public void initialize()
    {
        loadResources();
        this.camera = new Camera(new Vector2f(0, 0));

        if(loadedLevel)
        {
            return;
        }

        sprites.add(AssetPool.getSpriteSheet(SpriteSheetMappings.DOG.getTexturePath()));
        sprites.add(AssetPool.getSpriteSheet(SpriteSheetMappings.SPRING_OUTDOORS.getTexturePath()));
    }

    @Override
    public void update(float deltaTime)
    {
        virtualComponent.update(deltaTime);

        for(GameObject gameObject: this.gameObjects)
        {
            gameObject.update(deltaTime);
        }

        this.renderer.render();
    }

    @Override
    public void imGUI()
    {
        ImGui.begin("Runtime Dev Info");
        ImGui.text("FPS: " + Window.get().getFPS());
        ImGui.text("Mouse X Position: " + MouseListener.getXPos());
        ImGui.text("Mouse Y Position: " + MouseListener.getYPos());
        ImGui.text("Mouse World Coordinates X Position: " + MouseListener.getOrthogonalX());
        ImGui.text("Mouse World Coordinates Y Position: " + MouseListener.getOrthogonalY());
        ImGui.text("Game Objects in Scene: " + Window.getCurrentScene().gameObjects.size());
        ImGui.end();

        ImGui.begin("Level Editor");
        if(ImGui.treeNode("Game Object Editor"))
        {
            if(ImGui.beginTabBar("Tabs", ImGuiTabBarFlags.None))
            {
                if(ImGui.beginTabItem("Sprites"))
                {
                    for (SpriteSheet spriteSheet : sprites)
                    {
                        if (ImGui.treeNode(spriteSheet.getName()))
                        {
                            ImVec2 windowPos = ImGui.getWindowPos();
                            ImVec2 windowSize = ImGui.getWindowSize();
                            ImVec2 itemSpacing = ImGui.getStyle().getItemSpacing();

                            float windowX2 = windowPos.x + windowSize.x;
                            for (int i = 0; i < spriteSheet.getSprites().size(); i++)
                            {
                                Sprite sprite = spriteSheet.getSprites().get(i);
                                float spriteWidth = sprite.getWidth();
                                float spriteHeight = sprite.getHeight();
                                int id = sprite.getTextureId();
                                Vector2f[] textureCoordinates = sprite.getTextureCoordinates();

                                ImGui.pushID(i);
                                if (ImGui.imageButton(id, spriteWidth * 4, spriteHeight * 4, textureCoordinates[2].x, textureCoordinates[0].y, textureCoordinates[0].x, textureCoordinates[2].y))
                                {
                                    virtualComponent.referenceGameObject(GameObjectGenerator.generateSprite(sprite, spriteWidth, spriteHeight));
                                }
                                ImGui.popID();

                                ImVec2 lastButtonPos = new ImVec2();
                                ImGui.getItemRectMax(lastButtonPos);
                                float lastButtonX2 = lastButtonPos.x;
                                float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

                                if (i + 1 < spriteSheet.getSprites().size() && nextButtonX2 < windowX2)
                                {
                                    ImGui.sameLine();
                                }
                            }
                            ImGui.separator();
                            ImGui.treePop();
                        }
                    }
                    ImGui.endTabItem();
                }
                if(ImGui.beginTabItem("Options"))
                {
                    if(ImGui.button("Clear All Game Objects"))
                    {
                        this.gameObjects.forEach(GameObject::stop);
                        this.gameObjects.clear();
                    }
                    if(ImGui.button("Save Level"))
                    {
                        // TODO: Save level to a user specified location
                        this.save("assets/userDefinedLevel.txt");
                    }
                    if(ImGui.button("Load Level"))
                    {
                        //TODO: Remove all game objects and load level from file
                    }
                    ImGui.endTabItem();
                }
                ImGui.endTabBar();
            }
            ImGui.separator();
            ImGui.treePop();
        }
        if(ImGui.treeNode("Display Options"))
        {
            ImGui.separator();
            ImGui.treePop();
        }
        if(ImGui.treeNode("Debug Tools"))
        {
            // TODO: Add line, box creation.
            ImGui.separator();
            ImGui.treePop();
        }
        ImGui.end();
    }

    private void loadResources()
    {
        AssetPool.getAllShaders("assets/shaders");
        File dog = new File("assets/sprites/kaffee/textures/Stardew Valley - Dog Blonde.png");
        AssetPool.addSpriteSheet(
                dog.getPath(),
                new SpriteSheet(dog.getName().substring(0, dog.getName().lastIndexOf(".")), AssetPool.getTexture(dog.getPath()),32, 32, 36, 0));
        AssetPool.addSpriteSheet(SpriteSheetMappings.SPRING_OUTDOORS.getTexturePath(),
                new SpriteSheet(AssetPool.getTexture(SpriteSheetMappings.SPRING_OUTDOORS.getTexturePath()), SpriteSheetMappings.SPRING_OUTDOORS.getSpriteSheetMetadataPath()));
    }
}
