package model;

import Iterator.Iterator;
import Iterator.GlyphListIterator;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;


public class Root extends Glyph{
    List<Glyph> glyphs = new ArrayList<>();
    String tagname = "body";
    String attribute = "";
    public Root(){}

    @Override
    public void insert(Glyph g) {
        glyphs.add(g);
    }
    @Override
    public void remove(int i) {
        glyphs.remove(i);
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
    public Iterator getIterator() {
        return new GlyphListIterator(this);
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
    public String getContent() { return ""; }

    @Override
    public boolean isSingleTag() { return false; }
    @Override
    public void accept(Visitor visitor) {
        Iterator iterator = this.getIterator();
        while (iterator.hasNext()) {
            iterator.next().accept(visitor);
        }
        visitor.visit(this);
    }
}
