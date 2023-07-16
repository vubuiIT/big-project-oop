package com.example.demojavafx;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Gui6_3Controller implements Initializable {

    @FXML
    public Button addQues;
    @FXML
    private CheckBox alsoShowctg;

    @FXML
    private VBox showQuesvbox;

    @FXML
    private ChoiceBox<String> choiceboxCategory;

    @FXML
    private TreeView<Category> treeViewCategory;

    private Stage stage;
    @FXML
    void handleCategoryTreeview(MouseEvent event) {
        treeViewCategory.setVisible(!treeViewCategory.isVisible());
    }
    private BooleanProperty isCloseProperty = new SimpleBooleanProperty(false);
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public BooleanProperty isCloseProperty() {
        return isCloseProperty;
    }

    public void setIsClose(boolean value) {
        isCloseProperty.set(value);
    }


    public void clear63BoxFind() {
        showQuesvbox.getChildren().clear();
    }
    public List<HBox> getSelectedBoxes () {
        return selectedBoxes;
    }
    private TreeViewCategory treeView;
    public List<HBox> selectedBoxes = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        treeViewCategory.setVisible(false);

        // Data
        treeView = new TreeViewCategory(treeViewCategory, choiceboxCategory);
        treeView.start();

        treeViewCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            clear63BoxFind();
            int categoryId = treeView.getIdChoice();
            DatabaseConnector connector = new DatabaseConnector();

            // Connect to the database
            connector.connect();

            List<Question> questions = connector.getQuestionsFromCategory(categoryId);

            // Print the questions
            for (Question question : questions) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("63boxfind.fxml"));
                    HBox boxFind = loader.load();
                    // Truy cập vào label theo ID và thay đổi nội dung
                    Label nameLabel = (Label) boxFind.lookup("#name_lb");
                    Label textLabel = (Label) boxFind.lookup("#text_lbl");
                    Label quesId = (Label) boxFind.lookup("#id_lbl");
                    quesId.setUserData(question.getId());
                    nameLabel.setText(question.getName());
                    textLabel.setText(question.getText());
                    showQuesvbox.getChildren().add(boxFind);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Disconnect from the database after retrieving questions
            connector.disconnect();

            List<HBox> addedBoxes = new ArrayList<>();
            alsoShowctg.selectedProperty().addListener((observable1, oldValue1, newValue1) -> {
                if (newValue1) {
                    // Xử lý khi CheckBox được chọn
                    try {
                        connector.connect();
                        List<Category> allCategory = connector.getCategories(categoryId);

                        for (Category category : allCategory) {
                            List<Question> questionss = connector.getQuestionsFromCategory(category.getId());
                            for (Question question : questionss) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("63boxfind.fxml"));
                                HBox boxFind = loader.load();
                                // Truy cập vào label theo ID và thay đổi nội dung
                                Label nameLabel = (Label) boxFind.lookup("#name_lb");
                                Label textLabel = (Label) boxFind.lookup("#text_lbl");
                                Label quesId = (Label) boxFind.lookup("#id_lbl");
                                quesId.setUserData(question.getId());
                                nameLabel.setText(question.getName());
                                textLabel.setText(question.getText());
                                showQuesvbox.getChildren().add(boxFind);

                                addedBoxes.add(boxFind);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        connector.disconnect();
                    }
                } else {
                    showQuesvbox.getChildren().removeAll(addedBoxes);
                    addedBoxes.clear();
                }
            });
        });
        // thêm các ques được chọn khi nhấn add ques
        addQues.setOnAction(event -> {
            for (Node node : showQuesvbox.getChildren()) {
                if (node instanceof HBox) {
                    HBox hbox = (HBox) node;
                    CheckBox checkBox = (CheckBox) hbox.lookup("#question_cbx");
                        if (checkBox.isSelected()) {
                            selectedBoxes.add(hbox);
                        }
                }
            }
            stage.close();
            setIsClose(true);
        });

    }
}