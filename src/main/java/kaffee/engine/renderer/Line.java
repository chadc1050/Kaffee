package kaffee.engine.renderer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector4f;

@Getter
@Setter
@AllArgsConstructor
public class Line
{
    private Vector2f beginPoint;
    private Vector2f endPoint;
    private Vector4f color;
    private int framesActive;

    public int beginFrame()
    {
        this.framesActive--;
        return this.framesActive;
    }
}
