import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.util.HashMap;

class Klass {
    int p;

    public Klass(int p) {
        this.p = p;
    }

    @Override
    public int hashCode() {
        return p;
    }

    @Override
    public boolean equals(Object o) {
        return p == ((Klass)o).p;
    }

}

public class TryingTests {



    public static void testUrl() throws IOException {
        //Jsoup.connect("https://en.wikipedia.org/");
        Document d = Jsoup.connect("https://en.wikipedia.org/wiki/USA").get();
        String text = Jsoup.clean(d.toString(), Whitelist.none());
        text = superTrim(text);
        System.out.println(text);


        final String url = "https://lenta.ru/rss/news";

    }

    public static void collisionTest() {
        HashMap<Klass, String> klasses = new HashMap<>();
        Klass f = new Klass(1);
        klasses.put(f, "first");
        klasses.put(new Klass(1), "second");
        System.out.println(klasses.get(new Klass(1)));
    }


    public static void main(String[] args) throws IOException {
        collisionTest();
    }

    private static int count(String text) {
        int c = 0;
        while(text.contains(" ")) {
            c++;
            text.replace(" ", "");
        }
        return c;
    }

    static String superTrim(String str) {
        while(str.contains("  "))
            str = str.replaceAll("  ", " ");
        return str;
    }

}
