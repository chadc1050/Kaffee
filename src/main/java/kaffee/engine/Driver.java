package kaffee.engine;

public class Driver {

    // TODO: Make this overridable so that the entry point and window pointer may be changed.
    public static void main(String[] args)
    {
        Window window = Window.get();
        window.run();
    }
}
