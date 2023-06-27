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
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import javafx.scene.control.TextFormatter;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import javax.swing.*;

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
    public void showCategory(MouseEvent mouseEvent) {
        tab2Popup.setVisible(true);
    }
    @FXML
    private TextField idCategoryTextfield;
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

    @FXML
    private Button choosefileButton;
    @FXML
    private Label chosenfilepath;
    public static File chosenFile; // file đã chọn
    @FXML
    void choosefileButtonActionPerformed(MouseEvent event) {
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Choose a file");
        chosenFile = filechooser.showOpenDialog(null);
        chosenfilepath.setText((chosenFile.getName())) ;
    }
    // Xử lí kéo thả file
    @FXML
    private Pane dragfilePane;
    @FXML
    void handleDragOver(DragEvent event) {
        if(event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }

    }
    @FXML
    void handleDrop(DragEvent event) throws FileNotFoundException {
        List<File> files = event.getDragboard().getFiles();
        for(File f : files) chosenFile = f;
        chosenfilepath.setText(chosenFile.getName());

    }
    // Xử lí khi import file
    @FXML
    private Button importButton;
    public static boolean CheckChoices(String s) {
        if(s.length() >= 4) {
            if(s.charAt(0) >= 'A' && s.charAt(0) <= 'Z'){
                if (s.charAt(1) =='.') {
                    if(s.charAt(2) == ' ') {
                        if(s.charAt(3) != ' ') return true;
                    }
                }
            }
        }
        return false;
    }
    public static boolean CheckAnswers(String s) {
        if(s.length() >= 9) {
            if(s.substring(0,8).equals("ANSWER: ")) {
                if(s.charAt(8) >= 'A' && s.charAt(8) <= 'Z') return true;
            }
        }
        return false;
    }
    @FXML
    void importButtonActionPerformed(MouseEvent event) {
        String path = chosenfilepath.getText();
        String extension = "";
        if (path.contains("."))
            extension = path.substring(path.lastIndexOf(".")+1);

        if(extension.equals("txt")) {
            List<Quiz> quizList = new ArrayList<Quiz>(); // Create list for quizzes
            try {
                Scanner fileScanner = new Scanner(chosenFile);
                int currentline = 0; // Used to know whick line is being read
                boolean fileOpenFlag = true; // Flag to check if file being read
                boolean errorFlag = false; // Flag to check if error found in file
                // Loop for each quiz
                while(fileOpenFlag) {
                    Quiz quiz = new Quiz();
                    List<Choice> choicesList = new ArrayList<Choice>();
                    // Read first line (expecting question)
                    // If there's a line
                    if(fileScanner.hasNextLine()) {
                        currentline++;
                        String s = fileScanner.nextLine();
                        // If the line is empty
                        if(s.isEmpty()) {
                            JOptionPane.showMessageDialog(null,"Error found at line" + currentline);
                            fileScanner.close();
                            errorFlag = true;
                            fileOpenFlag = false;
                            break;
                        }
                        else {
                            quiz.setQuestion(s);
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Error found at line" + currentline);
                        fileScanner.close();
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }

                    // Read 2nd line (expecting choices)
                    // If there's a line
                    if(fileScanner.hasNextLine()) {
                        currentline++;
                        String s = fileScanner.nextLine();
                        // If the line is empty
                        if(s.isEmpty()) {
                            JOptionPane.showMessageDialog(null,"Error found at line" + currentline);
                            fileScanner.close();
                            errorFlag = true;
                            fileOpenFlag = false;
                            break;
                        }
                        // If the line has choices format
                        else if(CheckChoices(s)) {
                            choicesList.add(new Choice(s));
                        }
                        else {
                            JOptionPane.showMessageDialog(null,"Error found at line" + currentline);
                            fileScanner.close();
                            errorFlag = true;
                            fileOpenFlag = false;
                            break;
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Error found at line" + currentline);
                        fileScanner.close();
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }

                    // Read 3rd line (expecting choices)
                    // If there's a line
                    if(fileScanner.hasNextLine()) {
                        currentline++;
                        String s = fileScanner.nextLine();
                        // If the line is empty
                        if(s.isEmpty()) {
                            JOptionPane.showMessageDialog(null,"Error found at line" + currentline);
                            fileScanner.close();
                            errorFlag = true;
                            fileOpenFlag = false;
                            break;
                        }
                        // If the line has choices format
                        else if(CheckChoices(s)) {
                            choicesList.add(new Choice(s));
                        }
                        else {
                            JOptionPane.showMessageDialog(null,"Error found at line" + currentline);
                            fileScanner.close();
                            errorFlag = true;
                            fileOpenFlag = false;
                            break;
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Error found at line" + currentline);
                        fileScanner.close();
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }

                    // Read remaining lines
                    // Loop till there's no line left to read
                    while(fileScanner.hasNextLine()) {
                        currentline++;
                        String s = fileScanner.nextLine();
                        // If line is empty
                        if(s.isEmpty()) {
                            JOptionPane.showMessageDialog(null,"Error found at line" + currentline);
                            fileScanner.close();
                            errorFlag = true;
                            fileOpenFlag = false;
                            break;
                        }
                        // If choice
                        else if(CheckChoices(s)) {
                            choicesList.add(new Choice(s));
                            continue;
                        }
                        // If answer
                        else if(CheckAnswers(s)){
                            char ans = s.charAt(8);
                            for(Choice ch:choicesList) {
                                if(ans == ch.getChoiceText().charAt(0)) {
                                    quiz.setAnswers(ch.getChoiceText().substring(3));
                                    quiz.setChoices(new Choice(ch.getChoiceText().substring(3),1));
                                }
                                else quiz.setChoices(new Choice(ch.getChoiceText().substring(3),0));
                            }
                            // If next line is empty, continue the loop to check new quiz
                            // If there's still line to read
                            if(fileScanner.hasNextLine()) {
                                currentline++;
                                String s1 = fileScanner.nextLine();
                                // If next line is empty
                                if(s1.isEmpty()) {
                                    break;
                                }
                                else {
                                    JOptionPane.showMessageDialog(null,"Error found at line" + currentline);
                                    fileScanner.close();
                                    errorFlag = true;
                                    fileOpenFlag = false;
                                    break;
                                }
                            }
                            else {
                                fileScanner.close();
                                fileOpenFlag = false;
                                break;
                            }

                        }
                        // If not choices or answers
                        else {
                            JOptionPane.showMessageDialog(null,"Error found at line" + currentline);
                            fileScanner.close();
                            fileOpenFlag = false;
                            errorFlag = true;
                            break;
                        }
                    }
                    if(errorFlag == false) quizList.add(quiz);
                }
                if(errorFlag == false) {
                    /* Add quizList to database */
                    JOptionPane.showMessageDialog(null,"Successfully import " + quizList.size() + " quiz!");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        else {
            JOptionPane.showMessageDialog(null,"Wrong Format");
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

        // Hiện question bank từ nút gear
        Menu.setOnMouseClicked(event -> {
            list1.setVisible(false);
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
            list1.setVisible(true);
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
