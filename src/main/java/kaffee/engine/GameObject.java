package kaffee.engine;

import kaffee.engine.components.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    private String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;

    public GameObject(String name, int zIndex)
    {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.zIndex = zIndex;
    }

    public GameObject(String name, Transform transform, int zIndex)
    {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;
    }

    public void start()
    {
        for(int i = 0; i < components.size(); i++)
        {
            components.get(i).start();
        }
    }

    public void update(float deltaTime)
    {
        for(int i = 0; i < components.size(); i++)
        {
            components.get(i).update(deltaTime);
        }
    }

    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        for(Component component : components)
        {
            if(componentClass.isAssignableFrom(component.getClass()))
            {
                return componentClass.cast(component);
            }
        }
        return null;
    }

    public void addComponent(Component component)
    {
        this.components.add(component);
        component.gameObject = this;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass)
    {
        for(int i = 0; i < components.size(); i++)
        {
            Component component = components.get(i);
            if(componentClass.isAssignableFrom(component.getClass()))
            {
                components.remove(component);
                break;
            }
        }
    }

    public void imGUI()
    {
        for(Component component : components)
        {
            component.imGUI();
        }
    }

    public int getZIndex()
    {
        return zIndex;
    }
}
