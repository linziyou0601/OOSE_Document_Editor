package model;

import visitor.Visitor;

public class Bold extends Decorator{
    private String tagname = "b";
    private String attribute = "";
    public Bold(Glyph g){
        super.glyph = g;
    }
    @Override
    public int getChildSize() {
        return 1;
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
        super.accept(visitor);
        visitor.visit(this);
    }
}
