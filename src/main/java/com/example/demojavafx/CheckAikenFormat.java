package com.example.demojavafx;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        List<Quiz> quizList = new ArrayList<Quiz>(); // Create list for quizzes
        try {
            Scanner fileScanner = new Scanner(f);
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
                    else if(CheckChoicesTxt(s)) {
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
                    else if(CheckChoicesTxt(s)) {
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
                    else if(CheckChoicesTxt(s)) {
                        choicesList.add(new Choice(s));
                        continue;
                    }
                    // If answer
                    else if(CheckAnswersTxt(s)){
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

    public static boolean CheckChoicesDocx(String s) {
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
    public static boolean CheckAnswersDocx(String s) {
        if(s.length() >= 9) {
            if(s.substring(0,8).equals("ANSWER: ")) {
                if(s.charAt(8) >= 'A' && s.charAt(8) <= 'Z') return true;
            }
        }
        return false;
    }
    public static void CheckDocx(File f) throws FileNotFoundException, IOException {
        List<Quiz> quizList = new ArrayList<Quiz>(); // Create list for quizzes
        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(f));
            List<XWPFParagraph>paras = doc.getParagraphs();
            int currentpara = 0; // Used to know whick line is being read
            boolean fileOpenFlag = true; // Flag to check if file being read
            boolean errorFlag = false; // Flag to check if error found in file
            // Loop for each quiz
            while(fileOpenFlag) {
                Quiz quiz = new Quiz();
                List<Choice> choicesList = new ArrayList<Choice>();
                // Read first line (expecting question)
                // If there's a line
                if(currentpara < paras.size()) {
                    String s = paras.get(currentpara).getText();
                    // If the line is empty
                    if(s.isEmpty()) {
                        JOptionPane.showMessageDialog(null,"Error found at line" + (currentpara+1));
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }
                    else {
                        quiz.setQuestion(s);
                        currentpara++;
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"Error found at line" + (currentpara+1));
                    errorFlag = true;
                    fileOpenFlag = false;
                    break;
                }

                // Read 2nd line (expecting choices)
                // If there's a line
                if(currentpara < paras.size()) {
                    String s = paras.get(currentpara).getText();
                    // If the line is empty
                    if(s.isEmpty()) {
                        JOptionPane.showMessageDialog(null,"Error found at line" + (currentpara+1));
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }
                    // If the line has choices format
                    else if(CheckChoicesDocx(s)) {
                        choicesList.add(new Choice(s));
                        currentpara++;
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Error found at line" + (currentpara+1));
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"Error found at line" + (currentpara+1));
                    errorFlag = true;
                    fileOpenFlag = false;
                    break;
                }

                // Read 3rd line (expecting choices)
                // If there's a line
                if(currentpara < paras.size()) {
                    String s = paras.get(currentpara).getText();
                    // If the line is empty
                    if(s.isEmpty()) {
                        JOptionPane.showMessageDialog(null,"Error found at line" + (currentpara+1));
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }
                    // If the line has choices format
                    else if(CheckChoicesDocx(s)) {
                        choicesList.add(new Choice(s));
                        currentpara++;
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Error found at line" + (currentpara+1));
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"Error found at line" + (currentpara+1));
                    errorFlag = true;
                    fileOpenFlag = false;
                    break;
                }

                // Read remaining lines
                // Loop till there's no line left to read
                while(currentpara < paras.size()) {
                    String s = paras.get(currentpara).getText();
                    // If line is empty
                    if(s.isEmpty()) {
                        JOptionPane.showMessageDialog(null,"Error found at line" + (currentpara+1));
                        errorFlag = true;
                        fileOpenFlag = false;
                        break;
                    }
                    // If choice
                    else if(CheckChoicesDocx(s)) {
                        choicesList.add(new Choice(s));
                        currentpara++;
                        continue;
                    }
                    // If answer
                    else if(CheckAnswersDocx(s)){
                        char ans = s.charAt(8);
                        for(Choice ch:choicesList) {
                            if(ans == ch.getChoiceText().charAt(0)) {
                                quiz.setAnswers(ch.getChoiceText().substring(3));
                                quiz.setChoices(new Choice(ch.getChoiceText().substring(3),1));
                            }
                            else quiz.setChoices(new Choice(ch.getChoiceText().substring(3),0));
                        }
                        currentpara++;
                        // If next line is empty, continue the loop to check new quiz
                        // If there's still line to read
                        if(currentpara < paras.size()) {
                            String s1 = paras.get(currentpara).getText();
                            // If next line is empty
                            if(s1.isEmpty()) {
                                currentpara++;
                                break;
                            }
                            else {
                                JOptionPane.showMessageDialog(null,"Error found at line" + (currentpara+1));                              errorFlag = true;
                                fileOpenFlag = false;
                                break;
                            }
                        }
                        else {
                            fileOpenFlag = false;
                            break;
                        }

                    }
                    // If not choices or answers
                    else {
                        JOptionPane.showMessageDialog(null,"Error found at line" + (currentpara+1));
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
}
