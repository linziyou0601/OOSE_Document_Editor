package visitor;

import formatting.Formatting;
import model.Character;
import model.*;
import utils.parseArgs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class DrawGlyphVisitor implements Visitor{
    private Stack<parseArgs> parseStack = new Stack<>();
    private Formatting formatting;
    public DrawGlyphVisitor(Formatting formatting){
        this.formatting = formatting;
    }
    @Override
    public void visit(Root root) {pushCompositeToStack(root);}
    @Override
    public void visit(Paragraph paragraph) {pushCompositeToStack(paragraph);}
    @Override
    public void visit(Span span) {pushCompositeToStack(span);}
    @Override
    public void visit(Image image) {pushLeafToStack(image);}
    @Override
    public void visit(Character character) {pushLeafToStack(character);}
    @Override
    public void visit(Bold bold) {pushCompositeToStack(bold);}
    @Override
    public void visit(Italic italic) {pushCompositeToStack(italic);}
    @Override
    public void visit(Underline underline) {pushCompositeToStack(underline);}
    @Override
    public void visit(Font font) {pushCompositeToStack(font);}
    @Override
    public void visit(Glyph glyph) {pushCompositeToStack(glyph);}

    public void pushCompositeToStack(Glyph g){
        parseArgs parseArgs = formatting.parse(g);
        //若目前Glyph有子元素，且子元素數目 >= 目前堆疊內項目數
        if(g.getChildSize()>0 && parseStack.size() >= g.getChildSize()){
            //先將子元素依序組合起來 （兄節點會在弟節點之前被push進去，故弟節點會先poped出來，所以要 pop() + childParseStrings）
            String childParseStrings = "";
            for(int i = 0; i < g.getChildSize(); i++){
                childParseStrings = parseStack.pop().getFullTag() + childParseStrings;
            }
            //目前Glyph把childParseStrings包起來
            childParseStrings = parseArgs.getStartingTag() + childParseStrings + parseArgs.getClosingTag();
            //把目前Glyph給push回去堆疊裡
            parseArgs pushBackParseArgs = new parseArgs();
            pushBackParseArgs.setFullTag(childParseStrings);
            parseStack.push(pushBackParseArgs);
        } else {
            pushLeafToStack(g);
        }
    }

    public void pushLeafToStack(Glyph g){
        parseArgs parseArgs = formatting.parse(g);
        parseStack.push(parseArgs);
    }

    public String getParseString(){
        return parseStack.peek().getFullTag();
    }
}
