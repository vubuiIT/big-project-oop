package com.example.demojavafx;

import com.almasb.fxgl.entity.Entity;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.css.converter.StringConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.control.TextFormatter;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

public class Gui1_2_3Controller implements Initializable {
    final boolean[] isPopupVisible = {false};

    @FXML
    private Button Menu;

    @FXML
    private Button MenuBack;

    @FXML
    private ChoiceBox default1;

    @FXML
    private TreeView defaultPopup1;

    @FXML
    private TabPane hoiPopup;

    @FXML
    private ListView<?> list1;

    @FXML
    private Hyperlink questions;

    @FXML
    private GridPane slider;

    @FXML
    private Hyperlink categories;

    @FXML
    private Hyperlink importt;

    @FXML
    private Hyperlink export;
    
    @FXML
    private Tab tab1;

    @FXML
    private Tab tab2;

    @FXML
    private Tab tab3;

    @FXML
    private Tab tab4;

    @FXML
    private ChoiceBox tab2Click;

    @FXML
    private TreeView tab2Popup;

    @FXML
    private TreeViewCategory treeView;

    @FXML
    private TextField tab2Textfield;

    @FXML
    private Button tab2Button;

    @FXML
    private ImageView tab2Imageview;
    @FXML
    private TextArea infoCategoryTextfield;
    final boolean[] isTab2Popup = {false};
    @FXML
    private VBox quizVbox;
    @FXML
    /// Khi bấm Create a New Question - 3.2
    public void createQues(javafx.scene.input.MouseEvent mouseEvent) {
        System.out.print("Test createQues");
        try {
            // Tạo một Stage mới
            Stage stage = new Stage();

            // Tải file FXML mới
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gui3.2-create-question-view.fxml"));
            Parent root = loader.load();

            Gui32CreateQuestionViewController controller = loader.getController();
            controller.setStage(stage);

            // Tạo một Scene từ Parent và đặt nó cho Stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Hiển thị cửa sổ mới
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void turnEdit(MouseEvent event) {
        try {
            // Tạo một Stage mới
            Stage stage = new Stage();

            // Tải file FXML mới
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI5.fxml"));
            Parent root = loader.load();

            GUI5Controller controller = loader.getController();
            controller.setStage(stage);

            // Tạo một Scene từ Parent và đặt nó cho Stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Hiển thị cửa sổ mới
            stage.show();
            controller.isCloseProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    updateQuizShow();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showCategory(MouseEvent mouseEvent) {
        tab2Popup.setVisible(true);
    }
    @FXML
    private TextField idCategoryTextfield;
    @FXML
    private ScrollPane paneListQuiz;
    @FXML
    public Text showErrorText;
    private boolean addCategory(Integer parentID, String nameCategory,String infoCategory,String idCategory) throws InterruptedException {
        // Create an instance of DatabaseConnector
        DatabaseConnector connector = new DatabaseConnector();

        // Connect to the database
        connector.connect();
        String statusMess;
        //System.out.print(parentID + "-" + nameCategory + "-" + infoCategory + "-" + idCategory + ".");
        if (Objects.equals(idCategory, "")) {
            statusMess = connector.addCategory(parentID, infoCategory, nameCategory);
        } else {
            int idNum = Integer.parseInt(idCategory);
            statusMess = connector.addNewCategoryWithId(idNum,parentID,infoCategory,nameCategory);
        }
        showErrorText.setText(statusMess);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            tab2Textfield.setText("");
            showErrorText.setText("There are required fields in this form marked");
        }));
        timeline.play();

        connector.disconnect();
        return false;
    }
    void updateQuizShow() {
        DatabaseConnector connector = new DatabaseConnector();
        connector.connect();
        List<Quiz> quizs = connector.getQuiz();
        quizVbox.getChildren().clear();
        for (Quiz quiz: quizs) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quizBar.fxml"));
            try {
                AnchorPane quizBar = loader.load();
                Hyperlink quizName = (Hyperlink) quizBar.lookup("#quizName");
                // Add event listener to the Hyperlink
                quizName.setOnAction(e -> openPreviewQuiz(quiz)); // Call openPreviewQuiz function with the current quiz
                quizName.setText(quiz.getName());
                quizVbox.getChildren().add(quizBar);
            } catch (IOException e) {
                System.out.print(e);
                throw new RuntimeException(e);
            }

        }
        connector.disconnect();



    }
    private void openPreviewQuiz(Quiz quiz) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gui6.12.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage previewStage = new Stage();
            previewStage.setTitle("Quiz Preview");

