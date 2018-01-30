package storage.sourceparsers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LentaParser implements SourceParser {
    @Override
    public String getContent(Document document) {
        Elements divs = document.select("div[class=b-text clearfix js-topic__text]");

        StringBuilder content = new StringBuilder();
        for(Element div : divs) {
            Elements ps = div.getElementsByTag("p");
            for (Element p : ps) {
                content.append(p.text()).append("\n");
            }
        }

        return content.toString();
    }
}
