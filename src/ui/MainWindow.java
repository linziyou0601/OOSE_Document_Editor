package ui;

import controller.EditorViewerHandler;
import controller.MainWindowHandler;
import formatting.FullFormatting;
import formatting.Formatting;
import main.Application;
import model.Glyph;
import utils.HTMLtoGlyphParser;
import visitor.CountCharacterVisitor;
import visitor.DrawGlyphVisitor;
import widgets.Menu;
import widgets.MenuBar;
import widgets.MenuItem;
import widgets.*;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

public class MainWindow extends Window {
    // Frame
    private final JFrame frame;
    private final int width = 800;
    private final int height = 600;
    private final DialogWindow dialogWindow;

    // Handler初始化
    private final MainWindowHandler mainWindowHandler = new MainWindowHandler(this);
    private final EditorViewerHandler editorViewerHandler = new EditorViewerHandler(this);
    private final WidgetFactory widgetFactory = WidgetFactoryProducer.getFactory(super.getEnvironment());

    // 編輯區初始化
    private final JTextPane editorViewer = new JTextPane();
    private final JPanel statusBar = new JPanel(new BorderLayout());
    private final JLabel statusLabel = new JLabel();

    // 排版物件及Visitor物件初始化
    private Formatting formatting = new FullFormatting();

    // Glyph初始化
    private Glyph root = null;

    // MenuBar物件宣告
    private Menu fileMenu;
    private MenuItem newFileMenu;
    private MenuItem openFileMenu;
    private MenuItem saveFileMenu;
    private Menu editMenu;
    private MenuItem cutEditMenu;
    private MenuItem copyEditMenu;
    private MenuItem pasteEditMenu;
    private MenuItem insertImgEditMenu;
    private Menu fontStyleMenu;
    private MenuItem boldFontStyleMenu;
    private MenuItem italicFontStyleMenu;
    private MenuItem underlineFontStyleMenu;
    private Menu colorMenu;
    private MenuItem redBackgroundColorMenu;
    private MenuItem greenBackgroundColorMenu;
    private MenuItem blueBackgroundColorMenu;
    private MenuItem blackBackgroundColorMenu;
    private MenuItem noneBackgroundColorMenu;
    private MenuItem redForegroundColorMenu;
    private MenuItem greenForegroundColorMenu;
    private MenuItem blueForegroundColorMenu;
    private MenuItem whiteForegroundColorMenu;
    private MenuItem blackForegroundColorMenu;
    private Menu formatMenu;
    private MenuItem fullFormatMenu;
    private MenuItem plaintextFormatMenu;
    private Menu helpMenu;
    private MenuItem aboutHelpMenu;