            Gui6_12Controller controller = loader.getController(); // Get the controller instance
            controller.setVariable(quiz); // Pass the variable to the controller
            // Set the loaded scene as the content for the new stage
            previewStage.setScene(new Scene(root));

            // Show the new stage
            previewStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any exceptions that occur during loading or showing the stage
        }
    }
    @Override
    // 1.1 + 1.2 + 3.3
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter());
        idCategoryTextfield.setTextFormatter(textFormatter);
        slider.setVisible(false);
        hoiPopup.setVisible(false);
        defaultPopup1.setVisible(false);
        updateQuizShow();
        // Hiện question bank từ nút gear
        Menu.setOnMouseClicked(event -> {
            paneListQuiz.setVisible(false);
            slider.setVisible(true);
            Menu.setVisible(false);
            MenuBack.setVisible(true);
        });

        // Tab questions
        // Hiện popup questions từ question bank
        questions.setOnMouseClicked(event -> {
            hoiPopup.getSelectionModel().select(tab1);
            hoiPopup.setVisible(true);
            slider.setVisible(false);
        });

        // Hiện popup default
        default1.setOnMousePressed(event -> {
            if (!isPopupVisible[0]) {
                defaultPopup1.setVisible(true);
                isPopupVisible[0] = true;
                treeView = new TreeViewCategory(defaultPopup1, default1);
                treeView.start();
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
            paneListQuiz.setVisible(true);
            slider.setVisible(false);
            hoiPopup.setVisible(false);
            Menu.setVisible(true);
            MenuBack.setVisible(false);
        });

        // Tab categories
        tab2Popup.setVisible(false);

        // Hiện popup categories từ question bank
        categories.setOnMouseClicked(event -> {
            hoiPopup.getSelectionModel().select(tab2);
            hoiPopup.setVisible(true);
            slider.setVisible(false);
            tab2Popup.setVisible(false);
        });

        // Hiện popup tree
        tab2Click.setOnMousePressed(event -> {
            if (!isTab2Popup[0]) {
                tab2Popup.setVisible(true);
                isTab2Popup[0] = true;
                treeView = new TreeViewCategory(tab2Popup, tab2Click);
                treeView.start();
            } else {
                tab2Popup.setVisible(false);
                isTab2Popup[0] = false;
            }
        });

        // Ẩn popup tree khi click bên ngoài
        tab2Popup.getParent().setOnMouseClicked(event -> {
            if (!tab2Popup.getBoundsInParent().contains(event.getX(), event.getY())) {
                tab2Popup.setVisible(false);
            }
        });

        // Kiểm tra textfield đã có nội dung chưa khi bấm nút Add category
        tab2Button.setOnMousePressed(event -> {
            if (!tab2Textfield.getText().isEmpty()) {
                tab2Imageview.setVisible(false);
                int parentID = treeView.getIdChoice();
                if (parentID < 0) {
                    this.showErrorText.setText("Please choose a category");
                    return;
                }
                String nameCategory = tab2Textfield.getText();
                String infoCategory = infoCategoryTextfield.getText();
                String idCategory = idCategoryTextfield.getText();
                try {
                    addCategory(parentID,nameCategory,infoCategory,idCategory);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                tab2Imageview.setVisible(true);
            }


        });

        // Tab import
        // Hiện popup importt từ question bank
        importt.setOnMouseClicked(event -> {
            hoiPopup.getSelectionModel().select(tab3);
            hoiPopup.setVisible(true);
            slider.setVisible(false);
        });

        // Tab export
        // Hiện popup export từ question bank
        export.setOnMouseClicked(event -> {
            hoiPopup.getSelectionModel().select(tab4);
            hoiPopup.setVisible(true);
            slider.setVisible(false);
        });
    }
}
