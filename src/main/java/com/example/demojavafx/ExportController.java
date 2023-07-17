package com.example.demojavafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExportController implements Initializable{
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FlowPane flowPane;
    @FXML
    private float scores = 0;
    @FXML
    private Label additionalLabel;
    public Quiz quiz;
    @FXML
    public VBox snapBox;
    private boolean isSub=false;
    public void setquiz(Quiz quiz) {
        this.quiz = quiz;
        //System.out.println(this.quiz.getId());
        DatabaseConnector connector = new DatabaseConnector();
        connector.connect();
        VBox container = new VBox();
        try {
            //System.out.println(quizid);
            List<Question> questions = connector.getQuestionsFromQuiz(this.quiz.getId());
            for (Question question : questions) {
                VBox vbox = createVBox(container.getChildren().size() + 1);
                Label label= (Label) vbox.getChildren().get(0);
                int indexQues=Integer.valueOf(label.getText());
                flowPane.getChildren().add(vbox);
                List<Choices> choices = connector.getChoicesFromQuestion(question.getId());
                boolean check = false;
                for (Choices choice : choices) {
                    if (choice.getGrade() != 0.00) {
                        if (check) {
                            check = false;
                            break;
                        } else check = true;
                    }
                }
                FXMLLoader Loader = new FXMLLoader(getClass().getResource("questionBox.fxml"));
                if (check) Loader = new FXMLLoader(getClass().getResource("boxQuestion.fxml"));
                Parent root = Loader.load();
                Label num = (Label) root.lookup("#num");
                num.setText("" + (container.getChildren().size() + 1));
                Label name = (Label) root.lookup("#questionName");
                name.setText(question.getName() + " : " + question.getText());
                ImageView image = (ImageView) root.lookup("#imagetext");
                if(question.getMedia()!=null) {
                    InputStream inputStream = new ByteArrayInputStream(question.getMedia());
                    image.setImage(new Image(inputStream));
                }
                ToggleButton text1 = (ToggleButton) root.lookup("#choice1");
                text1.setVisible(false);
                ToggleButton text2 = (ToggleButton) root.lookup("#choice2");
                text2.setVisible(false);
                ToggleButton text3 = (ToggleButton) root.lookup("#choice3");
                text3.setVisible(false);
                ToggleButton text4 = (ToggleButton) root.lookup("#choice4");
                text4.setVisible(false);
                ToggleButton text5 = (ToggleButton) root.lookup("#choice5");
                text5.setVisible(false);

                int numChoices = 0;
                for (Choices choice : choices) {
                    numChoices++;
                    if (numChoices == 1) {
                        ToggleButton text = (ToggleButton) root.lookup("#choice1");
                        text.setText(choice.getText());
                        text.setVisible(true);
                        if(choice.getPic()!=null) {
                            InputStream inputStream = new ByteArrayInputStream(choice.getPic());
                            ImageView choice1image = (ImageView) root.lookup("#choice1image");
                            choice1image.setImage(new Image(inputStream));
                        }
                        text.setOnMouseClicked(event -> {
                            if(text.isSelected()) {
                                Node box = flowPane.getChildren().get(indexQues-1);
                                box.setStyle("-fx-background-color: #000000");
                            }
                        });
                    }
                    if (numChoices == 2) {
                        ToggleButton text = (ToggleButton) root.lookup("#choice2");
                        text.setText(choice.getText());
                        text.setVisible(true);
                        if(choice.getPic()!=null) {
                            InputStream inputStream = new ByteArrayInputStream(choice.getPic());
                            ImageView choice2image = (ImageView) root.lookup("#choice2image");
                            choice2image.setImage(new Image(inputStream));
                        }
                        text.setOnMouseClicked(event -> {
                            if(text.isSelected()) {
                                Node box = flowPane.getChildren().get(indexQues-1);
                                box.setStyle("-fx-background-color: #000000");
                            }
                        });
                    }
                    if (numChoices == 3) {
                        ToggleButton text = (ToggleButton) root.lookup("#choice3");
                        text.setText(choice.getText());
                        text.setVisible(true);
                        if(choice.getPic()!=null) {
                            InputStream inputStream = new ByteArrayInputStream(choice.getPic());
                            ImageView choice3image = (ImageView) root.lookup("#choice3image");
                            choice3image.setImage(new Image(inputStream));
                        }
                        text.setOnMouseClicked(event -> {
                            if(text.isSelected()) {
                                Node box = flowPane.getChildren().get(indexQues-1);
                                box.setStyle("-fx-background-color: #000000");
                            }
                        });
                    }
                    if (numChoices == 4) {
                        ToggleButton text = (ToggleButton) root.lookup("#choice4");
                        text.setText(choice.getText());
                        text.setVisible(true);
                        if(choice.getPic()!=null) {
                            InputStream inputStream = new ByteArrayInputStream(choice.getPic());
                            ImageView choice4image = (ImageView) root.lookup("#choice4image");
                            choice4image.setImage(new Image(inputStream));
                        }
                        text.setOnMouseClicked(event -> {
                            if(text.isSelected()) {
                                Node box = flowPane.getChildren().get(indexQues-1);
                                box.setStyle("-fx-background-color: #000000");
                            }
                        });

                    }
                    if (numChoices == 5) {
                        ToggleButton text = (ToggleButton) root.lookup("#choice5");
                        text.setText(choice.getText());
                        text.setVisible(true);
                        if(choice.getPic()!=null) {
                            InputStream inputStream = new ByteArrayInputStream(choice.getPic());
                            ImageView choice5image = (ImageView) root.lookup("#choice5image");
                            choice5image.setImage(new Image(inputStream));
                        }
                        text.setOnMouseClicked(event -> {
                            if(text.isSelected()) {
                                Node box = flowPane.getChildren().get(indexQues-1);
                                box.setStyle("-fx-background-color: #000000");
                            }
                        });
                    }
                }
                container.getChildren().add(root);
            }
            scrollPane.setContent(container); snapBox = container;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private VBox createVBox(int number) {
        VBox vbox = new VBox();
        vbox.setPrefSize(30, 40);
        vbox.setStyle("-fx-background-color: #ffffff");
        vbox.getStyleClass().add("vbox-with-border");
        vbox.setAlignment(Pos.TOP_CENTER); // Đặt căn giữa trên cùng của VBox
        vbox.getChildren().add(createLabel(number));
        return vbox;
    }
    private boolean kt = true;
    private Label createLabel(int number) {
        Label label = new Label("" + number);
        label.setPrefSize(30, 20); // Đặt kích thước cho Label
        label.setStyle("-fx-font-size: 13px; -fx-background-color: #f8f5f5; -fx-text-fill: #000000;"); // Đặt màu nền và màu chữ
        VBox.setMargin(label, new Insets(0, 0, 0, 0)); // Đặt khoảng cách canh trên của VBox
        VBox.setVgrow(label, Priority.NEVER); // Đặt không cho phép mở rộng kích thước của Label
        label.setAlignment(Pos.CENTER); // Đặt canh giữa theo chiều ngang và dọc
        return label;
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //System.out.println(quiz.getId());

    }
}