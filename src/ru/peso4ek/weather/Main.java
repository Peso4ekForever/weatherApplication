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
        Scene mainScene = new Scene(fxmlLoader.load(), 1260, 640);
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

                            for (int i = 1; i < 10; i++){
                                fillScene(i,weather);
                            }

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

        Label dateLabel = (Label) fxmlLoader.getNamespace().get("date" + index);
        dateLabel.setText(day.getDate());

        Label temperatureLabel = (Label) fxmlLoader.getNamespace().get("temperature" + index);
        temperatureLabel.setText(day.getTemperature());

        Label descriptionLabel = (Label) fxmlLoader.getNamespace().get("description" + index);
        descriptionLabel.setText(day.getDescription());

        Label pressureLabel = (Label) fxmlLoader.getNamespace().get("pressure" + index);
        pressureLabel.setText(day.getPressure() + " рт.ст.");

        Label wetLabel = (Label) fxmlLoader.getNamespace().get("wet" + index);
        wetLabel.setText("Влажность: " + day.getWet());

        Label windLabel = (Label) fxmlLoader.getNamespace().get("wind" + index);
        windLabel.setText("Ветер: " + day.getWind());

        Label uvIndexLabel = (Label) fxmlLoader.getNamespace().get("uvIndex" + index);
        uvIndexLabel.setText("Индекс UV: " + day.getUvIndex());

        Label downfallChanceLabel = (Label) fxmlLoader.getNamespace().get("downfallChance" + index);
        downfallChanceLabel.setText("Вер. осадков: " + day.getDownfallChance());
    }
}
