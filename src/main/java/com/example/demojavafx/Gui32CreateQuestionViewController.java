package com.example.demojavafx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;


public class Gui32CreateQuestionViewController implements Initializable {
    private Question questionStage = null;
    private Stage stage;
    private int idQuesCreate = -1;
    boolean[] hasImgChoice = new boolean[5];
    File[] choiceImg = new File[5];
    List<byte[]> choiceImgDataList = new ArrayList<>();
    private BooleanProperty isCloseProperty = new SimpleBooleanProperty(false);

    public BooleanProperty isCloseProperty() {
        return isCloseProperty;
    }

    public void setIsClose(boolean value) {
        isCloseProperty.set(value);
    }

    @FXML
    public GridPane gridPane;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setIdQues(int id) {
        this.idQuesCreate = id;
    }

    void expand_more_choices() {
        double rowWidth;
        int amountHasPic = 0;
        if (hasImgChoice[2]) amountHasPic++;
        if (hasImgChoice[3]) amountHasPic++;
        if (hasImgChoice[4]) amountHasPic++;
        if (this.expand_more_choice) {

            rowWidth = 38; // Width in pixels
            double previousHeight = this.gridPane.getHeight();

            if (previousHeight != 0) {
                gridPane.setPrefSize(gridPane.getPrefWidth(), previousHeight + 38 * 3 + 300 * amountHasPic + 38 * (3 - amountHasPic));
            }
            System.out.println(previousHeight);
        } else {
            double previousHeight = this.gridPane.getHeight();
            System.out.println(previousHeight);
            rowWidth = 0;
            if (previousHeight != 0) {
                gridPane.setPrefSize(gridPane.getPrefWidth(), previousHeight - 38 * 3 - 300 * amountHasPic - 38 * (3 - amountHasPic));
            }
        }
        for (int i = 0; i < 3; i++) {
            // Create a RowConstraints object for the specified row
            RowConstraints rowConstraints = new RowConstraints();
            if (hasImgChoice[i + 2] && rowWidth != 0)
                rowConstraints.setPrefHeight(300);
            else {
                rowConstraints.setPrefHeight(rowWidth);
            }

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
        } else {
            addchoicebtn.setVisible(false);
        }
    }

    private boolean expand_more_choice = false;
    private boolean firstTimeSaveChanges = false;
    List<Choices> choiceDataMedia = new ArrayList<>();
    int baseLine = 9;
    private File selectedFile;
    @FXML
    private ImageView showQuesImg;
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
    private Label fileMediaQues;
    @FXML
    private MediaView showQuesVideo;
    @FXML
    private Button pickImgChoice1;

    @FXML
    private Button pickImgChoice2;

    @FXML
    private Button pickImgChoice3;

    @FXML
    private Button pickImgChoice4;

    @FXML
    private Button pickImgChoice5;

    public boolean isImageVideoOrGif(File file) {
        String extension = getFileExtension(file.getName());

        return extension.equalsIgnoreCase("jpg") ||
                extension.equalsIgnoreCase("jpeg") ||
                extension.equalsIgnoreCase("png") ||
                extension.equalsIgnoreCase("gif") ||
                extension.equalsIgnoreCase("mp4") ||
                extension.equalsIgnoreCase("mov") ||
                extension.equalsIgnoreCase("avi");
    }

