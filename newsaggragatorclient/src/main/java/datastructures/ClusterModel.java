package datastructures;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class ClusterModel {
    String name;
    List<ItemModel> itemModels;

    public ClusterModel() {
        itemModels = new LinkedList<>();
    }
}
