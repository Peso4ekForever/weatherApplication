package ru.peso4ek.weather;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        Scene mainScene = new Scene(fxmlLoader.load(), 1260, 750);
        Pane mainPane = (Pane) fxmlLoader.getNamespace().get("applicationMainPane");
        mainPane.setStyle("-fx-background-image: url('" + String.valueOf(this.getClass().getResource("/weatherImageBackground.jpg")) + "')");

        WebParser webParser = new WebParser();
        List<String> citiesList = new ArrayList<String>(webParser.getCities().keySet());

        ComboBox comboBoxChangeCity = (ComboBox) fxmlLoader.getNamespace().get("comboBoxChangeCity");
        comboBoxChangeCity.getItems().addAll(citiesList);
        for (int i = 0; i < 10; i++){
            this.setImages(i);
        }

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

                            for (int i = 0; i < 10; i++){
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
        comboBoxChangeCity.getSelectionModel().select(103);
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
        pressureLabel.setText(day.getPressure());

        Label wetLabel = (Label) fxmlLoader.getNamespace().get("wet" + index);
        wetLabel.setText(day.getWet());

        Label windLabel = (Label) fxmlLoader.getNamespace().get("wind" + index);
        windLabel.setText(day.getWind());

        Label uvIndexLabel = (Label) fxmlLoader.getNamespace().get("uvIndex" + index);
        uvIndexLabel.setText(day.getUvIndex());

        Label downfallChanceLabel = (Label) fxmlLoader.getNamespace().get("downfallChance" + index);
        downfallChanceLabel.setText(day.getDownfallChance());
    }

    public void setImages(int index){
        ImageView pressureImage = (ImageView) fxmlLoader.getNamespace().get("pressureImage" + index);
        pressureImage.setImage(new Image(String.valueOf(this.getClass().getResource("/pressureImage.png"))));

        ImageView wetImage = (ImageView) fxmlLoader.getNamespace().get("wetImage" + index);
        wetImage.setImage(new Image(String.valueOf(this.getClass().getResource("/wetImage.png"))));

        ImageView windImage = (ImageView) fxmlLoader.getNamespace().get("windImage" + index);
        windImage.setImage(new Image(String.valueOf(this.getClass().getResource("/windImage.png"))));

        ImageView uvIndexImage = (ImageView) fxmlLoader.getNamespace().get("uvIndexImage" + index);
        uvIndexImage.setImage(new Image(String.valueOf(this.getClass().getResource("/uvIndexImage.png"))));

        ImageView downfallImage = (ImageView) fxmlLoader.getNamespace().get("downfallImage" + index);
        downfallImage.setImage(new Image(String.valueOf(this.getClass().getResource("/downfallChanceImage.png"))));
    }
}
