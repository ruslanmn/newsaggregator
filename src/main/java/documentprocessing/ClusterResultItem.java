package documentprocessing;

import documentsdatastructures.NewsDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClusterResultItem {
    NewsDocument newsDocument;
    double distanceFromCentroid;
}
