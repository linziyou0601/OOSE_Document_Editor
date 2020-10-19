package visitor;

import formatting.Formatting;
import model.Character;
import model.*;
import utils.parseArgs;

import java.util.Stack;

public class CountCharacterVisitor implements Visitor{
    int character = 0;
    int paragraph = 0;
    @Override
    public void visit(Root root) {count(root);}
    @Override
    public void visit(Paragraph paragraph) {count(paragraph);}
    @Override
    public void visit(Span span) {count(span);}
    @Override
    public void visit(Image image) {count(image);}
    @Override
    public void visit(Character character) {count(character);}
    @Override
    public void visit(Bold bold) {count(bold);}
    @Override
    public void visit(Italic italic) {count(italic);}
    @Override
    public void visit(Underline underline) {count(underline);}
    @Override
    public void visit(Font font) {count(font);}
    @Override
    public void visit(Glyph glyph) {count(glyph);}

    public void count(Glyph g) {
        if(g instanceof Character && !g.getContent().equals(""))
            character++;
        if(g instanceof Paragraph && g.getChildSize()!=0)
            paragraph++;
    }

    public int getCharacter(){
        return character;
    }
    public int getParagraph() { return paragraph; }
}
