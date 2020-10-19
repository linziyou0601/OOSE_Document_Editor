package Iterator;

import model.Glyph;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public interface GlyphStorage {
    void add(int index, Glyph element);
    void add(Glyph element);
    void remove(int index);
    void remove(Glyph glyph);
    int size();
    Glyph get(int index);
    Iterator<Glyph> iterator();
}