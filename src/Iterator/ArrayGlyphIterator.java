package Iterator;

import model.Glyph;

import java.util.Iterator;

public class ArrayGlyphIterator implements Iterator<Glyph> {
    int position = 0;
    ArrayGlyphStorage arrayGlyphStorage;
    public ArrayGlyphIterator(ArrayGlyphStorage arrayGlyphStorage){
        this.arrayGlyphStorage = arrayGlyphStorage;
    }

    @Override
    public boolean hasNext() {
        if(position >= arrayGlyphStorage.size() || arrayGlyphStorage.get(position) == null)
            return false;
        return true;
    }

    @Override
    public Glyph next() {
        return arrayGlyphStorage.get(position++);
    }

    @Override
    public void remove() {
        arrayGlyphStorage.remove(position-1);
        position -= 1;
    }
}
