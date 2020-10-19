package utils;

import ui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class BlankTextInputAction extends AbstractAction {
    MainWindow mainWindow;
    public BlankTextInputAction(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        mainWindow.insertBlankText();
    }
}
