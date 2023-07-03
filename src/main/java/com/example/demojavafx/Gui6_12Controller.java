package com.example.demojavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

import java.beans.EventHandler;
import java.util.List;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Gui6_12Controller implements Initializable {
    Integer number=1;
    @FXML
    private MenuItem add62a;

    @FXML
    private MenuItem add62b;

    @FXML
    private Button editquiz;

    @FXML
    private VBox listQues;

    @FXML
    private Label numOfQues_lbl;

    @FXML
    private Button preview_btn;

    @FXML
    private Label timeLimit_lbl;

    @FXML
    private Label totalMark_lbl;

    @FXML
    private VBox vbox61;

    @FXML
    private VBox vbox62;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editquiz.setOnMouseClicked(event -> {
            vbox61.setVisible(false);
            vbox62.setVisible(true);
        });

        //mở Gui63 khi ấn Add from the question bank
        add62a.setOnAction(event -> {
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Gui63.fxml"));
                Parent root = loader.load();

                Gui6_3Controller controller = loader.getController();
                controller.setStage(stage);

                Scene scene = new Scene(root);
                stage.setTitle("Add from the question bank at the end");
                stage.setScene(scene);
                stage.show();
                controller.isCloseProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        List<HBox> selectedBoxes = controller.getSelectedBoxes();
                        try {
                            updateQuestionList(selectedBoxes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //mở Gui65 khi ấn Add a random question
        add62b.setOnAction(event1 -> {
            try {
                Stage stage = new Stage();
                FXMLLoader loaderr = new FXMLLoader(getClass().getResource("GUI65.fxml"));
                Parent root = loaderr.load();

                GUI65Controller controller = loaderr.getController();
                controller.setStage(stage);

                Scene scene = new Scene(root);
                stage.setTitle("Add a random question to page 1");
                stage.setScene(scene);
                stage.show();
//                controller.isCloseProperty().addListener((observable, oldValue, newValue) -> {
//                    if (newValue) {
//                        List<HBox> selectedBoxes = controller.getSelectedBoxes();
//                        try {
//                            updateQuestionList(selectedBoxes);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public void updateQuestionList(List<HBox> selectedBoxes) throws IOException {
        for (HBox selectedBox : selectedBoxes) {
            // Lấy các thông tin từ ô được chọn
            Label nameLabel1 = (Label) selectedBox.lookup("#name_lb");
            Label textLabel1 = (Label) selectedBox.lookup("#text_lbl");
            Label quesId = (Label) selectedBox.lookup("#id_lbl");
            String name = nameLabel1.getText();
            String text = textLabel1.getText();
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("64boxtick.fxml"));
            HBox boxtick = loader1.load();
            // Truy cập vào label theo ID và thay đổi nội dung
            Label nameLabel = (Label) boxtick.lookup("#name_lb");
            Label textLabel = (Label) boxtick.lookup("#text_lbl");
            Label quesNumber = (Label) boxtick.lookup("#rank_lbl");
            Label quesId1 = (Label) boxtick.lookup("#quesID_lbl");
            nameLabel.setText(name);
            quesNumber.setText(""+number);
            totalMark_lbl.setText(""+number+".00");
            number++;
            textLabel.setText(text);
            quesId1.setUserData(quesId.getUserData());
            listQues.getChildren().add(boxtick);
        }
    }
}