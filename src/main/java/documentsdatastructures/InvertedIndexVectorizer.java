package documentsdatastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndexVectorizer {
    public static List<DocumentVector> vectorize(InvertedIndex invertedIndex) {
        List<DocumentVector> documentVectors = new ArrayList<>(invertedIndex.getDocumentsSize());

        Map<String, Double> idfs = computeIdfs(invertedIndex);
        Map<String, Integer> termsSpaceMap = formTermsSpace(invertedIndex);

        int termsSize = idfs.size();

        for(NewsDocument newsDocument : invertedIndex.getDocuments()) {
            DocumentVector documentVector = new DocumentVector(termsSize, newsDocument);
            for(String term : newsDocument.getWords())
                documentVector.set(termsSpaceMap.get(term), newsDocument.getNormolizedTermWeight(term));
            documentVectors.add(documentVector);
        }

        return documentVectors;
    }

    private static Map<String,Integer> formTermsSpace(InvertedIndex invertedIndex) {
        Map<String, Integer> termsSpaceMap = new HashMap<>();
        int ind = 0;

        for(String term : invertedIndex.getTermsMap().keySet()) {
            termsSpaceMap.put(term, ind);
            ind++;
        }

        return termsSpaceMap;
    }

    private static Map<String,Double> computeIdfs(InvertedIndex invertedIndex) {
        Map<String, Double> idfs = new HashMap<>();
        int documentsSize = invertedIndex.getDocumentsSize();

        for(Map.Entry<String, InvertedIndex.InvertedList> entry : invertedIndex.getTermsMap().entrySet()) {
            String term = entry.getKey();
            InvertedIndex.InvertedList invertedList = entry.getValue();
            int df = invertedList.size();
            double idf = df == 0 ? 0 : Math.log(documentsSize / df);
            idfs.put(term, idf);
        }

        return idfs;
    }
}
