package documentsdatastructures;

import java.util.*;

public class NewsVectorizer {
    List<NewsDocument> documents;
    Map<String, Integer> totalWords;
    int top;

    public NewsVectorizer() {
        totalWords = new HashMap<>();
        documents = new LinkedList<>();
        top = 0;
    }

    public void add(NewsDocument doc) {
        documents.add(doc);
        appendWords(doc.getWords());
    }

    private void appendWords(Set<String> docWords) {
        for(String word : docWords) {
            if(!totalWords.containsKey(word)) {
                totalWords.put(word, top);
                top++;
            }
        }
    }

    public List<DocumentVector> vectorize() {
        ArrayList<DocumentVector> documentVectors = new ArrayList<>(documents.size());
        for(NewsDocument doc : documents) {
            documentVectors.add(buildDocumentVectorizer(doc));
        }

        return documentVectors;
    }

    private DocumentVector buildDocumentVectorizer(NewsDocument document) {
        DocumentVector dVec = new DocumentVector(totalWords.size(), document);
        for(String word : document.getWords()) {
            dVec.set(totalWords.get(word), 1);
        }

        return dVec;
    }
}
