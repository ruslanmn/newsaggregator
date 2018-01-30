package storage.sourceparsers;

import org.jsoup.nodes.Document;

public interface SourceParser {
    String getContent(Document document);
}
