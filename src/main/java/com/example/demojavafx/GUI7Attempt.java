package com.example.demojavafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    @FXML
    private Label additionalLabel;
    public Quiz quiz;
    @FXML
    private Label timecountdown;
    @FXML
    private Button yes;
    @FXML
    private Button no;
    @FXML
    private VBox again;
    private Timeline countdownTimer;
    private int timeLimitInSeconds;
    private int timeLimit = 0;
    private boolean isSub=false;
    private boolean isPlay =false;
    public void setquiz(Quiz quiz) {
        this.quiz = quiz;
        //System.out.println(this.quiz.getId());
        if (quiz.getEnableTimeLimit() == 1) {
            timeLimitInSeconds = quiz.getTimeLimit()*60;
        }
        countdownTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), this::updateCountdown)
        );
        countdownTimer.setCycleCount(Timeline.INDEFINITE);
        countdownTimer.play();
        String timeBegin = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy, h:mm a", Locale.ENGLISH));
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
                vbox.setOnMouseClicked(event -> {
                    scrollToQuestion(scrollPane, indexQues);
                });
                flowPane.getChildren().add(vbox);
                List<Choices> choices = connector.getChoicesFromQuestion(question.getId());
                boolean selected;
                selected = quiz.getShuffle() == 1;
                if (selected) {
                    Collections.shuffle(choices);
                }
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
                FXMLLoader Loaderr = new FXMLLoader(getClass().getResource("boxQuestion.fxml"));
                Parent root;
                if(check) root = Loaderr.load();
                else root = Loader.load();
                Label num = (Label) root.lookup("#num");
                num.setText("" + (container.getChildren().size() + 1));
                Label name = (Label) root.lookup("#questionName");
                name.setText(question.getName() + " : " + question.getText());
                String fileExtension = getFileExtension(question.getMediaName());
                //System.out.println(question.getMediaName());
                if(question.getMediaName()!="")
                {
                    if (fileExtension.equalsIgnoreCase("mp4")) {
                        // Xử lý nếu file là video
                        Media media = byteArrayToMedia(question.getMedia(),question.getMediaName());
                        if(media != null) {
                            // Set the Media object to the MediaPlayer
                            MediaView mediaView = (MediaView) root.lookup("#mediaViewVideo");
                            //ByteArrayInputStream inputStream = new ByteArrayInputStream(question.getMedia());
                            //System.out.println(question.getMediaName());

                            // Tạo MediaPlayer từ Media
                            MediaPlayer mediaPlayer = new MediaPlayer(media);

                            // Đặt MediaPlayer cho MediaView
                            mediaView.setMediaPlayer(mediaPlayer);
                            mediaView.setOnMouseClicked(event -> {
                                if (isPlay) {
                                    mediaView.getMediaPlayer().pause();
                                    isPlay = false;
                                    mediaPlayer.setMute(true);
                                }
                                else {
                                    mediaView.getMediaPlayer().play();
                                    isPlay = true;
                                    mediaPlayer.setMute(false);
                                }
                            });
                        }
                    } else if (fileExtension.equalsIgnoreCase("gif")) {
                        ImageView image = (ImageView) root.lookup("#imagetext");
                        Image gifImage = new Image(new ByteArrayInputStream(question.getMedia()));
                        image.setImage(gifImage);
                    }
                    else if (!Objects.equals(question.getMediaName(), "")){
                        // Xử lý nếu file không phải là video
                        ImageView images = (ImageView) root.lookup("#imagetext");
                        Image image = byteArrayToImage(question.getMedia());
                        images.setImage(image);
                    }
                }
                if(check) {
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
                }
                else {
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
                    int numChoices = 0;
                    for (Choices choice : choices) {
                        numChoices++;
                        if (numChoices == 1) {
                            CheckBox text = (CheckBox) root.lookup("#choice1");
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
                            CheckBox text = (CheckBox) root.lookup("#choice2");
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
                            CheckBox text = (CheckBox) root.lookup("#choice3");
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
                            CheckBox text = (CheckBox) root.lookup("#choice4");
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
                            CheckBox text = (CheckBox) root.lookup("#choice5");
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
                }

                container.getChildren().add(root);
            }
            scrollPane.setContent(container);
        }catch (Exception e) {
            e.printStackTrace();
        }
        additionalLabel.setOnMouseClicked(even -> {
            if(!kt) {stage.close();return;}
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to submit your attempt?");

            // Tùy chỉnh các nút trong hộp thoại
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            // Hiển thị hộp thoại và chờ đến khi người dùng đóng nó
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            // Xử lý kết quả từ hộp thoại
            if (result == buttonTypeYes) {
                additionalLabel.setText(" Finish review ");kt=false;
                scrollToQuestion(scrollPane,1);
                try {
                    isSub=true;
                    timecountdown.setVisible(false);
                    FXMLLoader Loader = new FXMLLoader(getClass().getResource("preview.fxml"));
                    Parent rot = Loader.load();
                    Text begin = (Text) rot.lookup("#begin");
                    begin.setText(""+ timeBegin);
                    Text end = (Text) rot.lookup("#end");
                    String timeText = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy, h:mm a", Locale.ENGLISH));
                    end.setText("" + timeText);
                    Text mark = (Text) rot.lookup("#mark");
                    Text grade = (Text) rot.lookup("#grade");
                    Text time = (Text) rot.lookup("#time");
                    int hours = timeLimit / 3600; // Giờ
                    int minutes = (timeLimit % 3600) / 60; // Phút
                    int seconds = timeLimit % 60; // Giây
                    if(hours!=0) time.setText(String.format("%02d hours %02d mins %02d seconds", hours, minutes, seconds));
                    else if(minutes!=0) time.setText(String.format("%02d mins %02d seconds", minutes, seconds));
                    else time.setText(String.format("%02d seconds", seconds));
                    container.getChildren().add(0,rot);
                    List<Question> questions = connector.getQuestionsFromQuiz(quiz.getId());
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
                        float preScores = scores;
                        boolean click=false;
                        for (Choices choice : choices) {
                            numChoices++;
                            if (numChoices == 1) {
                                Node text = (Node) element.lookup("#choice1");
                                if (text instanceof CheckBox) {
                                    CheckBox choicet = (CheckBox) text;
                                    if(choicet.isSelected()) {
                                        scores += choice.getGrade();click=true;
                                        if(choice.getGrade()==0.00) scores = preScores;
                                    }
                                    // Sử dụng CheckBox
                                } else if (text instanceof RadioButton) {
                                    RadioButton choicet = (RadioButton) text;
                                    if(choicet.isSelected()) {scores += choice.getGrade();click=true;}
                                    // Sử dụng RadioButton
                                }
                            }
                            if (numChoices == 2) {
                                Node text = (Node) element.lookup("#choice2");
                                if (text instanceof CheckBox) {
                                    CheckBox choicet = (CheckBox) text;
                                    if(choicet.isSelected()) {
                                        scores += choice.getGrade();click=true;
                                        if(choice.getGrade()==0.00) scores = preScores;
                                    }
                                    // Sử dụng CheckBox
                                } else if (text instanceof RadioButton) {
                                    RadioButton choicet = (RadioButton) text;
                                    if(choicet.isSelected()) {scores += choice.getGrade();click=true;}
                                    // Sử dụng RadioButton
                                }
                            }
                            if (numChoices == 3) {
                                Node text = (Node) element.lookup("#choice3");
                                if (text instanceof CheckBox) {
                                    CheckBox choicet = (CheckBox) text;
                                    if(choicet.isSelected()) {
                                        scores += choice.getGrade();click=true;
                                        if(choice.getGrade()==0.00) scores = preScores;
                                    }
                                    // Sử dụng CheckBox
                                } else if (text instanceof RadioButton) {
                                    RadioButton choicet = (RadioButton) text;
                                    if(choicet.isSelected()) {scores += choice.getGrade();click=true;}
                                    // Sử dụng RadioButton
                                }
                            }
                            if (numChoices == 4) {
                                Node text = (Node) element.lookup("#choice4");
                                if (text instanceof CheckBox) {
                                    CheckBox choicet = (CheckBox) text;
                                    if(choicet.isSelected()) {
                                        scores += choice.getGrade();click=true;
                                        if(choice.getGrade()==0.00) scores = preScores;
                                    }
                                    // Sử dụng CheckBox
                                } else if (text instanceof RadioButton) {
                                    RadioButton choicet = (RadioButton) text;
                                    if(choicet.isSelected()) {scores += choice.getGrade();click=true;}
                                    // Sử dụng RadioButton
                                }
                            }
                            if (numChoices == 5) {
                                Node text = (Node) element.lookup("#choice5");
                                if (text instanceof CheckBox) {
                                    CheckBox choicet = (CheckBox) text;
                                    if(choicet.isSelected()) {
                                        scores += choice.getGrade();click=true;
                                        if(choice.getGrade()==0.00) scores = preScores;
                                    }
                                    // Sử dụng CheckBox
                                } else if (text instanceof RadioButton) {
                                    RadioButton choicet = (RadioButton) text;
                                    if(choicet.isSelected()) {scores += choice.getGrade();click=true;}
                                    // Sử dụng RadioButton
                                }
                            }
                            if (choice.getGrade()!=0.00) answers = answers + choice.getText() + "\n";
                        }
                        Node vbox = flowPane.getChildren().get(cnt/2 - 1);
                        vbox.setStyle("-fx-background-color: #000000");
                        if(scores-preScores==1.00) {
                            vbox.setStyle("-fx-background-color: #00fc15");
                        }
                        else if(scores-preScores!=0.00) {
                            vbox.setStyle("-fx-background-color: #ff9200");
                        }
                        else if(click) vbox.setStyle("-fx-background-color: #ff0000");
                        answer.setText(answers);
                        container.getChildren().add(cnt,root);
                    }
                    mark.setText("" + scores + "/" + cnt/2 + ".00");
                    float ten = 20*scores/cnt;
                    int percent = (int) ((int) 10*ten);
                    grade.setText("" + ten + " out of 10.00 (" + percent + "%)");
                    scrollPane.setContent(container);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("User chose No or closed the dialog.");
                alert.close();
            }
        });
    }
    private String generateRandomFileName() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder randomName = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(alphabet.length());
            randomName.append(alphabet.charAt(randomIndex));
        }

        return randomName.toString();
    }
    private Image byteArrayToImage(byte[] fileData) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Media byteArrayToMedia(byte[] fileData, String fileName) {
        try {
            String fileExt = getFileExtension(fileName);
            // Tạo một tệp tin ẩn với tên ngẫu nhiên
            String tempFileName = generateRandomFileName();
            String tempFilePath = System.getProperty("java.io.tmpdir") + tempFileName + "." + fileExt;

            // Ghi mảng byte vào tệp tin tạm thời
            Files.write(Paths.get(tempFilePath), fileData);

            // Tạo URI từ đường dẫn tệp tin tạm thời
            URI uri = new File(tempFilePath).toURI();

            // Tạo đối tượng Media từ URI
            Media media = new Media(uri.toString());

            return media;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public String getFileExtension(String fileName) {

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
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
    private void updateCountdown(ActionEvent event) {
        timeLimit++;
        if (quiz.getEnableTimeLimit() == 1) {
            timeLimitInSeconds--; // Giảm thời gian giới hạn sau mỗi giây
            int hours = timeLimitInSeconds / 3600; // Giờ
            int minutes = (timeLimitInSeconds % 3600) / 60; // Phút
            int seconds = timeLimitInSeconds % 60; // Giây
            String timeLeft = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            timecountdown.setText("Time left: " + timeLeft);
            if (timeLimitInSeconds == 0) {
                // Thực hiện hành động khi hết thời gian
                countdownTimer.stop();
                additionalLabel.getOnMouseClicked().handle(null);
            }
        }
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //System.out.println(quiz.getId());

    }
    private void scrollToQuestion(ScrollPane scrollPane, int questionIndex) {
        if(isSub!=true){
            // Tìm hbox chứa câu hỏi tương ứng
            VBox vbox = (VBox) scrollPane.getContent();
            HBox questionHBox = (HBox) vbox.getChildren().get(questionIndex - 1);
            // Di chuyển scrollpane đến hbox chứa câu hỏi
            double scrollY = questionHBox.getBoundsInParent().getMinY();
            scrollPane.setVvalue( scrollY / (vbox.getHeight()-450));
        }
        else {
            // Tìm hbox chứa câu hỏi tương ứng
            VBox vbox = (VBox) scrollPane.getContent();
            HBox questionHBox = (HBox) vbox.getChildren().get(2*questionIndex - 1);
            // Di chuyển scrollpane đến hbox chứa câu hỏi
            double scrollY = questionHBox.getBoundsInParent().getMinY();
            scrollPane.setVvalue( scrollY / (vbox.getHeight()-500));
        }
    }
}