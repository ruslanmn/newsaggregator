package documentsdatastructures;

import documentpreparing.DocumentPreparer;
import lombok.Getter;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import storage.sourceparsers.SourceParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NewsDocument {

    String title;
    MultiSet<String> wordsOccurrences;

    Map<String, Double> normalizedTerms;

    @Getter
    String sourceName;

    @Getter
    String content;

    public NewsDocument(String title, String url) throws IOException {
        this.title = title;
        Document doc = Jsoup.connect(url).get();
    }

    public NewsDocument(String sourceName, File newsFile, SourceParser sourceParser) throws IOException {
        this.sourceName = sourceName;
        String newsStr = FileUtils.readFileToString(newsFile);
        int newLineInd = newsStr.indexOf('\n');
        String title = newsStr.substring(0, newLineInd);
        String content = newsStr.substring(newLineInd + 1);
        loadNewsDocument(title, Jsoup.parse(content), sourceParser);
    }

    void loadNewsDocument(String title, Document doc, SourceParser sourceParser) {
        this.title = title;
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

            double val = tf * idf;
            normalizedTerms.put(term ,val);
            vectorLength += val * val;
        }

        vectorLength = Math.sqrt(vectorLength);

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
}
