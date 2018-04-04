package documentsdatastructures;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class VectorizedDocuments {
    Map<String, Integer> termsSpaceMap;
    List<DocumentVector> documentVectors;
}
