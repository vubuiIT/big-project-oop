package com.example.demojavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GUI5Controller implements Initializable {

    @FXML
    private Button cancel_btn;

    @FXML
    private ComboBox<Integer> clsDay_comb;

    @FXML
    private ComboBox<Integer> clsHour_comb;

    @FXML
    private ComboBox<Integer> clsMin_comb;

    @FXML
    private ComboBox<String> clsMonth_comb;

    @FXML
    private ComboBox<Integer> clsYear_comb;

    @FXML
    private Button create_btn;

    @FXML
    private TextArea description_fld;

    @FXML
    private TextField name_fld;

    @FXML
    private ComboBox<Integer> opnDay_comb;

    @FXML
    private ComboBox<Integer> opnHour_comb;

    @FXML
    private ComboBox<Integer> opnMin_comb;

    @FXML
    private ComboBox<String> opnMonth_comb;

    @FXML
    private ComboBox<Integer> opnYear_comb;

    @FXML
    private Label switch_lbl;

    @FXML
    private TextField timeLimit_fld;

    @FXML
    private CheckBox Checkbox_open;

    @FXML
    private CheckBox Checkbox_close;

    @FXML
    private CheckBox Checkbox_Timelimit;

    @FXML
    private ComboBox<?> Timelimit_comb;

    @FXML
    private CheckBox Checkbox_Description;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> monthList = FXCollections.observableArrayList(
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        );
        ObservableList<Integer> dayList = FXCollections.observableArrayList();
        for (int i=1;i<=31;i++) {
            dayList.add(i);

        }
        ObservableList<Integer> yearList = FXCollections.observableArrayList();
        for (int i=2023;i<=2099;i++) {
            yearList.add(i);

        }
        ObservableList<Integer> hourList = FXCollections.observableArrayList();
        for (int i=0;i<=23;i++) {
            hourList.add(i);
        }
        ObservableList<Integer> minList = FXCollections.observableArrayList();
        for (int i=0;i<=59;i++) {
            minList.add(i);
        }
        // Add days to the ComboBox
        opnDay_comb.getItems().addAll(dayList);
        clsDay_comb.getItems().addAll(dayList);
        // Add months to the ComboBox
        opnMonth_comb.getItems().addAll(monthList);
        clsMonth_comb.getItems().addAll(monthList);
        // Add years to the ComboBox
        opnYear_comb.getItems().addAll(yearList);
        clsYear_comb.getItems().addAll(yearList);
        // Add hours to the ComboBox
        opnHour_comb.getItems().addAll(hourList);
        clsHour_comb.getItems().addAll(hourList);
        // Add hours to the ComboBox
        opnMin_comb.getItems().addAll(minList);
        clsMin_comb.getItems().addAll(minList);
    }
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private void handleCheckBoxOpen(ActionEvent event) {
        boolean disableCombos = Checkbox_open.isSelected();
        opnDay_comb.setDisable(!disableCombos);
        opnHour_comb.setDisable(!disableCombos);
        opnMin_comb.setDisable(!disableCombos);
        opnMonth_comb.setDisable(!disableCombos);
        opnMin_comb.setDisable(!disableCombos);
        opnYear_comb.setDisable(!disableCombos);
    }

    @FXML
    private void handleCheckBoxClose(ActionEvent event) {
        boolean enableCombos = Checkbox_close.isSelected();
        clsDay_comb.setDisable(!enableCombos);
        clsHour_comb.setDisable(!enableCombos);
        clsMin_comb.setDisable(!enableCombos);
        clsMonth_comb.setDisable(!enableCombos);
        clsYear_comb.setDisable(!enableCombos);
    }

    @FXML
    private void handleTimelimit(ActionEvent event) {
        boolean enableComboslimit = Checkbox_Timelimit.isSelected();
        timeLimit_fld.setDisable(!enableComboslimit);
        Timelimit_comb.setDisable(!enableComboslimit);
    }

    @FXML
    private void Close_page(ActionEvent event) {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
}
