package com.example.demojavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TreeView;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    final boolean[] isPopupVisible = {false};
    @FXML
    private Text default2;

    @FXML
    private Button Menu;

    @FXML
    private Button MenuBack;

    @FXML
    private Button create1;

    @FXML
    private Spinner<?> default1;

    @FXML
    private TreeView <?> defaultPopup1;

    @FXML
    private TabPane hoiPopup;

    @FXML
    private ListView<?> list1;

    @FXML
    private Hyperlink questions;

    @FXML
    private GridPane slider;

    @FXML
    private CheckBox tick1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slider.setVisible(false);
        hoiPopup.setVisible(false);
        defaultPopup1.setVisible(false);

        // Hiện popup question bank từ nút gear
        Menu.setOnMouseClicked(event -> {
            list1.setVisible(false);
            slider.setVisible(true);
            Menu.setVisible(false);
            MenuBack.setVisible(true);
        });

        // Hiện popup questions từ question bank
        questions.setOnMouseClicked(event -> {
            hoiPopup.setVisible(true);
            slider.setVisible(false);
        });

        // Hiện popup default
        default1.setOnMousePressed(event -> {
            if (!isPopupVisible[0]) {
                defaultPopup1.setVisible(true);
                isPopupVisible[0] = true;
            } else {
                defaultPopup1.setVisible(false);
                isPopupVisible[0] = false;
            }
        });

        // Ẩn popup default khi click bên ngoài
        defaultPopup1.getParent().setOnMouseClicked(event -> {
            if (!defaultPopup1.getBoundsInParent().contains(event.getX(), event.getY())) {
                defaultPopup1.setVisible(false);
            }
        });
        // Quay về ban đầu
        MenuBack.setOnMouseClicked(event -> {
            list1.setVisible(true);
            slider.setVisible(false);
            hoiPopup.setVisible(false);
            Menu.setVisible(true);
            MenuBack.setVisible(false);
        });
    }
}
