package main;

import ui.LinuxWindowImp;
import ui.MainWindow;
import ui.WinWindowImp;
import ui.WindowImp;

public class Application
{
    public static final WindowImp impl = new WinWindowImp();
    public static void main(String[] args)
    {
        MainWindow mainWindow = new MainWindow(impl);
        mainWindow.run();
    }
}