    public String getFileExtension(String fileName) {

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    @FXML
    void chooseQuesMedia(MouseEvent event) throws IOException {
        // Create a FileChooser object
        FileChooser fileChooser = new FileChooser();

        // Set an initial directory (optional)
        String userHomeDir = System.getProperty("user.home");
        String picturesDir = userHomeDir + File.separator + "Pictures";
        File initialDirectory = new File(picturesDir);
        fileChooser.setInitialDirectory(initialDirectory);

        // Show the open file dialog
        selectedFile = fileChooser.showOpenDialog(this.stage);

        if (selectedFile != null) {
            // Process the selected file
            // You can read the file using selectedFile.getAbsolutePath()
            fileMediaQues.setText(selectedFile.getName());
            byte[] fileData = readFileData(selectedFile.getAbsolutePath());
            //Preview data
            // Kiểm tra định dạng của file dựa trên phần mở rộng (extension)
            String fileExtension = getFileExtension(selectedFile.getName());
            if (fileExtension.equalsIgnoreCase("mp4") || fileExtension.equalsIgnoreCase("mov") || fileExtension.equalsIgnoreCase("avi")) {
                // Xử lý nếu file là video
                Media media = byteArrayToMedia(fileData, selectedFile.getName());
                if (media != null) {
                    // Set the Media object to the MediaPlayer
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    showQuesVideo.setMediaPlayer(mediaPlayer);
                    showQuesVideo.setVisible(true);
                    mediaPlayer.setAutoPlay(true);
                    mediaPlayer.setMute(true);
                    showQuesImg.setVisible(false);
                }
            } else {
                // Xử lý nếu file không phải là video
                Image image = byteArrayToImage(fileData);
                showQuesImg.setImage(image);
                showQuesImg.setVisible(true);
                showQuesVideo.setVisible(false);
            }
            questionStage.setMedia(fileData);
            questionStage.setMediaName(selectedFile.getName());

        } else {
            System.out.println("No file selected.");
        }
    }

    @FXML
    boolean checkValidAddQuestion() {
        if (treeView.getIdChoice() == -1) {
            missingCategories.setVisible(true);
            return false;
        } else missingCategories.setVisible(false);
        String qText = questionText.getText();
        System.out.println("qText: " + qText);
        if (qText.isEmpty()) {
            missingText.setVisible(true);
            return false;
        } else {
            missingText.setVisible(false);
        }
        String qName = questioNameField.getText();
        if (qName.isEmpty()) {
            missingName.setVisible(true);
            return false;
        } else {
            missingName.setVisible(false);
        }
        String qMark = markField.getText();
        if (qMark.isEmpty()) {
            missingMark.setVisible(true);
            return false;
        } else missingMark.setVisible(false);
        // Kiểm tra default mark có phải float
        try {
            Float.parseFloat(qMark);
            missingMark.setVisible(false);
        } catch (NumberFormatException e) {
            missingMark.setVisible(true);
            return false;
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
            return false;
        } else {
            missingChoices.setVisible(false);
            warningText.setVisible(false);
        }
        if (selectedFile != null && !isImageVideoOrGif(selectedFile)) {
            missingChoices.setVisible(true);
            warningText.setText("Unsupported file format, only support (.jpg,.jpeg,.png,.gif,.mp4,.mov,.avi)");
            warningText.setVisible(true);
            return false;
        } else {
            missingChoices.setVisible(false);
            warningText.setVisible(false);
        }
        return true;
    }

    float convertPercentage(String percentageString) {
        String valueString = percentageString.replace("%", "");

        // Parse the remaining string as a float
        float percentage = Float.parseFloat(valueString);

        // Convert the percentage to its decimal value

        return percentage / 100;
    }

    private static byte[] readFileData(String filePath) throws IOException {
        Path path = Path.of(filePath);
        return Files.readAllBytes(path);
    }

    private boolean isImageFile(byte[] fileData) {
        // Kiểm tra nếu fileData là ảnh
        // Điều kiện kiểm tra phụ thuộc vào yêu cầu cụ thể của bạn
        return true;
    }

    private boolean isVideoFile(byte[] fileData) {
        // Kiểm tra nếu fileData là video
        // Điều kiện kiểm tra phụ thuộc vào yêu cầu cụ thể của bạn
        return true;
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

    void createQuestion() throws IOException {
        int idCategory = treeView.getIdChoice();
        String qText = questionText.getText();
        String qName = questioNameField.getText();
        String qMark = markField.getText();
        float qMarkF = Float.parseFloat(qMark);
        DatabaseConnector connector = new DatabaseConnector();
        connector.connect();
        String mediaName = "";
        if (!Objects.equals(questionStage.getMediaName(), "")) {
            mediaName = questionStage.getMediaName();
            byte[] fileData = questionStage.getMedia();
            if (idQuesCreate == -1) {
                idQuesCreate = connector.addQuestion(idCategory, qText, qName, fileData, mediaName, qMarkF);
            } else {
                connector.addQuesWithId(idQuesCreate, idCategory, qText, qName, fileData, mediaName, qMarkF);
            }
        } else {
            byte[] picData = new byte[0];
            if (idQuesCreate == -1) {
                idQuesCreate = connector.addQuestion(idCategory, qText, qName, picData, "", qMarkF);
            } else {
                connector.addQuesWithId(idQuesCreate, idCategory, qText, qName, picData, "", qMarkF);
            }
        }
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
                Choices file = choiceDataMedia.get(i - 1);
                String fileName = file.getPicName();
                if (!Objects.equals(fileName, "")) {
                    connector.addChoice(idQuesCreate, grade, file.getPic(), cText, fileName);
                } else {
                    byte[] picData = new byte[0];
                    connector.addChoice(idQuesCreate, grade, picData, cText, "");
                }
            }
        }
        connector.disconnect();
    }

