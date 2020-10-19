package ui;

public abstract class Window{
    WindowImp impl;
    public Window(WindowImp impl){
        this.impl = impl;
    }
}
