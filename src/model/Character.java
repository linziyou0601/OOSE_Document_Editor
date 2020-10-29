package model;

import visitor.Visitor;

public class Character extends Glyph{
    private String tagname = "character";
    private String attribute = "", content = "";
    public Character(){}
    @Override
    public int getChildSize() {
        return 0;
    }

    @Override
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getAttribute() {
        return attribute;
    }
    @Override
    public String getTagname() {
        return tagname;
    }
    @Override
    public String getContent() { return content; }

    @Override
    public boolean isSingleTag() { return true; }
    @Override
    public void accept(Visitor visitor) { visitor.visit(this); }
}
