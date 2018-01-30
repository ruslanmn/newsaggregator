package storage.sourceparsers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RamblerParser implements SourceParser {
    @Override
    public String getContent(Document document) {
        Elements divs = document.select("div[class=article__paragraph]");
        StringBuilder content = new StringBuilder();

        for(Element div : divs) {
            content.append(div.text()).append("\n");
        }

        return content.toString();
    }
}
