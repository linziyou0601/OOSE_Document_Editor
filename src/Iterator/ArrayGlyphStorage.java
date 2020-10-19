package Iterator;

import model.Glyph;

import java.util.Iterator;

public class ArrayGlyphStorage implements GlyphStorage{
    private Glyph[] glyphs = new Glyph[0];
    private int index = 0;

    @Override
    public void add(int index, Glyph element) {
        int i = index++;
        expandArray(glyphs.length+1);
        for(int pos = i+1; i < glyphs.length-1; i++){
            glyphs[pos+1] = glyphs[pos];
        }
        glyphs[i] = element;
    }

    @Override
    public void add(Glyph element) {
        int i = index++;
        expandArray(glyphs.length+1);
        glyphs[i] = element;
    }

    @Override
    public void remove(int index) {
        if(index < 0)
            throw new ArrayIndexOutOfBoundsException();
        for(int pos = index; pos < glyphs.length-1; pos++){
            glyphs[pos] = glyphs[pos+1];
        }
        glyphs[glyphs.length-1] = null;
    }

    @Override
    public void remove(Glyph glyph) {
        int pos = 0;
        while(pos<glyphs.length || !glyphs[pos].equals(glyph)){
            pos++;
        }
        this.remove(pos);
    }

    @Override
    public int size() {
        return glyphs.length;
    }

    @Override
    public Glyph get(int index) {
        if(index >= glyphs.length)
            throw new ArrayIndexOutOfBoundsException();
        return glyphs[index];
    }

    @Override
    public Iterator<Glyph> iterator() {
        return new ArrayGlyphIterator(this);
    }

    private void expandArray(int size) {
        if(size >= glyphs.length) {
            Glyph[] newGlyphs = new Glyph[size];
            System.arraycopy(glyphs, 0, newGlyphs, 0, glyphs.length);
            glyphs = newGlyphs;
        }
    }
}
