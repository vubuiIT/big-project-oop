package com.example.demojavafx;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

import java.util.*;

import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.ImageView;
import java.beans.EventHandler;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;


import java.io.IOException;
import java.net.URL;

public class Gui6_12Controller implements Initializable {
    @FXML
    private CheckBox shuffleChoice;
    Integer number=1;
    @FXML
    private Button multiDelete;
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
    private Label nameQuiz;
    @FXML
    private Label nameQuiz2;
    @FXML
    private Label nameQuizAbove;
    @FXML
    private Button preview_btn;

    @FXML
    private Label timeLimit_lbl;

    @FXML
    private Label totalMark_lbl;

    @FXML
    private VBox vbox61;
    @FXML
    private GridPane attempt;
    @FXML
    private Button close;
    @FXML
    private Button cancel;
    @FXML
    private Button start;
    @FXML
    private Button start1;
    @FXML
    private VBox vbox62;
    Set<Integer> check = new HashSet<>();
    boolean isDelete=false;
    public Quiz quiz; // Declare a variable to hold the passed value
    Set<Integer> finalQues = new HashSet<>();
    int quizId;


    // ...

    public void setVariable(Quiz quiz) {
        this.quiz = quiz; // Set the passed value to the variable
        quizId = this.quiz.getId();
        boolean selected;
        selected = quiz.getShuffle() == 1;
        shuffleChoice.setSelected(selected);
        nameQuiz.setText(this.quiz.getName());
        nameQuiz2.setText(this.quiz.getName());
        nameQuizAbove.setText(this.quiz.getName());
        if (quiz.getEnableTimeLimit() == 1) {
            String getTimeLimit = String.valueOf(quiz.getTimeLimit());
            timeLimit_lbl.setText(getTimeLimit);
        }
        else {
            timeLimit_lbl.setText("Unlimited");
        }
        // hiện data câu hỏi đã có từ trước
        System.out.println("2e3qwrefe    " + quizId);
        DatabaseConnector connector = new DatabaseConnector();
        connector.connect();
        List<Question> tmp1 = connector.getQuestionsFromQuiz(quizId);
        connector.disconnect();
        for(Question ques : tmp1) {
            try {
                FXMLLoader loader1 = new FXMLLoader(getClass().getResource("64boxtick.fxml"));
                HBox boxtick = loader1.load();
                Label nameLabel = (Label) boxtick.lookup("#name_lb");
                Label textLabel = (Label) boxtick.lookup("#text_lbl");
                Label quesNumber = (Label) boxtick.lookup("#rank_lbl");
                Label quesId1 = (Label) boxtick.lookup("#quesID_lbl");
                nameLabel.setText(ques.getName());
                quesNumber.setText("" + number);
                totalMark_lbl.setText("" + number + ".00");
                numOfQues_lbl.setText("" + number);
                number++;
                textLabel.setText(ques.getText());
                quesId1.setUserData(ques.getId());
                listQues.getChildren().add(boxtick);
                check.add(ques.getId());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {
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
                controller.isCloseProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1) {
                        List<HBox> selectedBoxes1 = controller.getSelectedBoxes();
                        try {
                            updateQuestionList(selectedBoxes1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // mở start attempt
        attempt.setVisible(false);
        preview_btn.setOnAction(event ->{
            attempt.setVisible(true);
        });
        cancel.setOnAction(event ->{
            attempt.setVisible(false);
        });
        close.setOnAction(event ->{
            attempt.setVisible(false);
        });

        start.setOnAction(event ->{
            Stage currentStage = (Stage) start.getScene().getWindow();
            currentStage.close();
            try {
                Stage stage = new Stage();
                FXMLLoader loaders = new FXMLLoader(getClass().getResource("GUI7.fxml"));
                Parent root = loaders.load();

                GUI7Attempt controller = loaders.getController();
                controller.setStage(stage);
                controller.setquiz(this.quiz);

                Scene scene = new Scene(root);
                stage.setTitle("Attempt quiz");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        start1.setOnAction(event ->{
            Stage currentStage = (Stage) start1.getScene().getWindow();
            currentStage.close();
            try {
                Stage stage = new Stage();
                FXMLLoader loaders = new FXMLLoader(getClass().getResource("Export.fxml"));
                Parent root = loaders.load();

                ExportController controller = loaders.getController();
                controller.setStage(stage);
                controller.setquiz(this.quiz);

                Scene scene = new Scene(root);
                stage.setTitle("Preview quiz and export");
                stage.setScene(scene);
                stage.show();

                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)","*.pdf"));
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    PdfWriter pdfWriter = new PdfWriter(file);
                    PdfDocument pdfDocument = new PdfDocument(pdfWriter);

                    PageSize pageSize = PageSize.A4;
                    float pageHeight = pageSize.getHeight(); // 842
                    float pageWidth = pageSize.getWidth(); // 595

                    Document document = new Document(pdfDocument, pageSize);
                    document.setMargins(50, 30, 50, 70);

                    WritableImage snapImage = controller.snapBox.snapshot(new SnapshotParameters(), null);
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapImage,null);
                    int curr_y = 0;
                    while (curr_y < bufferedImage.getHeight()) {
                        // Crop ảnh thành những ảnh nhỏ tỉ lệ với 1 trang PDF
                        int h = (int) (pageHeight * (float) bufferedImage.getWidth() / pageWidth);
                        if (curr_y + h > bufferedImage.getHeight()) h = bufferedImage.getHeight() - curr_y;
                        BufferedImage subImage = bufferedImage.getSubimage(0, curr_y, bufferedImage.getWidth(), h);
                        ImageData imgdata = ImageDataFactory.create(subImage, null);
                        Image img = new Image(imgdata);
                        img.scaleToFit(pageWidth - 100, pageHeight - 100);
                        document.add(img);
                        curr_y += h;
                    }
                    document.close();

                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setTitle("Set password for exported file?");
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES,ButtonType.NO);
                    dialog.setContentText("Do you want to set password for the file?");
                    Optional<ButtonType> option = dialog.showAndWait();
                    if(option.get() == ButtonType.YES) {
                        TextInputDialog textInputDialog = new TextInputDialog();
                        textInputDialog.setTitle("Set password");
                        textInputDialog.setHeaderText("Set password for the PDF file: ");
                        textInputDialog.setContentText("Password: ");
                        Optional<String> password = textInputDialog.showAndWait();
                        if(password.isPresent()) SetPassword.PDF(file,password.get());
                    }
                }
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //thao tác delete
    @FXML
    void handleMultidel(MouseEvent event) {
        try {
            if (!isDelete) {
                List<HBox> boxChoice1 = new ArrayList<>();
                for (Node node : listQues.getChildren()) {
                    if (node instanceof HBox) {
                        HBox hboxx = (HBox) node;
                        boxChoice1.add(hboxx);
                    }
                }
                listQues.getChildren().clear();
                for (HBox hbox : boxChoice1) {
                    Label nameLabel = (Label) hbox.lookup("#name_lb");
                    Label textLabel = (Label) hbox.lookup("#text_lbl");
                    Label quesId = (Label) hbox.lookup("#quesID_lbl");
                    String name = nameLabel.getText();
                    String text = textLabel.getText();
                    FXMLLoader loader1 = new FXMLLoader(getClass().getResource("63boxfind.fxml"));
                    HBox boxtick = loader1.load();
                    Label nameLabel1 = (Label) boxtick.lookup("#name_lb");
                    Label textLabel1 = (Label) boxtick.lookup("#text_lbl");
                    Label quesId1 = (Label) boxtick.lookup("#id_lbl");
                    nameLabel1.setText(name);
                    textLabel1.setText(text);
                    quesId1.setUserData(quesId.getUserData());
                    listQues.getChildren().add(boxtick);
                }
                multiDelete.setText("DELETE");
                isDelete=true;
            } else {
                List<HBox> boxChoice = new ArrayList<>();
                for (Node node : listQues.getChildren()) {
                    if (node instanceof HBox) {
                        HBox hbox = (HBox) node;
                        CheckBox checkBox2 = (CheckBox) hbox.lookup("#question_cbx");
                        if (!checkBox2.isSelected()) {
                            boxChoice.add(hbox);
                        }
                    }
                }
                listQues.getChildren().clear();
                check.clear();
                number=1;
                finalQues.clear();
                updateQuestionList(boxChoice);
                multiDelete.setText("SELECT MULTIPLE ITEMS");
                isDelete=false;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    // reset thứ tự câu hỏi
    public void resetRank() throws IOException {
        Integer rank=0;
        for (Node node : listQues.getChildren()) {
            if (node instanceof HBox) {
                rank++;
                HBox hbox = (HBox) node;
                Label rank1 = (Label) hbox.lookup("#rank_lbl");
                rank1.setText(""+rank);
            }
        }
    }
    //update list câu hỏi
    public void updateQuestionList(List<HBox> selectedBoxes) throws IOException {
        for (HBox selectedBox : selectedBoxes) {
            // Lấy các thông tin từ ô được chọn
            Label nameLabel1 = (Label) selectedBox.lookup("#name_lb");
            Label textLabel1 = (Label) selectedBox.lookup("#text_lbl");
            Label quesId = (Label) selectedBox.lookup("#id_lbl");

            Integer Id= (int) quesId.getUserData();
            if(check.contains(Id)) {int t;}
            else {
                check.add(Id);
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
                quesNumber.setText("" + number);
                totalMark_lbl.setText("" + number + ".00");
                numOfQues_lbl.setText(""+number);
                number++;
                textLabel.setText(text);
                quesId1.setUserData(quesId.getUserData());
                listQues.getChildren().add(boxtick);
                finalQues.add(Id);
            }
        }
    }

    // An nut Save -> luu Quiz vao database
    @FXML
    void saveActionPerformed(MouseEvent event) throws IOException {
        DatabaseConnector connector = new DatabaseConnector();
        connector.connect();

        connector.clearQuiz(quizId);

        int shuffleChoiceInt = shuffleChoice.isSelected() ? 1 : 0;
        //connector.changeShuffleValue(quizId,shuffleChoiceInt);
        for (int tmpId : finalQues){
            connector.addQuesToQuiz(tmpId, quizId);
            System.out.println("Successfully added question " + tmpId);
        }
        System.out.println(""+quizId);
        List<Question> tmp = connector.getQuestionsFromQuiz(quizId);
        for (Question run : tmp)
            System.out.println("Got" + run.getId() + " " + run.getText());

        connector.disconnect();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}