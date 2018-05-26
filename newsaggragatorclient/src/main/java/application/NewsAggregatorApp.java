package application;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class NewsAggregatorApp extends Application {

    public static void main(String[] args) {
        /*Date d = Calendar.getInstance().getTime();
        SimpleDateFormat smp = new SimpleDateFormat();
        smp.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println(smp.format(d));*/
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Новостной агрегатор");
        PageManager pageManager = new PageManager();
        primaryStage.setScene(pageManager.getScene());
        primaryStage.show();
    }



}
