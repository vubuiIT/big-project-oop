package com.example.demojavafx;

import javafx.collections.ObservableList;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GUI7Attempt implements Initializable{
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FlowPane flowPane;
    @FXML
    private float scores = 0;
    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private VBox createVBox(int number) {
        VBox vbox = new VBox();
        vbox.setPrefSize(30, 40);
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String timeBegin = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy, h:mm a", Locale.ENGLISH));
        int CategoryId = 23;
        DatabaseConnector connector = new DatabaseConnector();
        connector.connect();
        VBox container = new VBox();
        try {
            List<Question> questions = connector.getQuestionsFromCategory(CategoryId);
            for (Question question : questions) {
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
                if(check) Loader = new FXMLLoader(getClass().getResource("boxQuestion.fxml"));
                Parent root = Loader.load();
                Label num = (Label) root.lookup("#num");
                num.setText("" + (container.getChildren().size() + 1));
                Label name = (Label) root.lookup("#questionName");
                name.setText(question.getName() + " : " + question.getText());

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
                    }
                    if (numChoices == 2) {
                        ToggleButton text = (ToggleButton) root.lookup("#choice2");
                        text.setText(choice.getText());
                        text.setVisible(true);
                    }
                    if (numChoices == 3) {
                        ToggleButton text = (ToggleButton) root.lookup("#choice3");
                        text.setText(choice.getText());
                        text.setVisible(true);
                    }
                    if (numChoices == 4) {
                        ToggleButton text = (ToggleButton) root.lookup("#choice4");
                        text.setText(choice.getText());
                        text.setVisible(true);
                    }
                    if (numChoices == 5) {
                        ToggleButton text = (ToggleButton) root.lookup("#choice5");
                        text.setText(choice.getText());
                        text.setVisible(true);
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
        Label additionalLabel = new Label("  Finish attempt ...  ");
        additionalLabel.setStyle("-fx-font-size: 14px");
        flowPane.getChildren().add(additionalLabel);
        additionalLabel.setOnMouseClicked(event -> {
            try {
                FXMLLoader Loader = new FXMLLoader(getClass().getResource("preview.fxml"));
                Parent rot = Loader.load();
                Text begin = (Text) rot.lookup("#begin");
                begin.setText(""+ timeBegin);
                Text end = (Text) rot.lookup("#end");
                String timeText = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy, h:mm a", Locale.ENGLISH));
                end.setText("" + timeText);
                Text grade = (Text) rot.lookup("#grade");
                container.getChildren().add(0,rot);
                List<Question> questions = connector.getQuestionsFromCategory(CategoryId);
                int cnt=0;
                for (Question question : questions) {
                    cnt+=2;
                    Node element = container.getChildren().get(cnt-1);
                    FXMLLoader Load = new FXMLLoader(getClass().getResource("boxAnswer.fxml"));
                    Parent root = Load.load();
                    Label answer = (Label) root.lookup("#correctanswer");
                    String answers ="The correct answer is: ";
                    List<Choices> choices = connector.getChoicesFromQuestion(question.getId());
                    int numChoices = 0;
                    for (Choices choice : choices) {
                        numChoices++;
                        if (numChoices == 1) {
                            ToggleButton text = (ToggleButton) element.lookup("#choice1");
                            if(text.isSelected()) scores += choice.getGrade();
                        }
                        if (numChoices == 2) {
                            ToggleButton text = (ToggleButton) element.lookup("#choice2");
                            if(text.isSelected()) scores += choice.getGrade();
                        }
                        if (numChoices == 3) {
                            ToggleButton text = (ToggleButton) element.lookup("#choice3");
                            if(text.isSelected()) scores += choice.getGrade();
                        }
                        if (numChoices == 4) {
                            ToggleButton text = (ToggleButton) element.lookup("#choice4");
                            if(text.isSelected()) scores += choice.getGrade();
                        }
                        if (numChoices == 5) {
                            ToggleButton text = (ToggleButton) element.lookup("#choice5");
                            if(text.isSelected()) scores += choice.getGrade();
                        }
                        if (choice.getGrade()!=0.00) answers = answers + choice.getText() + "\n";
                    }
                    grade.setText("" + scores + "/" + container.getChildren().size()/2 + ".00");
                    answer.setText(answers);
                    container.getChildren().add(cnt,root);
                }
                scrollPane.setContent(container);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