    void modifiedQuestion() throws IOException {
        DatabaseConnector connector = new DatabaseConnector();
        connector.connect();
        //Delete old ques
        connector.deleteQues(idQuesCreate);
        List<Choices> choices = connector.getChoicesFromQuestion(idQuesCreate);
        for (Choices choice : choices) {
            connector.deleteChoice(choice.getId());
        }
        createQuestion();
        //Add new Ques
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
    void saveChange(MouseEvent event) throws IOException {
        boolean validAddQuestion = checkValidAddQuestion();
        if (validAddQuestion) {
            if (idQuesCreate != -1)
                modifiedQuestion();
            else
                createQuestion();
            closeStage();
            setIsClose(true);
        } else {
            System.out.print("Not valid");
        }
    }

    private void closeStage() {
        stage.close(); // Đóng Stage từ Controller
        setIsClose(true);
    }

    @FXML
    void saveContinueEdit(MouseEvent event) throws IOException {
        boolean validAddQuestion = checkValidAddQuestion();
        if (validAddQuestion) {
            if (idQuesCreate != -1)
                modifiedQuestion();
            else
                createQuestion();
        } else {
            System.out.print("Not valid");
        }
    }

    @FXML
    void addchoice(MouseEvent event) {
        // Thêm logic xử lý khi nhấp vào nút "addChoice" ở đây
        expand_more_choices();
    }

    private void chooseImgChoice(ActionEvent event, int stt) throws IOException {
        // Thực hiện các hành động khi Button được nhấp
        // Sử dụng biến additionalVariable ở đây

        FileChooser fileChooser = new FileChooser();

        // Set an initial directory (optional)
        String userHomeDir = System.getProperty("user.home");
        String picturesDir = userHomeDir + File.separator + "Pictures";
        File initialDirectory = new File(picturesDir);
        fileChooser.setInitialDirectory(initialDirectory);
        // Show the open file dialog
        File selectedFileChoice;
        selectedFileChoice = fileChooser.showOpenDialog(this.stage);

        if (selectedFileChoice != null) {
            double rowWidth = 300;
            int baseLine = 5;
            byte[] fileData = readFileData(selectedFileChoice.getAbsolutePath());
            double previousHeight = gridPane.getHeight();
            gridPane.setPrefSize(gridPane.getPrefWidth(), previousHeight + 300);
            // Kiểm tra định dạng của file dựa trên phần mở rộng (extension)
            String fileExtension = getFileExtension(selectedFileChoice.getName());
            if (fileExtension.equalsIgnoreCase("mp4")) {
                // Xử lý nếu file là video
                Media media = byteArrayToMedia(fileData, selectedFileChoice.getName());
                if (media != null) {
                    // Set the Media object to the MediaPlayer
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setAutoPlay(true);
                    mediaPlayer.setMute(true);
                }
            } else {
                // Xử lý nếu file không phải là video
                Image image = byteArrayToImage(fileData);
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setPrefHeight(rowWidth);
                int rowIndex = (stt - 1) * 2 + baseLine;
                gridPane.getRowConstraints().set(rowIndex, rowConstraints);

                // Khởi tạo ràng buộc cho các hàng còn lại

                ImageView imageView = new ImageView(image); // Đường dẫn tới tập tin ảnh
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                int columnIndex = 2; // Chỉ số cột (ở đây mình đặt là 0)
                gridPane.add(imageView, columnIndex, rowIndex);
                choiceImg[stt - 1] = selectedFileChoice;
                choiceDataMedia.get(stt - 1).setPicName(selectedFileChoice.getName());
                choiceDataMedia.get(stt - 1).setPicData(fileData);

            }
            hasImgChoice[stt - 1] = true;


        } else {
            System.out.println("No file selected.");
        }

    }

    private void setImgChoice(byte[] fileData, int stt, String picName) {
        double rowWidth = 300;
        int baseLine = 5;
        double previousHeight = gridPane.getHeight();
        gridPane.setPrefSize(gridPane.getPrefWidth(), previousHeight + 300 * stt);
        // Kiểm tra định dạng của file dựa trên phần mở rộng (extension)
        String fileExtension = getFileExtension(picName);
        if (fileExtension.equalsIgnoreCase("mp4")) {
            // Xử lý nếu file là video
            Media media = byteArrayToMedia(fileData, picName);
            if (media != null) {
                // Set the Media object to the MediaPlayer
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
                mediaPlayer.setMute(true);
            }
        } else {
            // Xử lý nếu file không phải là video
            Image image = byteArrayToImage(fileData);
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(rowWidth);
            int rowIndex = (stt - 1) * 2 + baseLine;
            gridPane.getRowConstraints().set(rowIndex, rowConstraints);

            // Khởi tạo ràng buộc cho các hàng còn lại

            ImageView imageView = new ImageView(image); // Đường dẫn tới tập tin ảnh
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            int columnIndex = 2; // Chỉ số cột (ở đây mình đặt là 0)
            gridPane.add(imageView, columnIndex, rowIndex);
        }
        choiceDataMedia.get(stt - 1).setPicName(picName);
        choiceDataMedia.get(stt - 1).setPicData(fileData);
        hasImgChoice[stt - 1] = true;
    }

    private TreeViewCategory treeView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileMediaQues.setText("");
        treeView = new TreeViewCategory(categoryTreeView, categoryChoiceBox);
        treeView.start();
        questionStage = new Question(-1,-1,"","",null,0,"");
        byte[] picData = new byte[0];
        for (int i = 1; i <= 5; i++) {
            choiceDataMedia.add(new Choices(0, 0, 0, picData, "", ""));
        }
//        missingCategories.setVisible(false);
//        missingChoices.setVisible(false);
//        missingMark.setVisible(false);
//        missingName.setVisible(false);
//        missingText.setVisible(false);
//        warningText.setVisible(false);
        Arrays.fill(hasImgChoice, false);
        Arrays.fill(choiceImg, null);
        List<String> gradeList = new ArrayList<>(Arrays.asList("None", "100%", "90%", "83.33333%", "80%", "75%", "70%", "66.66667%", "60%", "50%", "40%", "33.3333%", "30%", "25%", "20%", "16.66667%", "14.28571%", "12.5%", "11.11111%", "10%", "5%", "-5%", "-10%", "-11.11111%", "-12.5%", "-14.28571%", "-16.66667%", "-20%", "-25%", "-30%", "-33.3333%", "-40%", "-50%", "-60%", "-66.66667%", "-70%", "-75%", "-80%", "-83.33333%"));
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

        pickImgChoice1.setOnAction(event -> {
            try {
                chooseImgChoice(event, 1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        pickImgChoice2.setOnAction(event -> {
            try {
                chooseImgChoice(event, 2);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        pickImgChoice3.setOnAction(event -> {
            try {
                chooseImgChoice(event, 3);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        pickImgChoice4.setOnAction(event -> {
            try {
                chooseImgChoice(event, 4);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        pickImgChoice5.setOnAction(event -> {
            try {
                chooseImgChoice(event, 5);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        expand_more_choices();
    }

    @FXML
    public void run(Question question) {
        questionStage = question;
        questionText.setText(question.getText());
        questioNameField.setText(question.getName());
        markField.setText(Float.toString(question.getMark()));
        addedit.setText("Editing Multiple choice question");
        setIdQues(question.getId());
        this.stage.setOnShown(e -> {
            DatabaseConnector connector = new DatabaseConnector();
            connector.connect();
            if (!Objects.equals(question.getMediaName(), "")) {
                byte[] fileData = connector.getMediaData(question.getId());
                questionStage.setMedia(fileData);
                String mediaName = question.getName();
                fileMediaQues.setText(mediaName);
                String fileExtension = getFileExtension(mediaName);

                if (fileExtension.equalsIgnoreCase("mp4") || fileExtension.equalsIgnoreCase("mov") || fileExtension.equalsIgnoreCase("avi")) {
                    // Xử lý nếu file là video
                    Media media = byteArrayToMedia(fileData, mediaName);
                    if (media != null) {
                        // Set the Media object to the MediaPlayer
                        MediaPlayer mediaPlayer = new MediaPlayer(media);
                        showQuesVideo.setMediaPlayer(mediaPlayer);
                        showQuesVideo.setVisible(true);
                        mediaPlayer.setAutoPlay(true);
                        mediaPlayer.setMute(true);
                        showQuesImg.setVisible(false);
                    }
                } else {
                    // Xử lý nếu file không phải là video
                    Image image = byteArrayToImage(fileData);
                    showQuesImg.setImage(image);
                    showQuesImg.setVisible(true);
                    showQuesVideo.setVisible(false);
                }
            }
            treeView.setIdChoice(connector.getCategory(question.getCategoryId()).getId());
            categoryChoiceBox.setValue(connector.getCategory(question.getCategoryId()).getName());
            List<Choices> choices = connector.getChoicesFromQuestion(question.getId());
            int numChoices = 0;
            if (choices.size() > 2) {
                expand_more_choices();
            }
            for (Choices choice : choices) {
                numChoices++;
                if (numChoices == 1) {
                    choice1entry.setText(choice.getText());
                    String stringGrade = Float.toString(choice.getGrade() * 100);
                    grade1.setValue(stringGrade + "%");
                }
                if (numChoices == 2) {
                    choice2entry.setText(choice.getText());
                    String stringGrade = Float.toString(choice.getGrade() * 100);
                    grade2.setValue(stringGrade + "%");
                }
                if (numChoices == 3) {
                    choice3entry.setText(choice.getText());
                    String stringGrade = Float.toString(choice.getGrade() * 100);
                    grade3.setValue(stringGrade + "%");
                }
                if (numChoices == 4) {
                    choice4entry.setText(choice.getText());
                    String stringGrade = Float.toString(choice.getGrade() * 100);
                    grade4.setValue(stringGrade + "%");
                }
                if (numChoices == 5) {
                    choice5entry.setText(choice.getText());
                    String stringGrade = Float.toString(choice.getGrade() * 100);
                    grade5.setValue(stringGrade + "%");
                }
                if (!Objects.equals(choice.getPicName(), "")) {
                    choiceDataMedia.get(numChoices - 1).setPicName(choice.getPicName());
                    choiceDataMedia.get(numChoices - 1).setPicData(choice.getPic());
                    setImgChoice(choice.getPic(),numChoices,choice.getPicName());
                }
            }
            connector.disconnect();

        });
    }
}

