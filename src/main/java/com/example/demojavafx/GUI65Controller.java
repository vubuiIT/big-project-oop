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
import javafx.collections.FXCollections;
import java.util.Random;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GUI65Controller implements Initializable {
    @FXML
    private Button addQues;
    @FXML
    private CheckBox Include_ckb;
    @FXML
    private ChoiceBox<String> choicebox;
    @FXML
    private ComboBox<Integer> comboBox1;
    @FXML
    private TreeView<Category> treeViewCategory;
    @FXML
    private Pagination showQues;
    List<Integer> indexList = new ArrayList<>();
    private boolean comboBoxSelected = false;
    private Stage stage;

    @FXML
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private BooleanProperty isCloseProperty = new SimpleBooleanProperty(false);
    public BooleanProperty isCloseProperty() {
        return isCloseProperty;
    }
    public void setIsClose(boolean value) {
        isCloseProperty.set(value);
    }
    public List<HBox> getSelectedBoxes () {
        return selectedBoxes;
    }
    public List<HBox> selectedBoxes = new ArrayList<>();
    private TreeViewCategory treeView;
    private boolean isComboBoxSelected = false;

    @FXML
    void handleCategoryTreeview(MouseEvent event) {
        treeViewCategory.setVisible(!treeViewCategory.isVisible());
        showQues.setCurrentPageIndex(0);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        treeViewCategory.setVisible(false);
        treeView = new TreeViewCategory(treeViewCategory, choicebox);
        treeView.start();

        //chọn category
        treeViewCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            indexList.clear();
            selectedBoxes.clear();
            showQues.setPageFactory(null);
            comboBox1.setValue(null);
            int categoryId = treeView.getIdChoice();
            DatabaseConnector connector = new DatabaseConnector();

            // Connect to the database
            connector.connect();
            List<Question> questions = connector.getQuestionsFromCategory(categoryId);
            connector.disconnect();

            //chọn số câu random
            comboBoxSelected = true;
            for (int i = 0; i <= questions.size(); i++) {
                indexList.add(i);
            }
            comboBox1.setItems(FXCollections.observableArrayList(indexList));

            // in câu
            comboBox1.getSelectionModel().selectedItemProperty().addListener((observable1, oldValue1, newValue1) -> {
                int itemsPerPage = 8;
                selectedBoxes.clear();
                // show câu hỏi khi chọn xong số câu random
                if(comboBox1.getValue()!=null) {
                    List<Question> randomSentences = getRandomSentences(questions, comboBox1.getValue());
                    Integer pagenum;
                    if (comboBox1.getValue() % itemsPerPage == 0) pagenum = comboBox1.getValue() / itemsPerPage;
                    else pagenum = comboBox1.getValue() / itemsPerPage + 1;
                    showQues.setMaxPageIndicatorCount(pagenum);
                    showQues.setPageCount(pagenum);
                    showQues.setPageFactory(pageIndex -> {
                        VBox vboxshow = new VBox();
                        int startIndex = pageIndex * itemsPerPage;
                        int endIndex = Math.min(startIndex + itemsPerPage, randomSentences.size());
                        for (int i = startIndex; i < endIndex; i++) {
                            Question question = randomSentences.get(i);
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("65box.fxml"));
                                HBox boxFind = loader.load();

                                Label nameLabel = (Label) boxFind.lookup("#name_lb");
                                Label textLabel = (Label) boxFind.lookup("#text_lbl");
                                Label quesId = (Label) boxFind.lookup("#id_lbl");
                                quesId.setUserData(question.getId());
                                nameLabel.setText(question.getName());
                                textLabel.setText(question.getText());
                                selectedBoxes.add(boxFind);
                                vboxshow.getChildren().add(boxFind);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return vboxshow;
                    });
                    // thêm câu hỏi vào quiz
                    addQues.setOnMouseClicked(mouseEvent -> {
                        stage.close();
                        setIsClose(true);
                    });

                }
            });
        });
    }

    //hàm random
    public static List<Question> getRandomSentences(List<Question> sentences, int n) {
        List<Question> randomSentences = new ArrayList<>(sentences);

        List<Question> selectedSentences = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= n; i++) {
            int randomIndex = random.nextInt(randomSentences.size());
            Question randomSentence = randomSentences.get(randomIndex);
            selectedSentences.add(randomSentence);
            randomSentences.remove(randomIndex);
        }

        return selectedSentences;
    }
}