package ru.peso4ek.weather;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.peso4ek.weather.utils.WebParser;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) throws IOException {
        WebParser webParser = new WebParser();
        webParser.getCities();
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
