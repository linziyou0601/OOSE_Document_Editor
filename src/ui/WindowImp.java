package ui;

import widgets.WidgetFactory;

import javax.swing.*;
import java.awt.*;

public interface WindowImp {
    String getEnvironment();
    JFrame drawFrame();
    void setSystemFont(Font systemFont);
    Font getSystemFont();
    WidgetFactory getWidgetFactory();
}
