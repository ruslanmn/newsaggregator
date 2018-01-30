package storage.sourceparsers;

import storage.Source;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SourceParserFactory {

    private final static LentaParser lentaParser = new LentaParser();
    private final static RamblerParser ramblerParser = new RamblerParser();

    public static SourceParser get(Source source) throws NotImplementedException {
        if(source.getUrl().contains("lenta.ru"))
            return lentaParser;
        else if(source.getUrl().contains("rambler.ru"))
            return ramblerParser;
        else
            throw new NotImplementedException();
    }
}
