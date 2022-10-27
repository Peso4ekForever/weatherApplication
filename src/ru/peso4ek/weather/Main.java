package ru.peso4ek.weather;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.peso4ek.weather.utils.WebParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    public FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/weatherApplication.fxml"));
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/weatherApplication.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load(), 1000, 640);
        Pane mainPane = (Pane) fxmlLoader.getNamespace().get("applicationMainPane");
        mainPane.setStyle("-fx-background-image: url('" + String.valueOf(this.getClass().getResource("/weatherImageBackground.jpg")) + "')");

        WebParser webParser = new WebParser();
        List<String> citiesList = new ArrayList<String>(webParser.getCities().keySet());

        ComboBox comboBoxChangeCity = (ComboBox) fxmlLoader.getNamespace().get("comboBoxChangeCity");
        comboBoxChangeCity.getItems().addAll(citiesList);

        primaryStage.setTitle("Погода");
        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.show();

        EventHandler<ActionEvent> event =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        WebParser currentCityParser = new WebParser();
                        try {
                            Weather weather = currentCityParser.ParseWeather(WebParser.getCityUrlByName((String) comboBoxChangeCity.getValue()));
                            Day day = weather.getDay(0);
                            Label todayPressure = (Label) fxmlLoader.getNamespace().get("todayPressure");
                            todayPressure.setText(day.getPressure());

                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        Label selectedCity = (Label) fxmlLoader.getNamespace().get("selectedCity");
                        selectedCity.setText((String) comboBoxChangeCity.getValue());

                    }
                };
        comboBoxChangeCity.setOnAction(event);
    }

    public void fillScene(int index, Weather weather) {
        Day day = weather.getDay(index);

        Label pressureLabel = (Label) fxmlLoader.getNamespace().get("pressure" + index);
        Label temperatureLabel = (Label) fxmlLoader.getNamespace().get("temperature" + index);
        Label descriptionLabel = (Label) fxmlLoader.getNamespace().get("description" + index);
        Label dateLabel = (Label) fxmlLoader.getNamespace().get("data" + index);
        Label wetLabel = (Label) fxmlLoader.getNamespace().get("wet" + index);
        Label windLabel = (Label) fxmlLoader.getNamespace().get("wind" + index);
        Label uvIndexLabel = (Label) fxmlLoader.getNamespace().get("uvIndex" + index);

        pressureLabel.setText(day.getPressure());
        temperatureLabel.setText(day.getTemperature());
        descriptionLabel.setText(day.getDescription());
        dateLabel.setText(day.getDate());
        wetLabel.setText(day.getWet());
        windLabel.setText(day.getWind());
        uvIndexLabel.setText(day.getUvIndex());

    }
}
