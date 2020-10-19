package Iterator;

import model.Glyph;

import java.util.Iterator;

public class ListGlyphIterator implements Iterator<Glyph> {
    int position = 0;
    ListGlyphStorage listGlyphStorage;
    public ListGlyphIterator(ListGlyphStorage listGlyphStorage){
        this.listGlyphStorage = listGlyphStorage;
    }

    @Override
    public boolean hasNext() {
        if(position >= listGlyphStorage.size() || listGlyphStorage.get(position) == null)
            return false;
        return true;
    }

    @Override
    public Glyph next() {
        return listGlyphStorage.get(position++);
    }

    @Override
    public void remove() {
        listGlyphStorage.remove(position-1);
        position -= 1;
    }
}
