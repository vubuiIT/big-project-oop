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

public class GUI65Controller implements Initializable {


    @FXML
    private CheckBox Include_ckb;
    @FXML
    private ChoiceBox<String> choicebox;

    @FXML
    private ComboBox<?> comboBox1;
    @FXML
    private TreeView<Category> treeViewCategory;
    @FXML
    private Pagination showQues;
    private Stage stage;
    @FXML
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private TreeViewCategory treeView;
    @FXML
    void handleCategoryTreeview(MouseEvent event) {
        treeViewCategory.setVisible(!treeViewCategory.isVisible());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        treeViewCategory.setVisible(false);

        // Data
        treeView = new TreeViewCategory(treeViewCategory, choicebox);
        treeView.start();

        treeViewCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showQues.setCurrentPageIndex(0);
            int categoryId = treeView.getIdChoice();
            DatabaseConnector connector = new DatabaseConnector();

            // Connect to the database
            connector.connect();
            int itemsPerPage = 10; // Số lượng câu hỏi trong mỗi trang

            List<Question> questions = connector.getQuestionsFromCategory(categoryId);
            Integer pagenum;
            if(questions.size() % itemsPerPage ==0) pagenum=questions.size()/itemsPerPage;
            else pagenum=questions.size()/itemsPerPage+1;
            showQues.setMaxPageIndicatorCount(pagenum);
            showQues.setPageCount(pagenum);
            showQues.setPageFactory(pageIndex -> {
                VBox vboxshow = new VBox();

                int startIndex = pageIndex * itemsPerPage;
                int endIndex = Math.min(startIndex + itemsPerPage, questions.size());
                for (int i = startIndex; i < endIndex; i++) {
                    Question question = questions.get(i);
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

                        vboxshow.getChildren().add(boxFind);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return vboxshow;
            });
            connector.disconnect();
        });
//        // thêm các ques được chọn khi nhấn add ques
//        addQues.setOnAction(event -> {
//            for (Node node : showQuesvbox.getChildren()) {
//                if (node instanceof HBox) {
//                    HBox hbox = (HBox) node;
//                    CheckBox checkBox = (CheckBox) hbox.lookup("#question_cbx");
//                    if (checkBox.isSelected()) {
//                        selectedBoxes.add(hbox);
//                    }
//                }
//            }
//            stage.close();
//            setIsClose(true);
//        });

    }
}