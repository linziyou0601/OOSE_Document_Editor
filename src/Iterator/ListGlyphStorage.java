package Iterator;

import model.Glyph;

import java.util.ArrayList;
import java.util.Iterator;

public class ListGlyphStorage implements GlyphStorage{
    private ArrayList<Glyph> glyphs = new ArrayList<>();
    private int index = 0;

    @Override
    public void add(int index, Glyph element) {
        glyphs.add(index, element);
    }

    @Override
    public void add(Glyph element) {
        glyphs.add(element);
    }

    @Override
    public void remove(int index) {
        glyphs.remove(index);
    }

    @Override
    public void remove(Glyph glyph) {
        glyphs.remove(glyph);
    }

    @Override
    public int size() {
        return glyphs.size();
    }

    @Override
    public Glyph get(int index) {
        return glyphs.get(index);
    }

    @Override
    public Iterator<Glyph> iterator() {
        return new ListGlyphIterator(this);
    }
}
