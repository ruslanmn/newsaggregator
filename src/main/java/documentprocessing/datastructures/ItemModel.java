package documentprocessing.datastructures;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ItemModel {
    String title;
    String sourceName;
    Date date;
    double distanceFromCentroid;
}
