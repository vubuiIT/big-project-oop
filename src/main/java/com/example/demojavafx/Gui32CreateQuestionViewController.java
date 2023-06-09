package com.example.demojavafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;


public class Gui32CreateQuestionViewController implements Initializable {
    private Stage stage;
    private boolean isCreateQuestion;
    private int idQuesCreate;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    void expand_more_choices() {
        double rowWidth;
        if (this.expand_more_choice) {
            rowWidth = 38; // Width in pixels
        } else {
            rowWidth = 0;
        }
        for (int i = 0; i < 3; i++) {

            // Create a RowConstraints object for the specified row
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(rowWidth);

            // Apply the RowConstraints to the desired row
            gridPane.getRowConstraints().set(i * 2 + baseLine, rowConstraints);
            gridPane.getRowConstraints().set(i * 2 + baseLine + 1, rowConstraints);
            for (javafx.scene.Node node : gridPane.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(node);
                if (rowIndex != null && rowIndex == i * 2 + baseLine) {
                    node.setVisible(this.expand_more_choice);
                }
            }
            for (javafx.scene.Node node : gridPane.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(node);
                if (rowIndex != null && rowIndex == i * 2 + baseLine + 1) {
                    node.setVisible(this.expand_more_choice);
                }
            }
        }
        this.expand_more_choice = !this.expand_more_choice;
        if (this.expand_more_choice) {
            addchoicebtn.setText("Blanks for more 3 choices");
        }
        else {
            addchoicebtn.setText("Collapse");
        }
    }
    private boolean expand_more_choice = false;
    private boolean firstTimeSaveChanges = false;
    int baseLine = 9;
    private File selectedFile;
    @FXML
    private TextField choice1entry;
    @FXML
    private TextField choice2entry;

    @FXML
    private TextField choice3entry;

    @FXML
    private TextField choice4entry;

    @FXML
    private TextField choice5entry;
    @FXML
    private TextField markField;

    @FXML
    private TextField questioNameField;

    @FXML
    private Text addedit;

    @FXML
    private TextField questionText;

    @FXML
    private Button addchoicebtn;

    @FXML
    private GridPane gridPane;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ChoiceBox<String> grade1;

    @FXML
    private ChoiceBox<String> grade2;

    @FXML
    private ChoiceBox<String> grade3;

    @FXML
    private ChoiceBox<String> grade4;

    @FXML
    private ChoiceBox<String> grade5;
    @FXML
    private TreeView<Category> categoryTreeView;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    @FXML
    private Pane missingCategories;

    @FXML
    private Pane missingChoices;

    @FXML
    private Pane missingMark;

    @FXML
    private Pane missingName;

    @FXML
    private Pane missingText;

    @FXML
    private Text warningText;

