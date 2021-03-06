package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import formatting.FullFormatting;
import formatting.PlaintextFormatting;
import model.Glyph;
import model.Paragraph;
import model.Root;
import ui.MainWindow;
import utils.BackgroundColorAction;
import utils.ForegroundColorAction;
import utils.InsertImageAction;
import utils.OOSEFILEtoGlyphParser;
import visitor.GlyphToOOSEFILEVisitor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MainWindowHandler implements ActionListener {
    MainWindow mainWindow;
    Glyph root;
    public MainWindowHandler(MainWindow mainWindow){ this.mainWindow = mainWindow; }
    // 當MenuItem按鈕按下去時
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        this.root = mainWindow.getRoot();
        switch (s) {
            //----------------------------------------編輯----------------------------------------
            case "cut":
                new DefaultEditorKit.CutAction().actionPerformed(e); //editorViewer.cut();
                break;
            case "copy":
                new DefaultEditorKit.CopyAction().actionPerformed(e);
                break;
            case "paste":
                new DefaultEditorKit.PasteAction().actionPerformed(e);
                break;
            case "insertImg": {
                // 建立檔案選擇器
                JFileChooser jFileChooser = new JFileChooser("f:");
                jFileChooser.setFileFilter(new FileNameExtensionFilter("jpg, jpeg, png, and gif", new String[] {"JPEG", "JPG", "PNG", "GIF"}));
                setFileChooserText("插入圖片", "插入");
                SwingUtilities.updateComponentTreeUI(jFileChooser);
                // 若使用者選擇檔案
                if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    // 將標籤設置為所選目錄的路徑
                    String absPath = jFileChooser.getSelectedFile().getAbsolutePath();
                    AttributeSet outerAttr = new InsertImageAction(absPath).actionPerformed(e);
                    mainWindow.insertEditorImage(outerAttr);
                } else {
                    mainWindow.showDialog("取消開啟舊檔操作", "提醒");
                }
                break;
            }

            //----------------------------------------樣式----------------------------------------
            case "Bold":
                new StyledEditorKit.BoldAction().actionPerformed(e);
                break;
            case "Italic":
                new StyledEditorKit.ItalicAction().actionPerformed(e);
                break;
            case "Underline":
                new StyledEditorKit.UnderlineAction().actionPerformed(e);
                break;

            //----------------------------------------色彩----------------------------------------
            case "BG-Red":
                new BackgroundColorAction("BG-Red", Color.RED).actionPerformed(e);
                break;
            case "BG-Green":
                new BackgroundColorAction("BG-Green", Color.GREEN).actionPerformed(e);
                break;
            case "BG-Blue":
                new BackgroundColorAction("BG-Blue", Color.BLUE).actionPerformed(e);
                break;
            case "BG-Black":
                new BackgroundColorAction("BG-Black", Color.BLACK).actionPerformed(e);
                break;
            case "BG-None":
                new BackgroundColorAction("BG-None", null).actionPerformed(e);
                break;
            case "FG-Red":
                new ForegroundColorAction("FG-Red", Color.RED).actionPerformed(e);
                break;
            case "FG-Green":
                new ForegroundColorAction("FG-Green", Color.GREEN).actionPerformed(e);
                break;
            case "FG-Blue":
                new ForegroundColorAction("FG-Blue", Color.BLUE).actionPerformed(e);
                break;
            case "FG-White":
                new ForegroundColorAction("FG-White", Color.WHITE).actionPerformed(e);
                break;
            case "FG-Black":
                new ForegroundColorAction("FG-Black", Color.BLACK).actionPerformed(e);
                break;

            //----------------------------------------檔案----------------------------------------
            case "new":
                root = new Root();
                root.insert(new Paragraph());
                mainWindow.setEditorContent(root);
                break;
            case "open": {
                // 建立檔案選擇器
                JFileChooser jFileChooser = new JFileChooser("f:");
                jFileChooser.setFileFilter(new FileNameExtensionFilter("OOSE Document File", new String[] {"OOSEFILE"}));
                setFileChooserText("開啟舊檔", "開啟");
                SwingUtilities.updateComponentTreeUI(jFileChooser);
                // 若使用者選擇檔案
                if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    // 將絕對路徑設到已選的目錄
                    File file = new File(jFileChooser.getSelectedFile().getAbsolutePath().replaceAll("\\.oosefile", "") + ".oosefile");

                    try {
                        // 建立 file reader 及 buffered reader
                        InputStreamReader fileReader = new InputStreamReader (new FileInputStream(file),StandardCharsets.UTF_8);    //FileReader fileReader = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        // 一行一行讀入
                        String opening_oosefile = "";
                        String readline = "";
                        opening_oosefile = bufferedReader.readLine();
                        while ((readline = bufferedReader.readLine()) != null)
                            opening_oosefile = opening_oosefile + "\n" + readline;

                        // 設定到editorViewer
                        root = new OOSEFILEtoGlyphParser().parse(opening_oosefile);
                        mainWindow.setEditorContent(root);
                        // 設定排版
                        Map<String, Object> document = new Gson().fromJson(opening_oosefile, new TypeToken<Map<String, Object>>(){}.getType());
                        if(document.get("format").equals("Full")) mainWindow.setFormatting(new FullFormatting());
                        else  mainWindow.setFormatting(new PlaintextFormatting());
                    } catch (Exception evt) {
                        System.out.println(evt.getMessage());
                        mainWindow.showDialog(evt.getMessage(), "錯誤");
                    }

                }
                // If the user cancelled the operation
                else
                    mainWindow.showDialog("取消儲存檔案操作", "提醒");
                break;

            }
            case "save": {
                // 建立檔案選擇器
                JFileChooser jFileChooser = new JFileChooser("f:");
                jFileChooser.setFileFilter(new FileNameExtensionFilter("OOSE Document File", new String[] {"OOSEFILE"}));
                setFileChooserText("儲存檔案", "儲存");
                SwingUtilities.updateComponentTreeUI(jFileChooser);
                // 若使用者選擇檔案
                if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    // 將絕對路徑設到已選的目錄
                    File file = new File(jFileChooser.getSelectedFile().getAbsolutePath().replaceAll("\\.oosefile", "") + ".oosefile");

                    try {
                        // 建立 file writer 及 buffered writer
                        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);    //FileWriter fileWriter = new FileWriter(file, false);
                        BufferedWriter writer = new BufferedWriter(fileWriter);
                        // 寫入資料
                        GlyphToOOSEFILEVisitor glyphToOOSEFILEVisitor = new GlyphToOOSEFILEVisitor();
                        root.accept(glyphToOOSEFILEVisitor);
                        String saving_oosefile = "{\"format\": \"" + mainWindow.getFormatting().getTYPE() + "\"," +
                                                 "\"content\": " + glyphToOOSEFILEVisitor.getParseString() + "}";
                        writer.write(saving_oosefile);
                        writer.flush();
                        writer.close();
                    } catch (Exception evt) {
                        evt.printStackTrace();
                        mainWindow.showDialog(evt.getMessage(), "錯誤");
                    }
                }
                // If the user cancelled the operation
                else
                    mainWindow.showDialog("取消儲存檔案操作", "提醒");
                break;
            }

            //----------------------------------------排版----------------------------------------
            //按下的按鈕 為 "完整"
            case "full":
                mainWindow.setFormatting(new FullFormatting());
                break;
            //按下的按鈕 為 "純文字"
            case "plaintext":
                mainWindow.setFormatting(new PlaintextFormatting());
                break;

            //----------------------------------------幫助----------------------------------------
            case "about":
                mainWindow.showDialog("這是運行在" + mainWindow.getEnvironment() + "環境的文件編輯器", "關於");
                break;
        }
    }

    public void setFileChooserText(String openDialogTitleText, String openButton){
        UIManager.put("FileChooser.openDialogTitleText", openDialogTitleText);
        UIManager.put("FileChooser.lookInLabelText", "目前");
        UIManager.put("FileChooser.openButtonText", openButton);
        UIManager.put("FileChooser.cancelButtonText", "取消");
        UIManager.put("FileChooser.fileNameLabelText", "檔案名稱");
        UIManager.put("FileChooser.filesOfTypeLabelText", "檔案類型");
        UIManager.put("FileChooser.openButtonToolTipText", openButton+"指定的檔案");
        UIManager.put("FileChooser.cancelButtonToolTipText","取消");
    }
}
