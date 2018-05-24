package clustering;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.MathArrays;

public class AngularDistanceMeasure implements DistanceMeasure {
    @Override
    public double compute(double[] a, double[] b) throws DimensionMismatchException {
        MathArrays.checkEqualLength(a, b);

        double similarity = 0;
        for(int i = 0; i < a.length; i++)
            similarity += a[i] * b[i];

        return 1 / similarity;//2 * Math.acos(similarity) / Math.PI;
    }
}
