package utils;

import org.jsoup.Jsoup;

import java.io.IOException;

public class URLLoader {
    public static String loadContentByUrl(String urlStr) throws IOException {
        String content = Jsoup.connect(urlStr).get().toString();
        return content;
    }
}
