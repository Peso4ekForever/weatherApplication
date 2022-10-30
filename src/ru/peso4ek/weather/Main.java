package ru.peso4ek.weather;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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

        WebParser webParser = new WebParser();
        List<String> citiesList = new ArrayList<String>(WebParser.getCities().keySet());

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
        setMainImages(weather, index);

        Label dateLabel = (Label) fxmlLoader.getNamespace().get("date" + index);
        dateLabel.setText(day.getDate());

        Label temperatureLabel = (Label) fxmlLoader.getNamespace().get("temperature" + index);
        temperatureLabel.setText(day.getTemperature());

        Label descriptionLabel = (Label) fxmlLoader.getNamespace().get("description" + index);
        descriptionLabel.setText(day.getDescription());
        if (index != 0) {
            setWeatherImages(weather, index);
        }

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

    public void setMainImages(Weather weather, int index){
        Pane mainPane = (Pane) fxmlLoader.getNamespace().get("applicationMainPane");
        ImageView weatherImage = (ImageView) fxmlLoader.getNamespace().get("weatherImage" + index);
        String currentDayDescription = weather.getDay(index).getDescription();
        String[] description = currentDayDescription.split(" ");
        if (index == 0){
            for (String element: description) {
                switch (element) {
                    case "ясно":
                        mainPane.setStyle("-fx-background-image: url('" + this.getClass().getResource("/clearBackground.jpg") + "')");
                        weatherImage.setImage(new Image(String.valueOf(this.getClass().getResource("/sunImage.png"))));
                        break;
                    case "малооблачно":
                    case "облачно":
                    case "облачность":
                        mainPane.setStyle("-fx-background-image: url('" + this.getClass().getResource("/cloudyBackground.jpg") + "')");
                        weatherImage.setImage(new Image(String.valueOf(this.getClass().getResource("/cloudImage.png"))));
                        break;
                    case "снег":
                        mainPane.setStyle("-fx-background-image: url('" + this.getClass().getResource("/snowBackground.jpg") + "')");
                        weatherImage.setImage(new Image(String.valueOf(this.getClass().getResource("/snowImage.png"))));
                        break;
                    case "осадки":
                    case "дождь":
                        mainPane.setStyle("-fx-background-image: url('" + this.getClass().getResource("/rainBackground.jpg") + "')");
                        weatherImage.setImage(new Image(String.valueOf(this.getClass().getResource("/rainImage.png"))));
                        break;
                    case "морось":
                        mainPane.setStyle("-fx-background-image: url('" + this.getClass().getResource("/drizzleBackground.jpg") + "')");
                        weatherImage.setImage(new Image(String.valueOf(this.getClass().getResource("/cloudImage.png"))));
                        break;
                    case "дымка":
                        weatherImage.setImage(new Image(String.valueOf(this.getClass().getResource("/cloudImage.png"))));
                        mainPane.setStyle("-fx-background-image: url('" + this.getClass().getResource("/hazeBackground.jpg") + "')");
                        break;
                }
            }
        }
    }

    public void setWeatherImages(Weather weather, int index){
        String dayDescription = weather.getDay(index).getDescription();
        ImageView weatherImage = (ImageView) fxmlLoader.getNamespace().get("weatherImage" + index);
        switch (dayDescription){
            case "снег":
                weatherImage.setImage(new Image(String.valueOf(this.getClass().getResource("/snowImage.png"))));
                break;
            case "осадки":
                weatherImage.setImage(new Image(String.valueOf(this.getClass().getResource("/precipitationImage.png"))));
                break;
            case "дождь":
                weatherImage.setImage(new Image(String.valueOf(this.getClass().getResource("/rainImage.png"))));
                break;
            case "облачно":
                weatherImage.setImage(new Image(String.valueOf(this.getClass().getResource("/cloudImage.png"))));
                break;
            case "ясно":
                weatherImage.setImage(new Image(String.valueOf(this.getClass().getResource("/sunImage.png"))));
                break;
        }
    }
}
