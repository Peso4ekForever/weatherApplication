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

    public static void main(String[] args) throws IOException {
        WebParser webParser = new WebParser();
        webParser.getCities();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/weatherApplication.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load(), 1000, 640);
        Pane mainPane = (Pane)fxmlLoader.getNamespace().get("applicationMainPane");
        mainPane.setStyle("-fx-background-image: url('" + String.valueOf(this.getClass().getResource("/weatherImageBackground.jpg")) + "')");
        WebParser webParser = new WebParser();
        List<String> list = new ArrayList<String>(webParser.getCities().values());
        ComboBox comboBoxChangeCity = (ComboBox)fxmlLoader.getNamespace().get("comboBoxChangeCity");
        comboBoxChangeCity.getItems().addAll(
            list
        );
        primaryStage.setTitle("Погода");
        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.show();

        EventHandler<ActionEvent> event =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        WebParser currentCityParser = new WebParser();
                        try {
                            Weather weather = currentCityParser.ParseWeather((String) comboBoxChangeCity.getValue());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        Label selectedCity = (Label)fxmlLoader.getNamespace().get("selectedCity");
                        selectedCity.setText((String) comboBoxChangeCity.getValue());
                    }
                };
        comboBoxChangeCity.setOnAction(event);

    }

    public void fillScene(){

    }
}
