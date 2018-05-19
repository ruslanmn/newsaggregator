package application;

import javafx.application.Application;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

public class NewsAggregatorApp extends Application {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ParseException {
        /*Date d = Calendar.getInstance().getTime();
        SimpleDateFormat smp = new SimpleDateFormat();
        smp.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println(smp.format(d));*/
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException, ParserConfigurationException, SAXException {
        primaryStage.setTitle("Client");
        PageManager pageManager = new PageManager();
        primaryStage.setScene(pageManager.getScene());
        primaryStage.show();
    }



}
