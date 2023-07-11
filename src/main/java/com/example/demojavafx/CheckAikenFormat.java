package com.example.demojavafx;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CheckAikenFormat {
    public static boolean CheckChoicesTxt(String s) {
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
    public static boolean CheckAnswersTxt(String s) {
        if(s.length() >= 9) {
            if(s.substring(0,8).equals("ANSWER: ")) {
                if(s.charAt(8) >= 'A' && s.charAt(8) <= 'Z') return true;
            }
        }
        return false;
    }
    public static void CheckTxt(File f) {
        List<QuizAiken> quizList = new ArrayList<QuizAiken>(); // Create list for quizzes
        try {
            Scanner fileScanner = new Scanner(f);
            int currentline = 0; // Used to know whick line is being read
            boolean fileOpenFlag = true; // Flag to check if file being read
            boolean errorFlag = false; // Flag to check if error found in file
            // Loop for each quiz
            while(fileOpenFlag) {
                QuizAiken quiz = new QuizAiken();
                List<ChoiceAiken> choicesList = new ArrayList<ChoiceAiken>();
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
                    else if(CheckChoicesTxt(s)) {
                        choicesList.add(new ChoiceAiken(s));
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
                    else if(CheckChoicesTxt(s)) {
                        choicesList.add(new ChoiceAiken(s));
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
                    else if(CheckChoicesTxt(s)) {
                        choicesList.add(new ChoiceAiken(s));
                        continue;
                    }
                    // If answer
                    else if(CheckAnswersTxt(s)){
                        char ans = s.charAt(8);
                        // Modify each choice in the choicesList: remove the header and set grade
                        for(ChoiceAiken ch:choicesList) {
                            if(ans == ch.getChoiceText().charAt(0)) {
                                quiz.setAnswers(ch.getChoiceText().substring(3));
                                quiz.setChoices(new ChoiceAiken(ch.getChoiceText().substring(3),1));
                            }
                            else quiz.setChoices(new ChoiceAiken(ch.getChoiceText().substring(3),0));
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

    public static List<BufferedImage> getImagefromPara(XWPFParagraph para) throws IOException {
        List<XWPFRun> runs = para.getRuns();
        List<XWPFPicture> pics = new ArrayList<>();
        // Set image for Quiz
        for(XWPFRun run : runs) {
            pics.addAll(run.getEmbeddedPictures());
        }
        List<BufferedImage> imageList = new ArrayList<>();
        for(XWPFPicture pic : pics) {
            byte[] data = pic.getPictureData().getData();
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
            imageList.add(img);
        }
        return imageList;
    }
    public static boolean CheckChoicesDocx(XWPFParagraph para) throws IOException {
        String s = para.getText();
        List<BufferedImage> imgs = getImagefromPara(para);
        if(s.length() >= 3) {
            if(s.charAt(0) >= 'A' && s.charAt(0) <= 'Z'){
                if (s.charAt(1) =='.') {
                    if(s.charAt(2) == ' ') {
                        if((s.length() >=4 && s.charAt(3) != ' ') || imgs.size() > 0) return true;
                    }
                }
            }
        }
        return false;
    }
    public static boolean CheckAnswersDocx(String s) {
        if(s.length() >= 9) {
            if(s.substring(0,8).equals("ANSWER: ")) {
                if(s.charAt(8) >= 'A' && s.charAt(8) <= 'Z') return true;
            }
        }
        return false;
    }
    public static void CheckDocx(File f) throws IOException {
        List<QuizAiken> quizList = new ArrayList<QuizAiken>(); // Create list for quizzes
        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(f));
            List<XWPFParagraph>paras = doc.getParagraphs();
            int paraIterator = 0; // Used to know which para is being read
            boolean fileOpenFlag = true; // Flag to check if file being read
            boolean errorFlag = false; // Flag to check if error found in file
            // Loop for each quiz
            while(fileOpenFlag) {
                QuizAiken quiz = new QuizAiken();
                List<ChoiceAiken> choicesList = new ArrayList<ChoiceAiken>();
                // Read first para (expecting question)
                // If there's still para to read
                if(paraIterator < paras.size()) {
                    String s = paras.get(paraIterator).getText();
                    List<BufferedImage> imgs = getImagefromPara(paras.get(paraIterator));
                    // If the paragraph is empty -> error
                    if(s.isEmpty() && imgs.size() == 0) {
                        JOptionPane.showMessageDialog(null, "Error found at line" + (paraIterator + 1));
                        doc.close();
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }
                    else {
                        quiz.setQuestion(s);
                        quiz.setIllustrator(imgs);
                        paraIterator++;
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"Error found at line" + (paraIterator+1));
                    doc.close();
                    errorFlag = true;
                    fileOpenFlag = false;
                    break;
                }

                // Read 2nd para (expecting choices)
                // If there's a para
                if(paraIterator < paras.size()) {
                    // If the para has choices format
                    if(CheckChoicesDocx(paras.get(paraIterator))) {
                        String s = paras.get(paraIterator).getText();
                        List<BufferedImage> imgs = getImagefromPara(paras.get(paraIterator));
                        choicesList.add(new ChoiceAiken(s,imgs));
                        paraIterator++;
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Error found at line" + (paraIterator+1));
                        doc.close();
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"Error found at line" + (paraIterator+1));
                    doc.close();
                    errorFlag = true;
                    fileOpenFlag = false;
                    break;
                }

                // Read 3rd para (expecting choices)
                // If there's still para to read
                if(paraIterator < paras.size()) {
                    // If the para has choices format
                    if(CheckChoicesDocx(paras.get(paraIterator))) {
                        String s = paras.get(paraIterator).getText();
                        List<BufferedImage> imgs = getImagefromPara(paras.get(paraIterator));
                        choicesList.add(new ChoiceAiken(s,imgs));
                        paraIterator++;
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Error found at line" + (paraIterator+1));
                        doc.close();
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"Error found at line" + (paraIterator+1));
                    doc.close();
                    errorFlag = true;
                    fileOpenFlag = false;
                    break;
                }

                // Read remaining paras
                // Loop till there's no para left to read
                while(paraIterator < paras.size()) {
                    String s = paras.get(paraIterator).getText();
                    // If para is empty
                    if(s.isEmpty()) {
                        JOptionPane.showMessageDialog(null,"Error found at line" + (paraIterator+1));
                        doc.close();
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }
                    // If choice
                    else if(CheckChoicesDocx(paras.get(paraIterator))) {
                        List<BufferedImage> imgs = getImagefromPara(paras.get(paraIterator));
                        choicesList.add(new ChoiceAiken(s,imgs));
                        paraIterator++;
                        continue;
                    }
                    // If answer
                    else if(CheckAnswersDocx(s)){
                        char ans = s.charAt(8);
                        // Modify each choice in the choicesList: remove the header and set grade
                        for(ChoiceAiken ch:choicesList) {
                            if(ans == ch.getChoiceText().charAt(0)) {
                                quiz.setAnswers(ch.getChoiceText().substring(3));
                                quiz.setChoices(new ChoiceAiken(ch.getChoiceText().substring(3),ch.getImage(),1));
                            }
                            else quiz.setChoices(new ChoiceAiken(ch.getChoiceText().substring(3),ch.getImage(),0));
                        }
                        paraIterator++;
                        // If next para is empty, continue the loop to check new quiz
                        // If there's still para to read
                        if(paraIterator < paras.size()) {
                            String s1 = paras.get(paraIterator).getText();
                            // If next para is empty
                            if(s1.isEmpty()) {
                                paraIterator++;
                                break;
                            }
                            else {
                                JOptionPane.showMessageDialog(null,"Error found at line" + (paraIterator+1));                              errorFlag = true;
                                doc.close();
                                errorFlag = true;
                                fileOpenFlag = false;
                                break;
                            }
                        }
                        else {
                            doc.close();
                            fileOpenFlag = false;
                            break;
                        }

                    }
                    // If not choices or answers
                    else {
                        JOptionPane.showMessageDialog(null,"Error found at line" + (paraIterator+1));
                        doc.close();
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
            else quizList.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}