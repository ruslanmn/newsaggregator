package documentsdatastructures;

import documentpreparing.DocumentPreparer;
import documentprocessing.SearchResultItem;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.SetUtils;

import java.util.*;

public class InvertedIndex {
    HashMap<String, InvertedList> termsMap;
    List<NewsDocument> documents;

    public InvertedIndex() {
        termsMap = new HashMap<>();
        documents = new LinkedList<>();
    }

    public void add(NewsDocument document) {
        System.out.println(document.getTitle() + " added to inverted index");
        documents.add(document);
        for(String term : document.getWords()) {
            InvertedList invertedList;
            if(termsMap.containsKey(term))
                invertedList = termsMap.get(term);
            else {
                invertedList = new InvertedList();
                termsMap.put(term, invertedList);
            }
            invertedList.add(document);
        }
    }

    public void addAll(List<NewsDocument> newsDocuments) {
        for(NewsDocument newsDocument : newsDocuments) {
            add(newsDocument);
        }
    }

    public InvertedList getInvertedList(String term) {
        return termsMap.get(term);
    }

    public List<SearchResultItem> search(String query, int resultSize) {
        int documentsSize = documents.size();
        if(resultSize == 0)
            resultSize = documentsSize;
        MultiSet<String> queryTerms = DocumentPreparer.parseContent(query);

        HashMap<NewsDocument, Double> scores = new HashMap<>();

        for(String term : queryTerms) {
            InvertedList invertedList = getInvertedList(term);
            if(invertedList == null)
                continue;
            int df = invertedList.size();
            if(df == 0)
                continue;
            double idf = Math.log(documentsSize / df);

            for(NewsDocument newsDocument : invertedList.getDocuments()) {
                double score = newsDocument.getNormolizedTermWeight(term);
                if(scores.containsKey(newsDocument))
                    score += scores.get(newsDocument);
                scores.put(newsDocument, score);

            }
        }

        List<Map.Entry<NewsDocument, Double>> sortedNews = new ArrayList<>(scores.entrySet());
        Collections.sort(sortedNews, new Comparator<Map.Entry<NewsDocument, Double>>() {
            @Override
            public int compare(Map.Entry<NewsDocument, Double> o1, Map.Entry<NewsDocument, Double> o2) {
                double score1 = o1.getValue();
                double score2 = o2.getValue();

                if(score1 > score2)
                    return 1;
                else if(score1 < score2)
                    return -1;
                else
                    return 0;
            }
        });

        ArrayList<SearchResultItem> result = new ArrayList<>(resultSize);
        int i = 0;
        for(Map.Entry<NewsDocument, Double> entry : sortedNews) {
            if(i == resultSize)
                break;

            NewsDocument newsDocument = entry.getKey();
            SetUtils.SetView<String> foundTerms = SetUtils.intersection(newsDocument.getWords(), queryTerms.uniqueSet());
            result.add(new SearchResultItem(newsDocument, foundTerms));

            i++;
        }

        return result;
    }

    private int getDocumentFrequencyOfTerm(String term) {
        InvertedList invertedList = getInvertedList(term);
        return invertedList == null ? 0 : invertedList.size();
    }

    protected List<NewsDocument> getDocuments() {
        return documents;
    }

    protected HashMap<String,InvertedList> getTermsMap() {
        return termsMap;
    }

    public int getDocumentsSize() {
        return documents.size();
    }

    public void normalizeDocuments() {
        for(NewsDocument doc : documents)
            doc.normalize(this);
    }

    class InvertedList {
        List<NewsDocument> documents;

        public InvertedList() {
            documents = new LinkedList<>();
        }

        public void add(NewsDocument document) {
            documents.add(document);
        }

        public List<NewsDocument> getDocuments() {
            return documents;
        }

        public int size() {
            return documents.size();
        }
    }
}
