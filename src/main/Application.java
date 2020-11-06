package main;

import ui.LinuxWindowImp;
import ui.MainWindow;
import ui.WinWindowImp;
import ui.WindowImp;

public class Application
{
    //設定Implementor
    public static final WindowImp impl = new WinWindowImp();
    public static void main(String[] args)
    {
        //建立mainWindow並以Implementor當成參數
        MainWindow mainWindow = new MainWindow(impl);
        //執行mainWindow的run()
        mainWindow.run();
    }
}
