package sourcetester;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import storage.ConfigLoader;
import storage.sourceparsers.LentaParser;
import storage.sourceparsers.RamblerParser;
import storage.sourceparsers.SourceParser;

import java.io.File;
import java.io.IOException;

public class SourceTester {

    @Test
    void lentaTest() throws IOException {
        String url = "https://lenta.ru/news/2018/01/26/pornoapp";
        String content = FileUtils.readFileToString(new File(ConfigLoader.DOCUMENTS_DIR_PATH + "/1/558.txt"));
        Document doc = Jsoup.parse(content);//Jsoup.connect(url).get();

        SourceParser lentaParse = new LentaParser();
        content = lentaParse.getContent(doc);
        System.out.println(content);
    }


    @Test
    void ramblerTest() throws IOException {
        String url = "https://news.rambler.ru/economics/39011780-venesuele-prishlos-kupit-rossiyskuyu-neft/";
        Document doc = Jsoup.connect(url).get();

        SourceParser parse = new RamblerParser();
        System.out.println(parse.getContent(doc));
    }
}
