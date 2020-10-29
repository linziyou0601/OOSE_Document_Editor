package ui;

import javax.swing.*;

public abstract class Window{
    WindowImp impl;
    public Window(WindowImp impl){
        this.impl = impl;
    }
    public String getEnvironment(){ return this.impl.getEnvironment(); }
    public JFrame drawFrame(){ return this.impl.drawFrame(); }
}
