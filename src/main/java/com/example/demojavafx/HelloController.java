package com.example.demojavafx;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Button Menu;

    @FXML
    private Button MenuBack;

    @FXML
    private GridPane slider;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        slider.setTranslateX(-700);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-700);
            slide.setOnFinished(e -> {
                Menu.setVisible(false);
                MenuBack.setVisible(true);
            });

        });
        MenuBack.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-700);
            slide.play();

            slider.setTranslateX(-700);
            slide.setOnFinished(e -> {
                Menu.setVisible(true);
                MenuBack.setVisible(false);
            });

        });
    }
}
