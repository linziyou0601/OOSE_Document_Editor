package model;

import Iterator.GlyphStorage;
import Iterator.ListGlyphStorage;
import visitor.Visitor;

import java.util.Iterator;

public class Paragraph extends Glyph{
    GlyphStorage glyphs = new ListGlyphStorage();
    String tagname = "p";
    String attribute = "";
    public Paragraph(){}

    @Override
    public void insert(Glyph g) {
        glyphs.add(g);
    }
    @Override
    public void remove(Glyph g) {
        glyphs.remove(g);
    }

    @Override
    public Glyph getChild(int i) {
        return glyphs.get(i);
    }
    @Override
    public int getChildSize() {
        return glyphs.size();
    }

    @Override
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getAttribute() {
        return attribute;
    }
    @Override
    public String getTagname() { return tagname; }

    @Override
    public boolean isSingleTag() { return false; }
    @Override
    public void accept(Visitor visitor) {
        Iterator<Glyph> iterator = glyphs.iterator();
        while (iterator.hasNext()) {
            iterator.next().accept(visitor);
        }
        visitor.visit(this);
    }
}