    //================================================== GUI ==================================================
    public MainWindow(WindowImp impl) {
        super(impl);
        //-------------------- JFrame --------------------
        // 建立JFrame
        frame = super.drawFrame();
        frame.setTitle("OOSE Document Editor");
        frame.setBounds(100, 100, width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 建立MenuBar
        frame.setJMenuBar(createMenuBar());

        // 設定Layout
        frame.setLayout(new BorderLayout());
        frame.add(createScrollPane(editorViewer), BorderLayout.CENTER);
        frame.add(statusBar, BorderLayout.SOUTH);

        // 設定 Dialog Window
        dialogWindow = new DialogWindow(impl);

        //-------------------- 狀態欄 --------------------
        statusLabel.setHorizontalAlignment(JLabel.RIGHT);
        statusLabel.setFont(super.getSystemFont());
        statusBar.setPreferredSize(new Dimension(statusBar.getWidth(), 30));
        statusBar.add(statusLabel);
        //-------------------- 編輯器區 --------------------
        editorViewer.setContentType("text/html");
        editorViewer.setText("<html><head></head><body><p></p></body></html>");
        editorViewer.getDocument().addDocumentListener(editorViewerHandler);
        editorViewer.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) { insertBlankText(); }
        });

    }

    // 執行
    public void run(){
        //打包frame並顯示
        frame.pack();
        frame.setSize(width, height);
        frame.setVisible(true);
        loadFromEditorViewer();
    }


    //================================================== 繪製 ==================================================
    // 繪製 Glyph 內容到 editorViewer 上
    public void drawIntoEditorViewer(){
        if(editorViewerHandler.getActive()) {
            Runnable doAssist = () -> {
                // 暫停監聽
                editorViewerHandler.setActive(false);
                // 儲存游標狀態
                int caretPosition = editorViewer.getCaretPosition();
                // 執行Visit
                DrawGlyphVisitor drawGlyphVisitor = new DrawGlyphVisitor(formatting);
                CountCharacterVisitor countCharacterVisitor = new CountCharacterVisitor();
                root.accept(drawGlyphVisitor);
                root.accept(countCharacterVisitor);
                // 畫到 editorViewer上
                editorViewer.setText("<html><head></head>" + drawGlyphVisitor.getParseString() + "</html>");
                editorViewer.setCaretPosition(Math.min(caretPosition, editorViewer.getDocument().getLength()-1));
                // 顯示目前狀態
                statusLabel.setText( countCharacterVisitor.getParagraph() + " 個段落　" + countCharacterVisitor.getCharacter() + " 個字元　|　排版模式：" + formatting.getTYPE() + "　　");
                // 重新監聽
                editorViewerHandler.setActive(true);
            };
            SwingUtilities.invokeLater(doAssist);
        }
    }

    // 讀取使用者輸入的字
    public void loadFromEditorViewer(){ setEditorContent(new HTMLtoGlyphParser().parse(editorViewer.getText())); }


    //================================================== GETTER SETTER ==================================================
    // 設定內容到 editorViewer
    public void setEditorContent(Glyph root){
        this.root = root;
        drawIntoEditorViewer();
    }
    // 設定排版
    public void setFormatting(Formatting formatting){
        // 設定排版模式
        this.formatting = formatting;
        drawIntoEditorViewer();
        // 選單按鈕調整
        boolean enabled = this.formatting.getTYPE().equals("Full");
        insertImgEditMenu.setEnabled(enabled);
        fontStyleMenu.setEnabled(enabled);
        colorMenu.setEnabled(enabled);
    }
    // 取得排版
    public Formatting getFormatting(){ return this.formatting; }
    // 取得Glyph
    public Glyph getRoot() { return this.root; }
    // 啟動Dialog Window
    public void showDialog(String message, String title) {
        dialogWindow.showDialog(message, title);
    }


    //================================================== INSERT ACTION ==================================================
    // 插入圖片
    public void insertEditorImage(AttributeSet outerAttr){
        try { editorViewer.getDocument().insertString(editorViewer.getCaretPosition()," ", outerAttr); }
        catch (BadLocationException e) { e.printStackTrace(); }
    }
    // 插入空白字元
    public void insertBlankText(){
        try {
            editorViewer.getDocument().insertString(editorViewer.getCaretPosition(),"&nbsp;", null);
            editorViewer.setCaretPosition(Math.max(0, editorViewer.getCaretPosition()-6));
        }
        catch (BadLocationException e) { e.printStackTrace(); }
    }


    // ==================================================建立 ScrollPane==================================================
    public JScrollPane createScrollPane(Component component){
        JScrollPane scrollPane = new JScrollPane();
        // 建立並設定 ScrollBar
        scrollPane.setVerticalScrollBar(widgetFactory.createScrollbar().setOriented(JScrollBar.VERTICAL));
        scrollPane.setHorizontalScrollBar(widgetFactory.createScrollbar().setOriented(JScrollBar.HORIZONTAL));
        // 內容放入 ScrollPane
        scrollPane.setViewportView(component);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    // ==================================================建立選單列==================================================
    public MenuBar createMenuBar() {
        MenuBar bar = widgetFactory.createMenuBar();

        // --------------------------------------------------建立選單選項 [檔案]--------------------------------------------------
        fileMenu = widgetFactory.createMenu();
        fileMenu.setDescription("檔案");
        // 建立 [檔案] 選單子選項
        // --------------------
        newFileMenu =  widgetFactory.createMenuItem();
        newFileMenu.setDescription("新增檔案");
        newFileMenu.setActionCommand("new");
        // --------------------
        openFileMenu = widgetFactory.createMenuItem();
        openFileMenu.setDescription("開啟舊檔");
        openFileMenu.setActionCommand("open");
        // --------------------
        saveFileMenu = widgetFactory.createMenuItem();
        saveFileMenu.setDescription("儲存檔案");
        saveFileMenu.setActionCommand("save");

        // 監聽指定選項
        newFileMenu.addActionListener(mainWindowHandler);
        openFileMenu.addActionListener(mainWindowHandler);
        saveFileMenu.addActionListener(mainWindowHandler);

        // 按鈕加入上級選單中
        fileMenu.addMenuItem(newFileMenu);
        fileMenu.addMenuItem(openFileMenu);
        fileMenu.addMenuItem(saveFileMenu);

        // --------------------------------------------------建立選單選項 [編輯]--------------------------------------------------
        editMenu = widgetFactory.createMenu();
        editMenu.setDescription("編輯");
        // 建立 [編輯] 選單子選項
        // --------------------
        cutEditMenu = widgetFactory.createMenuItem();
        cutEditMenu.setDescription("剪下 (ctrl+x)");
        cutEditMenu.setActionCommand("cut");
        // --------------------
        copyEditMenu = widgetFactory.createMenuItem();
        copyEditMenu.setDescription("複製 (ctrl+c)");
        copyEditMenu.setActionCommand("copy");
        // --------------------
        pasteEditMenu = widgetFactory.createMenuItem();
        pasteEditMenu.setDescription("貼上 (ctrl+v)");
        pasteEditMenu.setActionCommand("paste");
        // --------------------
        insertImgEditMenu = widgetFactory.createMenuItem();
        insertImgEditMenu.setDescription("插入圖片");
        insertImgEditMenu.setActionCommand("insertImg");

        // 監聽指定選項
        cutEditMenu.addActionListener(mainWindowHandler);
        copyEditMenu.addActionListener(mainWindowHandler);
        pasteEditMenu.addActionListener(mainWindowHandler);
        insertImgEditMenu.addActionListener(mainWindowHandler);

        // 按鈕加入上級選單中
        editMenu.addMenuItem(cutEditMenu);
        editMenu.addMenuItem(copyEditMenu);
        editMenu.addMenuItem(pasteEditMenu);
        editMenu.addMenuItem(insertImgEditMenu);

        // --------------------------------------------------建立選單選項 [樣式]--------------------------------------------------
        fontStyleMenu = widgetFactory.createMenu();
        fontStyleMenu.setDescription("樣式");
        // 建立 [樣式] 選單子選項
        // --------------------
        boldFontStyleMenu = widgetFactory.createMenuItem();
        boldFontStyleMenu.setDescription("粗體");
        boldFontStyleMenu.setActionCommand("Bold");
        boldFontStyleMenu.setFont(new Font(boldFontStyleMenu.getFont().getFontName(), Font.BOLD, 12));
        // --------------------
        italicFontStyleMenu = widgetFactory.createMenuItem();
        italicFontStyleMenu.setDescription("斜體");
        italicFontStyleMenu.setActionCommand("Italic");
        italicFontStyleMenu.setFont(new Font(italicFontStyleMenu.getFont().getFontName(), Font.ITALIC, 12));
        // --------------------
        underlineFontStyleMenu = widgetFactory.createMenuItem();
        underlineFontStyleMenu.setDescription("底線");
        underlineFontStyleMenu.setActionCommand("Underline");
        Map attributes = underlineFontStyleMenu.getFont().getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        underlineFontStyleMenu.setFont(underlineFontStyleMenu.getFont().deriveFont(attributes));

        // 監聽指定選項
        boldFontStyleMenu.addActionListener(mainWindowHandler);
        italicFontStyleMenu.addActionListener(mainWindowHandler);
        underlineFontStyleMenu.addActionListener(mainWindowHandler);

        // 按鈕加入上級選單中
        fontStyleMenu.addMenuItem(boldFontStyleMenu);
        fontStyleMenu.addMenuItem(italicFontStyleMenu);
        fontStyleMenu.addMenuItem(underlineFontStyleMenu);

        // --------------------------------------------------建立選單選項 [色彩]--------------------------------------------------
        colorMenu = widgetFactory.createMenu();
        colorMenu.setDescription("色彩");
        // 建立 [色彩] 選單子選項
        // --------------------
        redBackgroundColorMenu = widgetFactory.createMenuItem();
        redBackgroundColorMenu.setDescription("紅底色");
        redBackgroundColorMenu.setActionCommand("BG-Red");
        redBackgroundColorMenu.setForeground(Color.WHITE);
        redBackgroundColorMenu.setBackground(Color.RED);
        // --------------------
        greenBackgroundColorMenu = widgetFactory.createMenuItem();
        greenBackgroundColorMenu.setDescription("綠底色");
        greenBackgroundColorMenu.setActionCommand("BG-Green");
        greenBackgroundColorMenu.setForeground(Color.WHITE);
        greenBackgroundColorMenu.setBackground(Color.GREEN);
        // --------------------
        blueBackgroundColorMenu = widgetFactory.createMenuItem();
        blueBackgroundColorMenu.setDescription("藍底色");
        blueBackgroundColorMenu.setActionCommand("BG-Blue");
        blueBackgroundColorMenu.setForeground(Color.WHITE);
        blueBackgroundColorMenu.setBackground(Color.BLUE);
        // --------------------
        blackBackgroundColorMenu = widgetFactory.createMenuItem();
        blackBackgroundColorMenu.setDescription("黑底色");
        blackBackgroundColorMenu.setActionCommand("BG-Black");
        blackBackgroundColorMenu.setForeground(Color.WHITE);
        blackBackgroundColorMenu.setBackground(Color.BLACK);
        // --------------------
        noneBackgroundColorMenu = widgetFactory.createMenuItem();
        noneBackgroundColorMenu.setDescription("無底色");
        noneBackgroundColorMenu.setActionCommand("BG-None");
        // --------------------
        redForegroundColorMenu = widgetFactory.createMenuItem();
        redForegroundColorMenu.setDescription("紅色");
        redForegroundColorMenu.setActionCommand("FG-Red");
        redForegroundColorMenu.setForeground(Color.RED);
        // --------------------
        greenForegroundColorMenu = widgetFactory.createMenuItem();
        greenForegroundColorMenu.setDescription("綠色");
        greenForegroundColorMenu.setActionCommand("FG-Green");
        greenForegroundColorMenu.setForeground(Color.GREEN);
        // --------------------
        blueForegroundColorMenu = widgetFactory.createMenuItem();
        blueForegroundColorMenu.setDescription("藍色");
        blueForegroundColorMenu.setActionCommand("FG-Blue");
        blueForegroundColorMenu.setForeground(Color.BLUE);
        // --------------------
        whiteForegroundColorMenu = widgetFactory.createMenuItem();
        whiteForegroundColorMenu.setDescription("白色");
        whiteForegroundColorMenu.setActionCommand("FG-White");
        // --------------------
        blackForegroundColorMenu = widgetFactory.createMenuItem();
        blackForegroundColorMenu.setDescription("黑色");
        blackForegroundColorMenu.setActionCommand("FG-Black");

        // 監聽指定選項
        redBackgroundColorMenu.addActionListener(mainWindowHandler);
        greenBackgroundColorMenu.addActionListener(mainWindowHandler);
        blueBackgroundColorMenu.addActionListener(mainWindowHandler);
        blackBackgroundColorMenu.addActionListener(mainWindowHandler);
        noneBackgroundColorMenu.addActionListener(mainWindowHandler);
        redForegroundColorMenu.addActionListener(mainWindowHandler);
        greenForegroundColorMenu.addActionListener(mainWindowHandler);
        blueForegroundColorMenu.addActionListener(mainWindowHandler);
        whiteForegroundColorMenu.addActionListener(mainWindowHandler);
        blackForegroundColorMenu.addActionListener(mainWindowHandler);

        // 按鈕加入上級選單中
        colorMenu.addMenuItem(redBackgroundColorMenu);
        colorMenu.addMenuItem(greenBackgroundColorMenu);
        colorMenu.addMenuItem(blueBackgroundColorMenu);
        colorMenu.addMenuItem(blackBackgroundColorMenu);
        colorMenu.addMenuItem(noneBackgroundColorMenu);
        colorMenu.addMenuItem(redForegroundColorMenu);
        colorMenu.addMenuItem(greenForegroundColorMenu);
        colorMenu.addMenuItem(blueForegroundColorMenu);
        colorMenu.addMenuItem(whiteForegroundColorMenu);
        colorMenu.addMenuItem(blackForegroundColorMenu);

        // --------------------------------------------------建立選單選項 [排版]--------------------------------------------------
        formatMenu = widgetFactory.createMenu();
        formatMenu.setDescription("排版");
        // 建立 [排版] 選單子選項
        // --------------------
        fullFormatMenu = widgetFactory.createMenuItem();
        fullFormatMenu.setDescription("完整");
        fullFormatMenu.setActionCommand("full");
        // --------------------
        plaintextFormatMenu = widgetFactory.createMenuItem();
        plaintextFormatMenu.setDescription("純文字");
        plaintextFormatMenu.setActionCommand("plaintext");

        // 監聽指定選項
        fullFormatMenu.addActionListener(mainWindowHandler);
        plaintextFormatMenu.addActionListener(mainWindowHandler);

        // 按鈕加入上級選單中
        formatMenu.addMenuItem(fullFormatMenu);
        formatMenu.addMenuItem(plaintextFormatMenu);

        // --------------------------------------------------建立選單選項 [幫助]--------------------------------------------------
        helpMenu = widgetFactory.createMenu();
        helpMenu.setDescription("幫助");
        // 建立 [幫助] 選單子選項
        // --------------------
        aboutHelpMenu = widgetFactory.createMenuItem();
        aboutHelpMenu.setDescription("關於");
        aboutHelpMenu.setActionCommand("about");

        // 監聽指定選項
        aboutHelpMenu.addActionListener(mainWindowHandler);

        // 按鈕加入上級選單中
        helpMenu.addMenuItem(aboutHelpMenu);

        // --------------------------------------------------按鈕加入上級選單中--------------------------------------------------
        bar.addMenu(fileMenu);
        bar.addMenu(editMenu);
        bar.addMenu(fontStyleMenu);
        bar.addMenu(colorMenu);
        bar.addMenu(formatMenu);
        bar.addMenu(helpMenu);
        return bar;
    }
}