    @FXML
    private Text fileMediaQues;
    @FXML
    void chooseQuesMedia(MouseEvent event) {
        // Create a FileChooser object
        FileChooser fileChooser = new FileChooser();

        // Set an initial directory (optional)
        File initialDirectory = new File("C:\\Users\\Vu Bui\\Pictures");
        fileChooser.setInitialDirectory(initialDirectory);

        // Show the open file dialog
        selectedFile = fileChooser.showOpenDialog(this.stage);

        if (selectedFile != null) {
            // Process the selected file
            // You can read the file using selectedFile.getAbsolutePath()
            fileMediaQues.setText(selectedFile.getName());
        } else {
            System.out.println("No file selected.");
        }
    }
    @FXML
    boolean checkValidAddQuestion() {
        boolean valid = true;
        if (treeView.getIdChoice() == -1) {
            valid = false;
            missingCategories.setVisible(true);
        }
        else missingCategories.setVisible(false);
        String qText = questionText.getText();
        if (qText.isEmpty()) {
            valid = false;
            missingText.setVisible(true);
        }
        else {
            missingText.setVisible(false);
        }
        String qName = questioNameField.getText();
        if (qName.isEmpty()) {
            valid = false;
            missingName.setVisible(true);
        }
        else {
            missingName.setVisible(false);
        }
        String qMark = markField.getText();
        if (qMark.isEmpty()) {
            valid = false;
            missingMark.setVisible(true);
        }
        else missingMark.setVisible(false);
        // Kiểm tra default mark có phải float
        try {
            Float.parseFloat(qMark);
            missingMark.setVisible(false);
        } catch (NumberFormatException e) {
            valid = false;
            missingMark.setVisible(true);
        }
        // Kiểm tra có ít nhất 2/5 choice được add
        int countChoice = 5;
        String cText1 = choice1entry.getText();
        String cText2 = choice2entry.getText();
        String cText3 = choice3entry.getText();
        String cText4 = choice4entry.getText();
        String cText5 = choice5entry.getText();
        if (cText1.isEmpty()) {
            countChoice -= 1;
        }
        if (cText2.isEmpty()) {
            countChoice -= 1;
        }
        if (cText3.isEmpty()) {
            countChoice -= 1;
        }
        if (cText4.isEmpty()) {
            countChoice -= 1;
        }
        if (cText5.isEmpty()) {
            countChoice -= 1;
        }
        if (countChoice < 2) {
            missingChoices.setVisible(true);
            warningText.setVisible(true);
            valid = false;
        }
        else {
            missingChoices.setVisible(false);
            warningText.setVisible(false);
        }
        return valid;
    }
        float convertPercentage (String percentageString) {
            String valueString = percentageString.replace("%", "");

            // Parse the remaining string as a float
            float percentage = Float.parseFloat(valueString);

            // Convert the percentage to its decimal value

            return percentage / 100;
        }
    void createQuestion()  {
        int idCategory = treeView.getIdChoice();
        String qText = questionText.getText();
        String qName = questioNameField.getText();
        String qMark = markField.getText();
        float qMarkF = Float.parseFloat(qMark);
        DatabaseConnector connector = new DatabaseConnector();
        connector.connect();
        String mediaDirection = "";
        if (selectedFile != null)
            mediaDirection = selectedFile.getAbsolutePath();
        int idQuestion = connector.addQuestion(idCategory,qText,qName,mediaDirection,qMarkF);
        String cText1 = choice1entry.getText();
        String cText2 = choice2entry.getText();
        String cText3 = choice3entry.getText();
        String cText4 = choice4entry.getText();
        String cText5 = choice5entry.getText();
        String grade1Value = grade1.getValue();
        String grade2Value = grade2.getValue();
        String grade3Value = grade3.getValue();
        String grade4Value = grade4.getValue();
        String grade5Value = grade5.getValue();
        for (int i = 1; i <= 5; i++) {
            String cText = null;
            String gradeValue = null;
            switch (i) {
                case 1 -> {
                    cText = cText1;
                    gradeValue = grade1Value;
                }
                case 2 -> {
                    cText = cText2;
                    gradeValue = grade2Value;
                }
                case 3 -> {
                    cText = cText3;
                    gradeValue = grade3Value;
                }
                case 4 -> {
                    cText = cText4;
                    gradeValue = grade4Value;
                }
                case 5 -> {
                    cText = cText5;
                    gradeValue = grade5Value;
                }
            }
            System.out.print(cText + ' ' + gradeValue + '\n');
            if (!cText.isEmpty()) {
                float grade = 0;
                if (Objects.equals(gradeValue, "None"))
                    grade = 0;
                else
                    grade = convertPercentage(gradeValue);
                connector.addChoice(idQuestion, grade, "", cText);
            }
        }
        isCreateQuestion = true;
        idQuesCreate = idQuestion;
        connector.disconnect();
    }
    void modifiedQuestion() {
        int idCategory = treeView.getIdChoice();
        String qText = questionText.getText();
        String qName = questioNameField.getText();
        String qMark = markField.getText();
        float qMarkF = Float.parseFloat(qMark);
        DatabaseConnector connector = new DatabaseConnector();
        connector.connect();
        int idQuestion = connector.addQuestion(idCategory,qText,qName,"",qMarkF);
        String cText1 = choice1entry.getText();
        String cText2 = choice2entry.getText();
        String cText3 = choice3entry.getText();
        String cText4 = choice4entry.getText();
        String cText5 = choice5entry.getText();
        String grade1Value = grade1.getValue();
        String grade2Value = grade2.getValue();
        String grade3Value = grade3.getValue();
        String grade4Value = grade4.getValue();
        String grade5Value = grade5.getValue();
        for (int i = 1; i <= 5; i++) {
            String cText = null;
            String gradeValue = null;
            switch (i) {
                case 1 -> {
                    cText = cText1;
                    gradeValue = grade1Value;
                }
                case 2 -> {
                    cText = cText2;
                    gradeValue = grade2Value;
                }
                case 3 -> {
                    cText = cText3;
                    gradeValue = grade3Value;
                }
                case 4 -> {
                    cText = cText4;
                    gradeValue = grade4Value;
                }
                case 5 -> {
                    cText = cText5;
                    gradeValue = grade5Value;
                }
            }
            System.out.print(cText + ' ' + gradeValue + '\n');
            if (!cText.isEmpty()) {
                float grade = 0;
                if (Objects.equals(gradeValue, "None"))
                    grade = 0;
                else
                    grade = convertPercentage(gradeValue);
                connector.addChoice(idQuestion, grade, "", cText);
            }
        }
        isCreateQuestion = true;
        connector.disconnect();
    }
    @FXML
    void cancel(MouseEvent event) {
        closeStage();
    }
    @FXML
    void handleCategoryTreeview(MouseEvent event) {
        categoryTreeView.setVisible(!categoryTreeView.isVisible());
    }
    @FXML
    void saveChange(MouseEvent event) {
        boolean validAddQuestion = checkValidAddQuestion();
        if (validAddQuestion) {
            if (isCreateQuestion)
                modifiedQuestion();
            else
                createQuestion();
            closeStage();
        }
        else {
            System.out.print("Not valid");
        }
    }
    private void closeStage() {
        stage.close(); // Đóng Stage từ Controller
    }
    @FXML
    void saveContinueEdit(MouseEvent event) {
        boolean validAddQuestion = checkValidAddQuestion();
        if (validAddQuestion) {
            if (isCreateQuestion)
                modifiedQuestion();
            else
                createQuestion();
        }
        else {
            System.out.print("Not valid");
        }
    }
    @FXML
    void addchoice(MouseEvent event) {
        // Thêm logic xử lý khi nhấp vào nút "addChoice" ở đây
        expand_more_choices();
    }
    private TreeViewCategory treeView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileMediaQues.setText("");
        treeView = new TreeViewCategory(categoryTreeView,categoryChoiceBox);
        treeView.start();
//        missingCategories.setVisible(false);
//        missingChoices.setVisible(false);
//        missingMark.setVisible(false);
//        missingName.setVisible(false);
//        missingText.setVisible(false);
//        warningText.setVisible(false);
        List<String> gradeList = new ArrayList<>(Arrays.asList("None", "100%", "90%", "83.33333%", "80%", "75%", "70%", "66.66667%", "60%", "50%", "40%", "33.3333%", "30%", "25%", "20%", "16.66667%", "14.28571%", "12.5%", "11.11111%", "10%", "5%","-5%", "-10%", "-11.11111%", "-12.5%", "-14.28571%", "-16.66667%", "-20%", "-25%", "-30%", "-33.3333%", "-40%", "-50%", "-60%", "-66.66667%", "-70%", "-75%", "-80%", "-83.33333%"));
        grade1.getItems().addAll(gradeList);
        grade1.setValue("None");
        grade2.getItems().addAll(gradeList);
        grade2.setValue("None");
        grade3.getItems().addAll(gradeList);
        grade3.setValue("None");
        grade4.getItems().addAll(gradeList);
        grade4.setValue("None");
        grade5.getItems().addAll(gradeList);
        grade5.setValue("None");
        categoryTreeView.setVisible(false);
        scrollPane.setContent(gridPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        expand_more_choices();
    }
    @FXML
    public void run(Question question){
        questionText.setText(question.getText());
        questioNameField.setText(question.getName());
        markField.setText(Float.toString(question.getMark()));
        addedit.setText("Editting Multiple choice question");
        DatabaseConnector connector = new DatabaseConnector();
        connector.connect();
        categoryChoiceBox.setValue(connector.getCategory(question.getCategoryId()).getName());
        List<Choices> choices = connector.getChoicesFromQuestion(question.getId());
        int numChoices=0;
        for(Choices choice: choices){
            numChoices++;
            if(numChoices==1) {
                choice1entry.setText(choice.getText());
                grade1.setValue("100%");
            }
            if(numChoices==2) {
                choice2entry.setText(choice.getText());
                grade2.setValue("100%");
            }
            if(numChoices==3) {
                choice3entry.setText(choice.getText());
                grade3.setValue("100%");
            }
            if(numChoices==4) {
                choice4entry.setText(choice.getText());
                grade4.setValue("100%");
            }
            if(numChoices==5) {
                choice5entry.setText(choice.getText());
                grade5.setValue("100%");
            }
            if(numChoices>2) {
                expand_more_choices();
            }
        }

    }
}
