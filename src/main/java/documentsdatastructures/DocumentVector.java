package documentsdatastructures;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class DocumentVector implements Clusterable {
    double[] vec;
    NewsDocument document;

    public DocumentVector(int size, NewsDocument document) {
        vec = new double[size];
        this.document = document;
    }

    public void set(int i, double val) {
        vec[i] = val;
    }

    public double[] getPoint() {
        return vec;
    }

    public NewsDocument getDocument() {
        return document;
    }
}
