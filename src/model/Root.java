package model;

import Iterator.ArrayGlyphStorage;
import Iterator.GlyphStorage;
import visitor.Visitor;

import java.util.Iterator;

public class Root extends Glyph{
    GlyphStorage glyphs = new ArrayGlyphStorage();
    String tagname = "body";
    String attribute = "";
    public Root(){}

    @Override
    public void insert(Glyph g) {
        glyphs.add(g);
    }
    @Override
    public void remove(Glyph g) {
        glyphs.remove(g);
    }

    @Override
    public Glyph getChild(int i) { return glyphs.get(i); }
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
