package com.example.demojavafx;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class GUI7Attempt implements Initializable{
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FlowPane flowPane;
    private VBox createVBox(int number) {
        VBox vbox = new VBox();
        vbox.setPrefSize(25, 35);
        vbox.setStyle("-fx-background-color: #f0f0f0");
        vbox.getStyleClass().add("vbox-with-border");
        vbox.setAlignment(Pos.TOP_CENTER); // Đặt căn giữa trên cùng của VBox
        vbox.getChildren().add(createLabel(number));
        return vbox;
    }

    private Label createLabel(int number) {
        Label label = new Label("" + number);
        label.setStyle("-fx-font-size: 13px");
        return label;
    }
    int score = 0;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int CategoryId = 23;
        try {
            System.out.println(1);
            VBox container = new VBox();
            DatabaseConnector connector = new DatabaseConnector();
            connector.connect();
            List<Question> questions = connector.getQuestionsFromCategory(CategoryId);
            for (Question question : questions) {
                FXMLLoader Loader = new FXMLLoader(getClass().getResource("questionBox.fxml"));
                Parent root = Loader.load();
                Label num = (Label) root.lookup("#num");
                num.setText("" + (container.getChildren().size() + 1));
                Label name = (Label) root.lookup("#questionName");
                name.setText(question.getName() + " : " + question.getText());

                CheckBox text1 = (CheckBox) root.lookup("#choice1");
                text1.setVisible(false);
                CheckBox text2 = (CheckBox) root.lookup("#choice2");
                text2.setVisible(false);
                CheckBox text3 = (CheckBox) root.lookup("#choice3");
                text3.setVisible(false);
                CheckBox text4 = (CheckBox) root.lookup("#choice4");
                text4.setVisible(false);
                CheckBox text5 = (CheckBox) root.lookup("#choice5");
                text5.setVisible(false);

                List<Choices> choices = connector.getChoicesFromQuestion(question.getId());
                int numChoices = 0;
                for (Choices choice : choices) {
                    numChoices++;
                    if (numChoices == 1) {
                        CheckBox text = (CheckBox) root.lookup("#choice1");
                        text.setText(choice.getText());
                        text.setVisible(true);
                        if(text.isSelected()) score += choice.getGrade();
                    }
                    if (numChoices == 2) {
                        CheckBox text = (CheckBox) root.lookup("#choice2");
                        text.setText(choice.getText());
                        text.setVisible(true);
                        if(text.isSelected()) score += choice.getGrade();
                    }
                    if (numChoices == 3) {
                        CheckBox text = (CheckBox) root.lookup("#choice3");
                        text.setText(choice.getText());
                        text.setVisible(true);
                        if(text.isSelected()) score += choice.getGrade();
                    }
                    if (numChoices == 4) {
                        CheckBox text = (CheckBox) root.lookup("#choice4");
                        text.setText(choice.getText());
                        text.setVisible(true);
                        if(text.isSelected()) score += choice.getGrade();
                    }
                    if (numChoices == 5) {
                        CheckBox text = (CheckBox) root.lookup("#choice5");
                        text.setText(choice.getText());
                        text.setVisible(true);
                        if(text.isSelected()) score += choice.getGrade();
                    }
                }
                VBox vbox = createVBox(container.getChildren().size()+1);
                flowPane.getChildren().add(vbox);
                container.getChildren().add(root);
            }
            scrollPane.setContent(container);
        }catch (Exception e) {
            e.printStackTrace();
        }
        Label additionalLabel = new Label("Finish attempt ... ");
        additionalLabel.setStyle("-fx-font-size: 14px");
        flowPane.getChildren().add(additionalLabel);
        additionalLabel.setOnMouseClicked(event -> {
            System.out.println(score);
        });
    }
}
