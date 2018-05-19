package storage.sourceparsers;

import storage.Source;
import java.lang.UnsupportedOperationException;

public class SourceParserFactory {

    private final static LentaParser lentaParser = new LentaParser();
    private final static RamblerParser ramblerParser = new RamblerParser();

    public static SourceParser get(Source source) throws UnsupportedOperationException {
        if(source.getUrl().contains("lenta.ru"))
            return lentaParser;
        else if(source.getUrl().contains("rambler.ru"))
            return ramblerParser;
        else
            throw new UnsupportedOperationException();
    }
}
