package kaffee.engine.components;

import kaffee.engine.GameObject;
import kaffee.engine.Window;
import kaffee.engine.listener.MouseListener;

import java.util.Optional;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class VirtualComponent extends Component
{
    private Optional<GameObject> virtualGameObject = Optional.empty();

    public void referenceGameObject(GameObject gameObject)
    {
        this.virtualGameObject = Optional.ofNullable(gameObject);
        Window.getCurrentScene().addGameObjectsToCurrentScene(gameObject);
    }

    public void dereferenceGameObject()
    {
        this.virtualGameObject = Optional.empty();
    }

    @Override
    public void update(float deltaTime)
    {
        if(this.virtualGameObject.isPresent())
        {
            this.virtualGameObject.get().transform.position.x = MouseListener.getOrthogonalX() - 16;
            this.virtualGameObject.get().transform.position.y = MouseListener.getOrthogonalY() - 16;

            if(MouseListener.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT))
            {
                dereferenceGameObject();
            }
        }
    }
}
