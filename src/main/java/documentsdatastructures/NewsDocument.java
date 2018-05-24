package documentsdatastructures;

import com.google.common.base.CharMatcher;
import documentpreparing.DocumentPreparer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import storage.sourceparsers.SourceParser;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Setter
@Getter
public class NewsDocument {

    private static final DateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("dd MM yyyy HH:mm");
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GWT"));
    }

    private String title;
    private Date date;
    private String source;
    private String content;
    private String url;

    private MultiSet<String> wordsOccurrences;
    private Map<String, Double> normalizedTerms;


    public NewsDocument() {
        wordsOccurrences = new HashMultiSet<>();
        normalizedTerms = new HashMap<>();
    }

    private static String[] spaces = {"\t","\u1680","\u180e","\u202f","\u205f","\u3000",
            "\u2000","\u2001", "\u2002", "\u2003", "\u2004", "\u2005", "\u2006",
            "\u2007", "\u2008", "\u2009", "\u200a"};

    public NewsDocument(String source, File newsFile, SourceParser sourceParser) throws IOException, ParseException {
        this.source = source;

        String newsStr = FileUtils.readFileToString(newsFile);

        int urlEndInd = newsStr.indexOf('\n');
        url = newsStr.substring(0, urlEndInd);

        int titleEndInd = newsStr.indexOf('\n', urlEndInd + 1);
        title = newsStr.substring(urlEndInd + 1, titleEndInd);

        int dateEndInd = newsStr.indexOf('\n', titleEndInd + 1);
        date = loadDate(newsStr.substring(titleEndInd + 1, dateEndInd));

        String content = newsStr.substring(dateEndInd + 1);
        loadNewsDocument(Jsoup.parse(content), sourceParser);
    }

    private Date loadDate(String dateStr) throws ParseException {
        return DATE_FORMAT.parse(dateStr);
    }

    void loadNewsDocument(Document doc, SourceParser sourceParser) {
        //doc.select("a").remove();
        this.content = sourceParser.getContent(doc);
        wordsOccurrences = DocumentPreparer.parseContent(content + "\n" + title);
    }

    void normalize(InvertedIndex invertedIndex) {
        int documentsSize = invertedIndex.getDocumentsSize();
        normalizedTerms = new HashMap<>();

        double vectorLength = 0;

        for(MultiSet.Entry<String> termEntry : wordsOccurrences.entrySet()) {
            String term =  termEntry.getElement();
            int tf = termEntry.getCount();

            InvertedIndex.InvertedList invertedList = invertedIndex.getInvertedList(term);
            double idf = 0;
            if((invertedList != null) && (invertedList.size() > 0)) {
                idf = Math.log(documentsSize / invertedList.size());
            }

            double wf = tf > 0 ? 1 + Math.log(tf) : 0;
            //double wf = tf;
            double val = wf * idf;
            //val = termEntry.getCount() == 0 ? 0 : 1.0;
            //val =  tf;

            //SETTING VECTOR VALUES
            normalizedTerms.put(term ,val);
            vectorLength += val * val;
        }

        vectorLength = Math.sqrt(vectorLength);
        // NORMALIZATION
        for(String term : normalizedTerms.keySet()) {
            normalizedTerms.put(term, normalizedTerms.get(term) / vectorLength);
        }
    }


    public Set<String> getWords() {
        return wordsOccurrences.uniqueSet();
    }


    public MultiSet<String> getWordsOccurrences() {
        return wordsOccurrences;
    }


    public String getTitle() {
        return title;
    }

    public int getTermFrequency(String term) {
        return wordsOccurrences.getCount(term);
    }

    public double getNormolizedTermWeight(String term) {
        return normalizedTerms.get(term);
    }

    public void setTitle(String title) {
        for(String space : spaces) {
            title = title.replaceAll(space, " ");
        }
        title = CharMatcher.INVISIBLE.replaceFrom(title, " ");
        this.title = title;
    }

}
