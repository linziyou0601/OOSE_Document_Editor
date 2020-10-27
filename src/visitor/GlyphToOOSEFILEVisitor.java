package visitor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Character;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class GlyphToOOSEFILEVisitor implements Visitor{
    Stack<Map<String, Object>> parseStack = new Stack<>();
    @Override
    public void visit(Root root) {appendParseString(root);}
    @Override
    public void visit(Paragraph paragraph) {appendParseString(paragraph);}
    @Override
    public void visit(Span span) {appendParseString(span);}
    @Override
    public void visit(Image image) {appendParseString(image);}
    @Override
    public void visit(Character character) {appendParseString(character);}
    @Override
    public void visit(Bold bold) {appendParseString(bold);}
    @Override
    public void visit(Italic italic) {appendParseString(italic);}
    @Override
    public void visit(Underline underline) {appendParseString(underline);}
    @Override
    public void visit(Font font) {appendParseString(font);}
    @Override
    public void visit(Glyph glyph) {appendParseString(glyph);}

    public void appendParseString(Glyph g) {
        //若目前Glyph有子元素，且子元素數目 >= 目前堆疊內項目數
        if(g.getChildSize()>0 && parseStack.size() >= g.getChildSize()){
            //先將子元素依序組合起來 （兄節點會在弟節點之前被push進去，故弟節點會先poped出來，所以要 pop() + childParseStrings）
            ArrayList<Map<String, Object>> childList = new ArrayList<>();
            for(int i = 0; i < g.getChildSize(); i++){
                childList.add(0, parseStack.pop());
            }
            Map<String, Object> elem = new HashMap<>();
            elem.put("tag", g.getTagname());
            elem.put("attribute", g.getAttribute().replace("\\\"", "\""));
            elem.put("content", g.getContent());
            if(g instanceof Decorator){
                elem.put("decoratee", childList.get(0));
                elem.put("child", new ArrayList<>());
            } else {
                elem.put("decoratee", null);
                elem.put("child", childList);
            }
            //把目前Glyph給push回去堆疊裡
            parseStack.push(elem);
        } else {
            Map<String, Object> elem = new HashMap<>();
            elem.put("tag", g.getTagname());
            elem.put("attribute", g.getAttribute().replace("\\\"", "\""));
            elem.put("content", g.getContent());
            elem.put("decoratee", null);
            elem.put("child", new ArrayList<>());
            parseStack.push(elem);
        }
    }

    public String getParseString(){
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(parseStack.peek());
    }
}
