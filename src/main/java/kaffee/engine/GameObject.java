package kaffee.engine;

import kaffee.engine.components.Component;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
        components.forEach(Component::start);
    }

    public void stop()
    {
        components.forEach(Component::stop);
    }

    public void update(float deltaTime)
    {
        components.forEach(component -> component.update(deltaTime));
    }

    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        return components.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .findFirst()
                .map(componentClass::cast)
                .orElse(null);
    }

    public void addComponent(Component component)
    {
        this.components.add(component);
        component.gameObject = this;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass)
    {
        for(Component component : components)
        {
            if(componentClass.isAssignableFrom(component.getClass()))
            {
                components.remove(component);
                break;
            }
        }
    }

    public void imGUI()
    {
        components.forEach(Component::imGUI);
    }
}
