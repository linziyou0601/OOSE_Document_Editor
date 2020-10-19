package visitor;

import formatting.Formatting;
import model.Character;
import model.*;
import utils.parseArgs;

import java.util.Stack;

public class DrawGlyphVisitor implements Visitor{
    Stack<parseArgs> parseStack = new Stack<>();
    Formatting formatting;
    public DrawGlyphVisitor(Formatting formatting){
        this.formatting = formatting;
    }
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
            parseStack.push(parseArgs);
        }
    }

    public String getParseString(){
        return parseStack.peek().getFullTag();
    }
}
