package formatting;

import model.Glyph;
import utils.parseArgs;

public interface Formatting {
    parseArgs parse(Glyph glyph);
    String getTYPE();
}
