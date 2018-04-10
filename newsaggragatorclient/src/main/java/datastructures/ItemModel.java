package datastructures;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemModel {
    String title;
    String source;
    Date date;
    double distance;

    public ItemModel(String title, String source, Date date, double distance) {
        this.title = title;
        this.source = source;
        this.date = date;
        this.distance = distance;
        this.url = "vk.com";
    }

    String url;

    @Override
    public String toString() {
        return title;
    }
}
